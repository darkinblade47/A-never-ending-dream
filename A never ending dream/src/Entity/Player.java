package Entity;

import Audio.Sound;
import Entity.Enemies.Boss;
import Entity.Enemies.Scheleti;
import Entity.Enemies.Soldati;
import Entity.Environment.Boy;
import Entity.Environment.Ciorescu;
import Entity.Environment.Girl;
import Entity.Environment.Man;
import GameState.GameState;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Player extends MapObject {

    private static final int IDLE = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int ARROW = 4;
    private static final int DYING = 5;

    private final int maxHealth;//viata maxima
    private final int damage;//daunele provocate de caracater
    private final int[] numFrames = {2, 8, 10, 1, 11, 10};//2 frames pentru IDLE, 8 pentru RUNNING s.a.m.d
    private final int[] nr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    private final ArrayList<Arrow> ArrowBag;
    private int lives;//numarul de vieti
    private int health;//viata curenta
    private long flinchTimer;//durata animatiei de flinching
    private int speech = nr[0];
    private boolean happened = false;
    private boolean dead;//daca caracterul este mort
    private boolean flinching;//daca caracterul a luat daune recent si intra in animatia de flinching
    private boolean firing;//daca caracaterul ataca
    private ArrayList<BufferedImage[]> sprites;
    private BufferedImage[] speeches;


    public Player(TileMap tm) {
        super(tm);
        //dimensiunile unui frame
        width = 64;
        height = 64;
        //dimensiunile hitboxului
        cwidth = 16;
        cheight = 40;
        facingRight = true;

        moveSpeed = 0.6;//viteza de miscare la inceput
        maxSpeed = 3.2;//viteza maxima de miscare
        stopSpeed = 0.5;//viteza de oprire
        fallSpeed = 0.3;//viteza de cadere la inceput
        maxFallSpeed = 4.0;//viteza maxima de cadere
        jumpStart = -7.5;//inaltimea la care se inalta cu o simpla apasare de tasta jump
        stopJumpSpeed = 0.5;//cu cat incetinteste viteza de ridicare a sariturii

        ArrowBag = new ArrayList<Arrow>();

        lives = 3;
        health = maxHealth = 6;
        damage = 2;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/sprite1.png"));
            sprites = new ArrayList<BufferedImage[]>();//un List de vectori de animatii
            for (int i = 0; i < 6; i++) {
                BufferedImage[] buff = new BufferedImage[numFrames[i]];//punem fiecare frame al animatiei respective intr-un vector
                for (int j = 0; j < numFrames[i]; j++)
                    buff[j] = sprite.getSubimage(j * width, i * height, width, height);
                sprites.add(buff);
            }
            speeches = new BufferedImage[29];
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archiesolo.png"));
            speeches[1] = sprite.getSubimage(0, 0, 129, 36);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Am1.png"));
            speeches[2] = sprite.getSubimage(0, 0, 139, 39);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Am3.png"));
            speeches[3] = sprite.getSubimage(0, 0, 34, 33);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Am6.png"));
            speeches[4] = sprite.getSubimage(0, 0, 51, 33);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieforest1.png"));
            speeches[5] = sprite.getSubimage(0, 0, 154, 54);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieforest2.png"));
            speeches[6] = sprite.getSubimage(0, 0, 160, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ak3.png"));
            speeches[7] = sprite.getSubimage(0, 0, 135, 38);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ak4.png"));
            speeches[8] = sprite.getSubimage(0, 0, 175, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ak7.png"));
            speeches[9] = sprite.getSubimage(0, 0, 142, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Archieusa1.png"));
            speeches[10] = sprite.getSubimage(0, 0, 172, 54);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Archieusa2.png"));
            speeches[11] = sprite.getSubimage(0, 0, 142, 38);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archielvl2.png"));
            speeches[12] = sprite.getSubimage(0, 0, 190, 54);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archielevel21.png"));
            speeches[13] = sprite.getSubimage(0, 0, 160, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archielevel22.png"));
            speeches[14] = sprite.getSubimage(0, 0, 148, 39);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieusac.png"));
            speeches[15] = sprite.getSubimage(0, 0, 166, 39);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieusac2.png"));
            speeches[16] = sprite.getSubimage(0, 0, 74, 33);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieusac3.png"));
            speeches[17] = sprite.getSubimage(0, 0, 154, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieusac4.png"));
            speeches[18] = sprite.getSubimage(0, 0, 177, 52);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archieusat.png"));
            speeches[19] = sprite.getSubimage(0, 0, 147, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archietem.png"));
            speeches[20] = sprite.getSubimage(0, 0, 106, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ar1.png"));
            speeches[21] = sprite.getSubimage(0, 0, 86, 34);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ar3.png"));
            speeches[22] = sprite.getSubimage(0, 0, 161, 39);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ar8.png"));
            speeches[23] = sprite.getSubimage(0, 0, 179, 42);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ar11.png"));
            speeches[24] = sprite.getSubimage(0, 0, 143, 37);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/deadking.png"));
            speeches[25] = sprite.getSubimage(0, 0, 110, 38);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ac1.png"));
            speeches[26] = sprite.getSubimage(0, 0, 110, 33);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/Ac4.png"));
            speeches[27] = sprite.getSubimage(0, 0, 61, 35);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/archiesolofinal.png"));
            speeches[28] = sprite.getSubimage(0, 0, 130, 50);

            Sound.load("/SoundEffects/ArrowShot.mp3", "shot");
            Sound.load("/SoundEffects/Hit.mp3", "hit");

        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        currentAction = IDLE;//in prima faza, caracterul este in stare IDLE
        animation.setFrames(sprites.get(IDLE));

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int l) {
        lives = l;
    }

    public void loseLife() {
        lives--;
    }

    public void stop() {
        left = right = flinching = jumping = firing = false;
    }

    public void hit(int damage) {
        if (flinching) return;
        health -= damage;
        Sound.play("hit");
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void setDead() {
        health = 0;
        stop();
    }

    public void reset() {
        health = maxHealth;
        dead = false;
        facingRight = true;
        currentAction = -1;
        stop();
    }

    public void setSpeech(int s) {
        speech = nr[s];
    }

    @Override
    public void setFiring(boolean firing) {
        this.firing = firing;
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
        } else {
            if (dx > 0)         //daca caracterul se indreapta in dreapta din inertie,
            {
                dx -= stopSpeed;//deplasarea se micsoreaza cu stopSpeed pixeli;
                if (dx < 0)     //daca deplasarea depaseste 0 astfel incat sa se indrepte spre stanga,
                    dx = 0;     //deplasarea devine 0 iar caracterul sta pe loc;
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0)
                    dx = 0;
            }
        }


        if ((currentAction == ARROW) && !(jumping || falling)) { //daca tragem cu arcul si nu sarim sau nu cadem,
            dx = 0;                                              //stam pe loc;
        }
        if (jumping && !falling) {      //daca sarim si nu suntem in cadere,
            dy = jumpStart;             //deplasarea in sus incepe cu jumpStart pixeli
        }


        if (falling) {          //daca suntem in cadere
            dy += fallSpeed;    //caracterul se deplaseaza in jos cu fallSpeed pixeli

            if (dy > 0)
                jumping = false;
            if (dy < 0 && !jumping)     //daca caracterul se deplaseaza in sus iar tasta de sarit nu este apasata,
                dy += stopJumpSpeed;    //deplasarea in sus se face cu stopJumpSpeed pixeli mai putin;
            //acest lucru creeaza efectul in care cu cat tinem mai mult apasata,
            //tasta de sarit, cu atat caracterul sare mai sus;
            if (dy > maxFallSpeed)      //daca suntem in cadere si deplasarea este pe cale sa depaseasca viteza maxima de cadere,
                dy = maxFallSpeed;      //deplasarea devine viteza maxima de cadere;
        }


    }

    public void update() {
        getNextPosition();              //actualizam urmatoarea pozitie a jucatorului
        checkTileMapCollision();        //verificam daca nu avem coliziuni
        setPosition(xtemp, ytemp);
        checkSpeech();

        for (int i = 0; i < ArrowBag.size(); i++) {
            ArrowBag.get(i).update();
            if (ArrowBag.get(i).Remove()) {
                ArrowBag.remove(i);
                i--;
            }
        }

        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) {
                flinching = false;
            }
        }
        //daca in animatia de tras cu arcul personajul da drumul la cablu,
        //sageata este generata
        if (firing && animation.getFrames() == 8) {
            if (!happened) {
                Sound.play("shot");
                Arrow sageata = new Arrow(tileMap, facingRight);
                sageata.setPosition(x, y - 7);
                ArrowBag.add(sageata);
                happened = true;
            }
        }

        if (currentAction == ARROW)
            if (animation.PlayedOnce()) {
                firing = false;
                happened = false;
            }

        if (health == 0) {
            stop();
            if (currentAction != DYING) {
                currentAction = DYING;
                animation.setFrames(sprites.get(DYING));
                animation.setDelay(161);
            }
        } else if (firing) {
            if (currentAction != ARROW) {
                currentAction = ARROW;
                animation.setFrames(sprites.get(ARROW));
                animation.setDelay(60);
            }
        } else if (dy > 0) {
            if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(60);
            }
        } else if (left || right) {

            if (currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(90);
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
            }
        }

        if (currentAction != ARROW) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
        animation.update();
    }
    //functie de verificare daune cu soldatii
    public void checkSoldAttack(ArrayList<Soldati> enemies) {
        for (Enemy enemy : enemies) {

            for (Arrow arrow : ArrowBag) {
                if (arrow.intersects(enemy)) {
                    enemy.hit(damage);
                    arrow.setHit();
                    break;
                }
            }

            if (!enemy.isDying() && intersects(enemy)) {
                hit(enemy.getDamage());
            }
        }
    }
    //functie de verificare daune cu inamicii in general
    public void checkAttack(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {

            for (Arrow arrow : ArrowBag) {
                if (arrow.intersects(enemy)) {
                    enemy.hit(damage);
                    arrow.setHit();
                    break;
                }
            }

            // check enemy collision
            if (!enemy.isDying() && intersects(enemy)) {
                hit(enemy.getDamage());
            }
        }
    }
    //functie de verificare daune cu scheletii
    public void checkSkelAttack(ArrayList<Scheleti> enemies) {
        for (Enemy enemy : enemies) {

            for (Arrow arrow : ArrowBag) {
                if (arrow.intersects(enemy)) {
                    enemy.hit(damage);
                    arrow.setHit();
                    break;
                }
            }

            // check enemy collision
            if (!enemy.isDying() && intersects(enemy)) {
                hit(enemy.getDamage());
            }
        }
    }
    //functie care modificare niste parametri necesari discutiilor
    public void checkSpeech() {
        //daca jucatorul are ceva de spus sau una din entitati vorbeste sau boss-ul nu este in dash,
        //acesta sta pe loc
        if (speech != nr[0] || Man.isSpeaking() || Girl.isSpeaking() || Boy.isSpeaking() || (Boss.isSpeaking() && !Boss.isDashing()) || Ciorescu.isSpeaking()) {
            dx = 0;
        } else if (Boss.isSpeaking() && Boss.isDashing()) {//daca boss-ul vorbeste si urmeaza sa dea dash-uri, jucatorul se poate misca liber
        } else//daca nu are nimic de spus, atunci variabile sectick ramane constant 0
            GameState.sectick = 0;
        //o verificare asemanatoare cu prima
        /*
        if (Man.isSpeaking() || Girl.isSpeaking() || Boy.isSpeaking() || (Boss.isSpeaking() && !Boss.isDashing()) || Ciorescu.isSpeaking())
            dx = 0;

         */
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        //verificam care discurs trebuie afisat
        if (speech == nr[1]) {
            g.drawImage(speeches[1], (int) (x + xmap - cwidth - 20), (int) (y + ymap - 43), null);
        }
        if (speech == nr[2]) {
            g.drawImage(speeches[2], (int) (x + xmap - cwidth - 20), (int) (y + ymap - 49), null);
        }
        if (speech == nr[3]) {
            g.drawImage(speeches[3], (int) (x + xmap - cwidth + 13), (int) (y + ymap - 46), null);
        }
        if (speech == nr[4]) {
            g.drawImage(speeches[4], (int) (x + xmap - cwidth + 7), (int) (y + ymap - 46), null);
        }
        if (speech == nr[5]) {
            g.drawImage(speeches[5], (int) (x + xmap - cwidth - 30), (int) (y + ymap - 65), null);
        }
        if (speech == nr[6]) {
            g.drawImage(speeches[6], (int) (x + xmap - cwidth - 30), (int) (y + ymap - 55), null);
        }
        if (speech == nr[7]) {
            g.drawImage(speeches[7], (int) (x + xmap - cwidth - 25), (int) (y + ymap - 47), null);
        }
        if (speech == nr[8]) {
            g.drawImage(speeches[8], (int) (x + xmap - cwidth - 35), (int) (y + ymap - 52), null);
        }
        if (speech == nr[9]) {
            g.drawImage(speeches[9], (int) (x + xmap - cwidth - 25), (int) (y + ymap - 52), null);
        }
        if (speech == nr[10]) {
            g.drawImage(speeches[10], (int) (x + xmap - cwidth - 35), (int) (y + ymap - 65), null);
        }
        if (speech == nr[11]) {
            g.drawImage(speeches[11], (int) (x + xmap - cwidth - 23), (int) (y + ymap - 52), null);
        }
        if (speech == nr[12]) {
            g.drawImage(speeches[12], (int) (x + xmap - cwidth - 39), (int) (y + ymap - 65), null);
        }
        if (speech == nr[13]) {
            g.drawImage(speeches[13], (int) (x + xmap - cwidth - 32), (int) (y + ymap - 55), null);
        }
        if (speech == nr[14]) {
            g.drawImage(speeches[14], (int) (x + xmap - cwidth - 25), (int) (y + ymap - 50), null);
        }
        if (speech == nr[15]) {
            g.drawImage(speeches[15], (int) (x + xmap - cwidth - 30), (int) (y + ymap - 50), null);
        }
        if (speech == nr[16]) {
            g.drawImage(speeches[16], (int) (x + xmap - cwidth - 1), (int) (y + ymap - 45), null);
        }
        if (speech == nr[17]) {
            g.drawImage(speeches[17], (int) (x + xmap - cwidth - 28), (int) (y + ymap - 60), null);
        }
        if (speech == nr[18]) {
            g.drawImage(speeches[18], (int) (x + xmap - cwidth - 34), (int) (y + ymap - 64), null);
        }
        if (speech == nr[19]) {
            g.drawImage(speeches[19], (int) (x + xmap - cwidth - 28), (int) (y + ymap - 55), null);
        }
        if (speech == nr[20]) {
            g.drawImage(speeches[20], (int) (x + xmap - cwidth - 12), (int) (y + ymap - 55), null);
        }
        if (speech == nr[21]) {
            g.drawImage(speeches[21], (int) (x + xmap - cwidth - 5), (int) (y + ymap - 45), null);
        }
        if (speech == nr[22]) {
            g.drawImage(speeches[22], (int) (x + xmap - cwidth - 30), (int) (y + ymap - 48), null);
        }
        if (speech == nr[23]) {
            g.drawImage(speeches[23], (int) (x + xmap - cwidth - 35), (int) (y + ymap - 52), null);
        }
        if (speech == nr[24]) {
            g.drawImage(speeches[24], (int) (x + xmap - cwidth - 24), (int) (y + ymap - 50), null);
        }
        if (speech == nr[25]) {
            g.drawImage(speeches[25], (int) (x + xmap - cwidth - 13), (int) (y + ymap - 50), null);
        }
        if (speech == nr[26]) {
            g.drawImage(speeches[26], (int) (x + xmap - cwidth - 13), (int) (y + ymap - 45), null);
        }
        if (speech == nr[27]) {
            g.drawImage(speeches[27], (int) (x + xmap - cwidth + 4), (int) (y + ymap - 45), null);
        }
        if (speech == nr[28]) {
            g.drawImage(speeches[28], (int) (x + xmap - cwidth - 20), (int) (y + ymap - 60), null);
        }

        //facem "animatia" prin care caracterul apare si dispare de pe ecran in momentul in care ia daune
        if (flinching && health > 0) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0)
                return;
        }

        super.draw(g);
        //desenam sagetile pe ecran
        for (int i = 0; i < ArrowBag.size(); i++) {
            ArrowBag.get(i).draw(g);
        }
    }

}
