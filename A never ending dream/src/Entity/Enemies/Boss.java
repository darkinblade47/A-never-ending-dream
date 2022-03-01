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

public class Boss extends Enemy {
    private static final int TAUNT = 0;
    private static final int WALKING = 1;
    private static final int DASH = 2;
    private static final int JUMPING = 3;
    private static final int ATTACKING = 4;
    private static final int LEAP = 5;
    private static final int DYING = 6;
    private static final int[] nr4 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private BufferedImage[] speeches4;
    private int speech4 = nr4[0];
    private static boolean Dash = false;
    private static boolean speaking;
    private final Player player;
    private final int[] NUMFRAMES = {
            18, 8, 8, 25, 30, 40, 41
    };
    private final int[] WIDTHFRAME = {
            187, 187, 187, 242, 187, 286, 187
    };
    private final int[] HEIGHTFRAME = {
            106, 106, 106, 106, 106, 151, 106
    };
    private ArrayList<BufferedImage[]> sprite;


    private int count = 0;
    private int rageturn = 0;
    private int dmg = 1;
    private int tick = 0;

    private boolean doable = false;
    private boolean inposition = false;
    private boolean onlyonce = false;
    private boolean onlyonce2 = false;
    private boolean onlyonce3 = false;

    public Boss(TileMap tm, Player p) {
        super(tm);
        player = p;

        //dimensiunile hitboxului
        cwidth = 31;
        cheight = 41;

        moveSpeed = 0.6;//viteza de miscare la inceput
        maxSpeed = 0.9;//viteza maxima de miscare
        fallSpeed = 0.4;//viteza de cadere la inceput
        maxFallSpeed = 10;//viteza maxima de cadere
        jumpStart = -10.5;//inaltimea la care se inalta cu o simpla apasare de tasta jump
        stopJumpSpeed = 0.2;//cu cat incetinteste viteza de ridicare a sariturii

        health = maxHealth = 20;
        damage = 1;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/boss2.png"));
            int count = 0;
            sprite = new ArrayList<>();
            for (int i = 0; i < NUMFRAMES.length; i++) {
                BufferedImage[] buff = new BufferedImage[NUMFRAMES[i]];//punem fiecare frame al animatiei respective intr-un vector
                for (int j = 0; j < NUMFRAMES[i]; j++) {
                    if (i == 6)
                        buff[j] = spritesheet.getSubimage(j * WIDTHFRAME[i], count, WIDTHFRAME[i], HEIGHTFRAME[i]);
                    else
                        buff[j] = spritesheet.getSubimage(j * WIDTHFRAME[i], count + 10, WIDTHFRAME[i], HEIGHTFRAME[i]);
                }
                sprite.add(buff);
                count += HEIGHTFRAME[i];
            }
            //aici incarc imaginile ce contin convorbirile cu jucatorul
            speeches4 = new BufferedImage[11];
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR2.png"));
            speeches4[1] = spritesheet.getSubimage(0, 0, 183, 33);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR4.png"));
            speeches4[2] = spritesheet.getSubimage(0, 0, 173, 50);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR5.png"));
            speeches4[3] = spritesheet.getSubimage(0, 0, 182, 62);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR6.png"));
            speeches4[4] = spritesheet.getSubimage(0, 0, 154, 39);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR7.png"));
            speeches4[5] = spritesheet.getSubimage(0, 0, 146, 37);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR9.png"));
            speeches4[6] = spritesheet.getSubimage(0, 0, 184, 54);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aR10.png"));
            speeches4[7] = spritesheet.getSubimage(0, 0, 165, 50);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/R1.png"));
            speeches4[8] = spritesheet.getSubimage(0, 0, 70, 33);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/R2.png"));
            speeches4[9] = spritesheet.getSubimage(0, 0, 87, 53);
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/R3.png"));
            speeches4[10] = spritesheet.getSubimage(0, 0, 147, 34);

        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        currentAction = TAUNT;//in prima faza, acesta ne provoaca
        animation.setFrames(sprite.get(TAUNT));
        animation.setDelay(40);
        facingRight = false;
    }

    //daca boss-ul vorbeste
    //ma folosesc de aceasta functie pentru a controla actiunile jucatorului in convorbiri
    public static boolean isSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean s) {
        speaking = s;
    }

    //daca boss-ul da dash-uri
    //ma folosesc de aceasta functie pentru a controla actiunile jucatorului in convorbiri
    public static boolean isDashing() {
        return Dash;
    }

    //setez indicele pentru discurs
    public void setSpeech(int s) {
        speech4 = nr4[s];
    }

    //returnez in care stadiu de "rage"(al catelea stadiu de dash) se afla boss-ul
    public int getRageturn() { return rageturn; }

    //setez stadiul de rage necesar in resetarea boss-ului
    public void setRageturn(int r) {
        rageturn = r;
    }

    //returnez viata acestuia
    public int getHealth() {
        return health;
    }

    //setez boss-ul sa fie mort
    public void setDead(boolean d){dead=d;}


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
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //daca boss-ul sufera daune, se da play sunetului de "hit"
        if (health == (maxHealth - dmg)) {
            Sound.play("hit");
            dmg++;
        }

        //in functie de actiunea curenta, dimensiunile frame-ului din sprite se schimba deoarece nu sunt constante
        switch (currentAction) {
            case 0, 1, 2, 4, 6 -> {
                width = 187;
                height = 106;
            }
            case 3 -> {
                width = 242;
                height = 96;
            }
            case 5 -> {
                width = 286;
                height = 185;
            }
        }

        //daca boss-ul nu este pe moarte
        if (!isDying()) {
            //verificam daca trebuie sa intre in stadiul 2
            if (health <= (maxHealth - 5) && !onlyonce) {
                tick = 2;
                rageturn++;
                onlyonce = true;
            }
            //verificam daca trebuie sa intre in stadiul 4
            if (health <= (maxHealth - 10) && !onlyonce2) {
                tick = 4;
                onlyonce2 = true;
            }
            //verificam daca trebuie sa intre in stadiul 5
            if (health <= (maxHealth - 15) && !onlyonce3) {
                tick = 5;
                count = 0;
                onlyonce3 = true;
            }
            //pornim animatia de mers
            if ((left || right) && dx != 0) {
                if (currentAction != WALKING) {
                    currentAction = WALKING;
                    animation.setFrames(sprite.get(WALKING));
                    animation.setDelay(90);
                }
            }

            //un switch care seteaza stadiile de manifestare ale boss-ului
            switch (tick) {
                //prima oara asteptam ca acesta sa primeasca un punct de dauna pentru a se putea misca
                case 0 -> {
                    if (health == (maxHealth - 2))
                        tick = 1;
                    Dash = false;
                }
                //in stadiul 1, acesta doar urmareste caracterul cu o viteza maxima mai ridicata
                case 1 -> {
                    maxSpeed = 1.5;
                    if (player.getx() < x && Math.abs(player.getx() - x) > 2) {
                        left = true;
                        right = facingRight = false;
                    } else if (player.getx() > x && Math.abs(player.getx() - x) > 2) {
                        left = false;
                        right = facingRight = true;
                    } else {
                        dx = 0;
                        right = left = false;
                        facingRight = player.getFacing();
                    }
                    //daca boss-ul este destul de aproape de jucator, il va ataca
                    if (Math.abs(player.getx() - x) < 35 && Math.abs(player.gety() - y) < 20) {
                        dx = 0;
                        left = false;
                        right = false;
                        if (currentAction != ATTACKING) {
                            currentAction = ATTACKING;
                            animation.setFrames(sprite.get(ATTACKING));
                            animation.setDelay(25);
                        }
                        if (currentAction == ATTACKING && ((animation.getFrames() >= 5 && animation.getFrames() <= 8) || (animation.getFrames() >= 14 && animation.getFrames() <= 18)))
                            cwidth = 58;
                        else cwidth = 31;
                    } else cwidth = 31;
                }
                //in stadiul 2, acesta va da dash-uri repetate pe harta, schimbandu-si directia la coliziunea cu peretele
                //daca nu se afla intr-o anumita pozitie pe harta, acesta se va indrepta acolo
                //abia dupa ce este in pozitie, va executa succesiunea de dash-uri
                case 2 -> {
                    Dash = true;
                    immune = true;//in timpul animatiei, acesta devine imun la daune
                    if (!inposition) right = facingRight = true;//daca nu este in pozitie, se indreapta spre ea
                    left = false;
                    if (x >= 476 && !inposition) {//daca a ajuns in pozitie, se fac niste schimbari de valori
                        right = false;
                        facingRight = false;
                        doable = true;
                        inposition = true;
                        dx = 0;
                    }
                    if (currentAction != TAUNT && doable) {//boss-ul face o animatie completa de TAUNT
                        currentAction = TAUNT;
                        animation.setFrames(sprite.get(TAUNT));
                        animation.setDelay(80);
                    }
                    //in momentul in care nimereste peretele din stanga, se intoarce
                    if (animation.PlayedOnce() && dx == 0 && x < 200 && count != 7) {
                        count++;
                        dx = 6;
                        facingRight = true;
                    }
                    //in momentul in care nimereste peretele din dreapta, se intoarce
                    if (animation.PlayedOnce() && dx == 0 && x > 200 && count != 7) {
                        count++;
                        dx = -6;
                        facingRight = false;
                    }
                    //dupa un anumit numar de dash-uri si cand ajunge intr-o pozitie pe harta,
                    //se opreste din dash-uri, face un TAUNT si trece la urmatorul stadiu de atac
                    if (count == 7 && x >= 476) {
                        damage = 1;
                        cwidth = 31;
                        dx = 0;
                        facingRight = false;
                        if (currentAction != TAUNT) {
                            currentAction = TAUNT;
                            animation.setFrames(sprite.get(TAUNT));
                            animation.setDelay(80);

                        }
                        //schimbarea stadiului de atac
                        if (currentAction == TAUNT && animation.getFrames() == 17) {
                            Dash = false;
                            rageturn++;
                            tick = 3;
                            doable = inposition = false;
                            count = 0;
                        }
                    }
                    //daca a terminat de facut primul TAUNT, sa inceapa animatiile de dash
                    if (currentAction == TAUNT && animation.PlayedOnce()) {
                        doable = false;
                        if (currentAction != DASH) {
                            damage = 2;
                            cwidth = 81;
                            currentAction = DASH;
                            animation.setFrames(sprite.get(DASH));
                            animation.setDelay(40);
                        }
                    }
                }
                //in stadiul 3, boss-ul poate sari pe platforme daca jucatorul se afla la inaltime,
                //dar doar din anumite pozitii
                case 3 -> {
                    immune = false;
                    if (y == 300) maxSpeed = 1.5;
                    if (player.getx() < x && Math.abs(player.getx() - x) > 2) {
                        left = true;
                        right = facingRight = false;
                        if (player.gety() < y && ((x >= 486 && x <= 496) || (x >= 308 && x <= 328)) && y >= 200 && player.gety() >= 124 && player.gety() <= 204) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 202 && y < 204) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;

                        }
                        if (player.gety() < y && (x >= 190 && x <= 200) && y > 100 && y < 210) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 80 && y < 82) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                    } else if (player.getx() > x && Math.abs(player.getx() - x) > 2) {
                        left = false;
                        right = facingRight = true;

                        if (player.gety() < y && ((x >= 486 && x <= 496) || (x >= 308 && x <= 328)) && y >= 200 && player.gety() >= 124 && player.gety() <= 204) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 202 && y < 204) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                        if (player.gety() < y && (x >= 440 && x <= 450) && y > 100 && y < 210) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 80 && y < 82) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                    } else {
                        dx = 0;
                        right = left = false;
                        facingRight = player.getFacing();
                    }
                    if (Math.abs(player.getx() - x) < 35 && Math.abs(player.gety() - y) < 20) {
                        dx = 0;
                        left = false;
                        right = false;
                        if (currentAction != ATTACKING) {
                            currentAction = ATTACKING;
                            animation.setFrames(sprite.get(ATTACKING));
                            animation.setDelay(25);
                        }
                        if (currentAction == ATTACKING && ((animation.getFrames() >= 5 && animation.getFrames() <= 8) || (animation.getFrames() >= 14 && animation.getFrames() <= 18)))
                            cwidth = 54;
                        else cwidth = 31;
                    } else cwidth = 31;

                }
                //stadiul 4 este asemanator cu stadiul 2, doar ca boss-ul este mai rapid
                case 4 -> {
                    Dash = true;
                    immune = true;
                    if (!inposition) right = facingRight = true;
                    left = false;
                    if (x >= 476 && !inposition) {
                        right = false;
                        facingRight = false;
                        doable = true;
                        inposition = true;
                        dx = 0;
                    }
                    if (currentAction != TAUNT && doable) {
                        currentAction = TAUNT;
                        animation.setFrames(sprite.get(TAUNT));
                        animation.setDelay(80);
                    }

                    if (animation.PlayedOnce() && dx == 0 && x < 200 && count != 7) {
                        count++;
                        dx = 12;
                        facingRight = true;
                    }
                    if (animation.PlayedOnce() && dx == 0 && x > 200 && count != 7) {
                        count++;
                        dx = -12;
                        facingRight = false;
                    }
                    if (count == 7 && x >= 476) {
                        damage = 1;
                        cwidth = 31;
                        dx = 0;
                        facingRight = false;
                        if (currentAction != TAUNT) {
                            currentAction = TAUNT;
                            animation.setFrames(sprite.get(TAUNT));
                            animation.setDelay(80);

                        }
                        if (currentAction == TAUNT && animation.getFrames() == 17) {
                            Dash = false;
                            rageturn++;
                            doable = inposition = false;
                            tick = 3;

                        }

                    }
                    if (currentAction == TAUNT && animation.PlayedOnce()) {
                        doable = false;
                        if (currentAction != DASH) {
                            damage = 2;
                            cwidth = 81;
                            currentAction = DASH;
                            animation.setFrames(sprite.get(DASH));
                            animation.setDelay(25);
                        }
                    }
                }
                //in stadiul 5, boss-ul nu trebuie sa mai fie intr-o anumita pozitie,
                //doar executa un TAUNT si intra intr-o succesiune mai rapida si mai lunga de dash-uri
                case 5 -> {
                    Dash = true;
                    immune = true;
                    if (!inposition) {
                        right = left = false;
                        doable = true;
                        inposition = true;
                        dx = 0;
                    }

                    if (currentAction != TAUNT && doable) {
                        currentAction = TAUNT;
                        animation.setFrames(sprite.get(TAUNT));
                        animation.setDelay(80);
                    }
                    if (animation.PlayedOnce() && dx == 0 && x < 200 && count != 14) {
                        count++;
                        dx = 18;
                        facingRight = true;
                    }
                    if (animation.PlayedOnce() && dx == 0 && x > 200 && count != 14) {
                        count++;
                        dx = -18;
                        facingRight = false;
                    }
                    if (count == 14) {
                        damage = 1;
                        cwidth = 31;
                        dx = 0;
                        facingRight = false;
                        if (currentAction != TAUNT) {
                            currentAction = TAUNT;
                            animation.setFrames(sprite.get(TAUNT));
                            animation.setDelay(80);

                        }
                        if (currentAction == TAUNT && animation.getFrames() == 17) {
                            Dash = false;
                            inposition = false;
                            rageturn++;

                            tick = 6;

                        }
                    }
                    if (currentAction == TAUNT && animation.PlayedOnce()) {
                        doable = false;
                        if (currentAction != DASH) {
                            damage = 3;
                            cwidth = 81;
                            currentAction = DASH;
                            animation.setFrames(sprite.get(DASH));
                            animation.setDelay(25);
                        }
                    }


                }
                //stadiul 6 se aseamana cu stadiul 4, doar ca aici boss-ul este mai rapid cand nu merge pe platforme
                case 6 -> {
                    immune = false;
                    if (y == 300) maxSpeed = 1.9;
                    if (player.getx() < x && Math.abs(player.getx() - x) > 2) {
                        left = true;
                        right = facingRight = false;
                        if (player.gety() < y && ((x >= 486 && x <= 496) || (x >= 308 && x <= 328)) && y >= 200 && player.gety() >= 124 && player.gety() <= 204) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 202 && y < 204) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;

                        }
                        if (player.gety() < y && (x >= 190 && x <= 200) && y > 100 && y < 210) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 80 && y < 82) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                    } else if (player.getx() > x && Math.abs(player.getx() - x) > 2) {
                        left = false;
                        right = facingRight = true;

                        if (player.gety() < y && ((x >= 486 && x <= 496) || (x >= 308 && x <= 328)) && y >= 200 && player.gety() >= 124 && player.gety() <= 204) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 202 && y < 204) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                        if (player.gety() < y && (x >= 440 && x <= 450) && y > 100 && y < 210) {
                            maxSpeed = 2.5;
                            moveSpeed = 2.5;
                            jumping = true;
                        }
                        if (y >= 80 && y < 82) {
                            jumping = false;
                            moveSpeed = 0.6;
                            maxSpeed = 0.9;
                        }
                    } else {
                        dx = 0;
                        right = left = false;
                        facingRight = player.getFacing();
                    }
                    if (Math.abs(player.getx() - x) < 35 && Math.abs(player.gety() - y) < 20) {
                        dx = 0;
                        left = false;
                        right = false;
                        if (currentAction != ATTACKING) {
                            currentAction = ATTACKING;
                            animation.setFrames(sprite.get(ATTACKING));
                            animation.setDelay(25);
                        }
                        if (currentAction == ATTACKING && ((animation.getFrames() >= 5 && animation.getFrames() <= 8) || (animation.getFrames() >= 14 && animation.getFrames() <= 18)))
                            cwidth = 54;
                        else cwidth = 31;
                    } else cwidth = 31;

                }
            }
        //daca este pe moarte, afisam animatia de moarte si ii oprim orice deplasare
        } else if (isDying()) {
            dx = 0;
            left = false;
            right = false;
            if (currentAction != DYING) {
                currentAction = DYING;
                animation.setFrames(sprite.get(DYING));
                damage = 0;
                animation.setDelay(120);
            }
        }
        //daca a murit, oprim muzica tensionata si o pornim inapoi pe cea calma si adaugam punctele la scor
        if (currentAction == DYING && animation.getFrames() == 40) {
            dead = true;
            HUD.addScore(1000);
            Sound.stop("bossmusic");
            Sound.loop("playmusic");
        }

        animation.update();
    }

    public void draw(Graphics2D g) {

        //verific care discurs trebuie afisat pe ecran
        if (speech4 == nr4[1]) {
            g.drawImage(speeches4[1], (int) (x + xmap - cwidth - 93), (int) (y + ymap - 43), null);
        }
        if (speech4 == nr4[2]) {
            g.drawImage(speeches4[2], (int) (x + xmap - cwidth - 84), (int) (y + ymap - 55), null);
        }
        if (speech4 == nr4[3]) {
            g.drawImage(speeches4[3], (int) (x + xmap - cwidth - 90), (int) (y + ymap - 67), null);
        }
        if (speech4 == nr4[4]) {
            g.drawImage(speeches4[4], (int) (x + xmap - cwidth - 74), (int) (y + ymap - 47), null);
        }
        if (speech4 == nr4[5]) {
            g.drawImage(speeches4[5], (int) (x + xmap - cwidth - 68), (int) (y + ymap - 47), null);
        }
        if (speech4 == nr4[6]) {
            g.drawImage(speeches4[6], (int) (x + xmap - cwidth - 94), (int) (y + ymap - 59), null);
        }
        if (speech4 == nr4[7]) {
            g.drawImage(speeches4[7], (int) (x + xmap - cwidth - 82), (int) (y + ymap - 59), null);
        }
        if (speech4 == nr4[8]) {
            g.drawImage(speeches4[8], (int) (x + xmap - cwidth - 20), (int) (y + ymap - 43), null);
        }
        if (speech4 == nr4[9]) {
            g.drawImage(speeches4[9], (int) (x + xmap - cwidth - 36), (int) (y + ymap - 49), null);
        }
        if (speech4 == nr4[10]) {
            g.drawImage(speeches4[10], (int) (x + xmap - cwidth - 70), (int) (y + ymap - 43), null);
        }

        setMapPosition();
        super.draw(g);

    }
}
