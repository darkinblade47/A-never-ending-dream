package Entity.Enemies;

import Audio.Sound;
import Entity.Animation;
import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Soldati extends Enemy {
    private static final int ATTACKING = 0;
    private static final int IDLE = 1;
    private static final int WALKING = 2;
    private final Player player;
    private final int[] NUMFRAMES = {
            7, 4, 8
    };
    private final int id;
    private ArrayList<BufferedImage[]> sprite;
    private boolean following = false;
    private boolean took1 = false;
    private boolean took2 = false;

    public Soldati(int id, TileMap tm, Player p) {
        super(tm);
        player = p;
        this.id = id;

        moveSpeed = 0.6;
        maxSpeed = 2.5;
        fallSpeed = 0.4;
        maxFallSpeed = 10.0;

        //dimensiunile frame-ului sagetii
        width = 80;
        height = 100;
        //dimensiunile hitboxului sagetii
        cwidth = 29;
        cheight = 62;

        health = maxHealth = 3;
        damage = 2;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/soldati.png"));
            sprite = new ArrayList<>();
            for (int i = 0; i < NUMFRAMES.length; i++) {
                BufferedImage[] buff = new BufferedImage[NUMFRAMES[i]];//punem fiecare frame al animatiei respective intr-un vector
                for (int j = 0; j < NUMFRAMES[i]; j++)
                    buff[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                sprite.add(buff);
            }
            //Sound.load("/SoundEffects/Hit.mp3","hit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprite.get(IDLE));
        animation.setDelay(200);
        facingRight = false;
    }

    private void getNextPosition() {
        if (left)                   //daca caracterul se indreapta in stanga la actiunea tastelor,
        {
            dx -= moveSpeed;        //se va deplasa la stanga cu moveSpeed pixeli;
            if (dx < -maxSpeed)     //daca deplasarea depaseste viteza maxima,
                dx = -maxSpeed;     //deplasarea va deveni maxSpeed pixeli;
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed)
                dx = maxSpeed;
        }
        if (falling) {
            dy += fallSpeed;
        }


    }

    public int getID() {
        return id;
    }

    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        //redam sunetul de dauna primita de fiecare data cand primeste daune si nu este inca mort
        if (health == maxHealth - 1 && !took1) {
            Sound.play("hit");
            took1 = true;
        }
        if (health == maxHealth - 2 && !took2) {
            Sound.play("hit");
            took2 = true;
        }
        if (isDying())
            Sound.play("hit");

        //daca jucatorul este aproape de inamic, inamicul se va indrepta spre jucator
        if (player.getx() < x && player.getx() - x > -150) {
            left = true;
            right = facingRight = false;
            following = true;
        } else if (player.getx() > x && player.getx() - x < 150) {
            left = false;
            right = facingRight = true;
            following = true;
        }

        if (Math.abs(player.getx() - x) < 42 && Math.abs(player.gety() - y) < 40) {
            following = true;
            dx = 0;
            left = false;
            right = false;
            if (currentAction != ATTACKING) {
                currentAction = ATTACKING;
                animation.setFrames(sprite.get(ATTACKING));
                animation.setDelay(80);
                dx = 0;
                left = false;
                right = false;
            }
            if (animation.getFrames() == 4 || animation.getFrames() == 5 || animation.getFrames() == 6)
                cwidth = 70;
            else
                cwidth = 29;
        } else if (left || right) {
            if (currentAction != WALKING) {
                cwidth = 29;
                currentAction = WALKING;
                animation.setFrames(sprite.get(WALKING));
                animation.setDelay(90);
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprite.get(IDLE));
                animation.setDelay(200);
                cheight = 52;
                cwidth = 29;
            }
        }

        //daca este lovit, se va indrepta spre jucator
        if (health <= 2) {
            if (player.getx() < x) {
                left = true;
                right = facingRight = false;
                following = true;
            } else if (player.getx() > x) {
                left = false;
                right = facingRight = true;
                following = true;
            }
        }

        if (isDying()) {
            dead = true;
            HUD.addScore(100);
        }
        animation.update();

    }

    public void draw(Graphics2D g) {
        setMapPosition();
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
