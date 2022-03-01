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

public class Scheleti extends Enemy {
    private static final int ATTACKING = 0;
    private static final int DYING = 1;
    private static final int WALKING = 2;
    private final Player player;
    private final int[] NUMFRAMES = {
            6, 7, 6
    };
    private final int id;
    private ArrayList<BufferedImage[]> sprite;

    public Scheleti(int id, TileMap tm, Player p) {
        super(tm);
        player = p;
        this.id = id;//setam un id pentru scheleti pentru a-i putea sterge din baza de date

        moveSpeed = 0.6;
        maxSpeed = 0.9;
        fallSpeed = 0.4;
        maxFallSpeed = 10.0;

        //dimensiunile frame-ului sagetii
        width = 115;
        height = 90;
        //dimensiunile hitboxului sagetii
        cwidth = 25;
        cheight = 43;

        health = 1;
        damage = 1;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/scheleti_sprite.png"));
            sprite = new ArrayList<>();
            for (int i = 0; i < NUMFRAMES.length; i++) {
                BufferedImage[] buff = new BufferedImage[NUMFRAMES[i]];//punem fiecare frame al animatiei respective intr-un vector
                for (int j = 0; j < NUMFRAMES[i]; j++)
                    buff[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                sprite.add(buff);
            }

            Sound.load("/SoundEffects/DeadSkel.mp3", "deadskel");

        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        currentAction = WALKING;
        animation.setFrames(sprite.get(WALKING));
        animation.setDelay(90);
        right = true;
        facingRight = true;
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
        //daca jucatorul se afla in apropierea inamicului, acesta se va indrepta inspre jucator
        if (player.getx() < x && player.getx() - x > -150) {
            left = true;
            right = facingRight = false;
        } else if (player.getx() > x && player.getx() - x < 150) {
            left = false;
            right = facingRight = true;
        } else if (right && dx == 0) {
            right = facingRight = false;
            left = true;
        } else if (left && dx == 0) {
            right = facingRight = true;
            left = false;
        }
        //cat timp inamicul nu e mort, acesta va ataca
        if (!isDying()) {
            if (Math.abs(player.getx() - x) < 40 && Math.abs(player.gety() - y) < 20) {
                dx = 0;
                left = false;
                right = false;
                if (currentAction != ATTACKING) {
                    currentAction = ATTACKING;
                    animation.setFrames(sprite.get(ATTACKING));
                    animation.setDelay(80);
                }
                if (animation.getFrames() == 3 || animation.getFrames() == 4)
                    cwidth = 75;
                else
                    cwidth = 25;
            }
        }

        //daca este pe moarte, executam animatia de moarte, redam sunetul de moarte
        if (isDying()) {
            dx = 0;
            left = false;
            right = false;
            if (currentAction != DYING) {
                Sound.play("deadskel");
                currentAction = DYING;
                animation.setFrames(sprite.get(DYING));
                damage = 0;
                animation.setDelay(60);
            }
        }
        //daca a murit si s-a terminat animatia, adaugam punctele la scor
        if (currentAction == DYING && animation.getFrames() == 6) {
            dead = true;
            HUD.addScore(50);
        }

        if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                cwidth = 25;
                animation.setFrames(sprite.get(WALKING));
                animation.setDelay(90);
            }
        }
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);

    }
}
