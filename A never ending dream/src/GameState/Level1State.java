package GameState;

import Audio.Sound;
import Entity.Enemies.Scheleti;
import Entity.Environment.*;
import Entity.HUD;
import Entity.Player;
import Input.Keys;
import Main.DataBase;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.util.ArrayList;

import static Input.Keys.anyKeyPress;

public class Level1State extends GameState {

    private Background bush, forest1, forest2, forest3, forest4, sky;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Scheleti> enemies;
    private Man man;
    private Tent tent;
    private Horse horse;
    private Boy boy;
    private Girl girl;
    private Entity.HUD HUD;
    private int draw1 = 0;

    private int tick = 0;
    //variabile pentru discursurile entitatilor
    private int ManSpeechCount = 1;
    private int BoySpeechCount = 1;
    private int GirlSpeechCount = 1;

    private boolean blockInput = false;
    private boolean shouldRespawn;
    private boolean dreamover = false;//variabila care stabileste cand sa apara ecranul de game over
    private boolean stop = false;
    //variabila care stabileste faptul ca s-a terminat de incarcat jocul
    //sau ca s-a terminat de facut trecerea de la un nivel la altul
    private boolean done = false;

    private BufferedImage startImage;
    private BufferedImage helpImage;
    private BufferedImage dreamOver;


    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(16);
        tileMap.loadTiles("/Tilesets/castle.png");//incarcam tileset-ul
        tileMap.loadMap("/Maps/map111.map");//incarcam matricea hartii
        tileMap.setPosition(0, 0);//setam pozitia hartii

        //incarcam fiecare parte din fundal
        //partile de fundal mai indepartate isi schimba pozitia mai greu decat cele din fata(Parallax Scrolling)
        bush = new Background("/Background/PS/02_Bushes.png", 0.7);
        forest1 = new Background("/Background/PS/04_Forest.png", 0.6);
        forest2 = new Background("/Background/PS/06_Forest.png", 0.5);
        forest3 = new Background("/Background/PS/07_Forest.png", 0.4);
        forest4 = new Background("/Background/PS/08_Forest.png", 0.3);
        sky = new Background("/Background/PS/10_Sky.png", 0.1);

        //incarcam imaginile care apar la inceputul nivelului si la game over
        try {
            helpImage = ImageIO.read(getClass().getResourceAsStream("/Background/help.png"));
            startImage = ImageIO.read(getClass().getResourceAsStream("/Background/level1.png"));
            dreamOver = ImageIO.read(getClass().getResourceAsStream("/Background/dreamover.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player = new Player(tileMap);
        HUD = new HUD(player);

        man = new Man(tileMap, player);
        man.setPosition(849, 282.9);

        tent = new Tent(tileMap);
        tent.setPosition(121, 253);

        horse = new Horse(tileMap);
        horse.setPosition(770, 248);

        boy = new Boy(tileMap, player);
        boy.setPosition(3163, 155.9);

        girl = new Girl(tileMap, player);
        girl.setPosition(3183, 155.9);

        enemies = new ArrayList<>();

        Sound.load("/SoundEffects/PlayMusic.mp3", "playmusic");


    }
    //functia care populeaza nivelul cu inamici si ii insereaza in baza de date
    private void populate() {

        enemies.clear();
        Scheleti skel;
        //1
        skel = new Scheleti(1, tileMap, player);
        skel.setPosition(2468, 235.9);
        DataBase.getInstance().InsertEnemy(1, 2468, 235.9);
        enemies.add(skel);

        skel = new Scheleti(2, tileMap, player);
        skel.setPosition(2387, 235.9);
        DataBase.getInstance().InsertEnemy(2, 2387, 235.9);
        enemies.add(skel);

        skel = new Scheleti(3, tileMap, player);
        skel.setPosition(2303, 235.9);
        DataBase.getInstance().InsertEnemy(3, 2303, 235.9);
        enemies.add(skel);

        //2
        skel = new Scheleti(4, tileMap, player);
        skel.setPosition(2700, 171.9);
        DataBase.getInstance().InsertEnemy(4, 2700, 171.9);
        enemies.add(skel);

        skel = new Scheleti(5, tileMap, player);
        skel.setPosition(2766, 171.9);
        DataBase.getInstance().InsertEnemy(5, 2766, 171.9);
        enemies.add(skel);

        skel = new Scheleti(6, tileMap, player);
        skel.setPosition(2832, 171.9);
        DataBase.getInstance().InsertEnemy(6, 2832, 171.9);
        enemies.add(skel);

        //3
        skel = new Scheleti(7, tileMap, player);
        skel.setPosition(2932, 171.9);
        DataBase.getInstance().InsertEnemy(7, 2932, 171.9);
        enemies.add(skel);

        skel = new Scheleti(8, tileMap, player);
        skel.setPosition(3014, 171.9);
        DataBase.getInstance().InsertEnemy(8, 3014, 171.9);
        enemies.add(skel);

        skel = new Scheleti(9, tileMap, player);
        skel.setPosition(3108, 171.9);
        DataBase.getInstance().InsertEnemy(9, 3108, 171.9);
        enemies.add(skel);

        //4
        skel = new Scheleti(10, tileMap, player);
        skel.setPosition(3423, 219.9);
        DataBase.getInstance().InsertEnemy(10, 3423, 219.9);
        enemies.add(skel);

        skel = new Scheleti(11, tileMap, player);
        skel.setPosition(3293, 219.9);
        DataBase.getInstance().InsertEnemy(11, 3293, 219.9);
        enemies.add(skel);

        //5
        skel = new Scheleti(12, tileMap, player);
        skel.setPosition(3823, 251.9);
        DataBase.getInstance().InsertEnemy(12, 3823, 251.9);
        enemies.add(skel);

        skel = new Scheleti(13, tileMap, player);
        skel.setPosition(3707, 251.9);
        DataBase.getInstance().InsertEnemy(13, 3707, 251.9);
        enemies.add(skel);
    }

    private int BoySpeechState(int BoySpeechCount) {
        player.stop();//oprim miscarile jucatorului
        player.setSpeech(0);//ii spunem sa nu vorbeasca
        girl.setSpeech(0);//ii spunem entitatii girl sa nu vorbeasca
        boy.setSpeech(BoySpeechCount);//setam discursul entitatii boy
        boy.setSpeaking(true);//setam entitatea sa vorbeasca pentru a influenta functia checkSpeech() din Player
        if (sectick < 60) {//asteptam 2 secunde inainte de a putea trece la urmatorul discurs
            blockInput = true;
            return BoySpeechCount;//cat timp asteptam, returnam inapoi aceeasi valoare a indicelui discursului
        }
        int rez = BoySpeechCount;
        if (sectick > 60)//cand au trecut 2 secunde
            if (anyKeyPress()) {//apasam orice tasta
                blockInput = false;
                rez += 1;//incrementam parametrul de discurs
                sectick = 0;//setam tickul pe 0 pentru a ne pregati de un nou discurs
                turn++;//incrementam randul de discurs din switch
                DataBase.getInstance().UpdateBoyCount(rez);//punem in baza de date informatiile necesare
                DataBase.getInstance().UpdateTurn(turn);
                boy.setSpeaking(false);//oprim entitatea din vorbit
                return rez;//returnam parametrul incrementat
            }
        return BoySpeechCount;
    }
    //manifestari asemanatoare cu functia de sus
    private int GirlSpeechState(int GirlSpeechCount) {
        player.stop();
        player.setSpeech(0);
        boy.setSpeech(0);
        girl.setSpeech(GirlSpeechCount);
        girl.setSpeaking(true);
        if (sectick < 60) {
            blockInput = true;
            return GirlSpeechCount;
        }
        int rez = GirlSpeechCount;
        if (sectick > 60)
            if (anyKeyPress()) {
                blockInput = false;
                rez += 1;
                sectick = 0;
                turn++;
                DataBase.getInstance().UpdateGirlCount(rez);
                DataBase.getInstance().UpdateTurn(turn);
                girl.setSpeaking(false);
                return rez;
            }
        return GirlSpeechCount;
    }
    //manifestari asemanatoare cu functia de sus
    private int ManSpeechState(int ManSpeechCount) {
        player.stop();
        player.setSpeech(0);
        man.setSpeech(ManSpeechCount);
        man.setSpeaking(true);
        if (sectick < 60) {
            blockInput = true;
            return ManSpeechCount;
        }
        int rez = ManSpeechCount;
        if (sectick > 60)
            if (anyKeyPress()) {
                blockInput = false;
                man.setSpeech(0);
                rez += 1;
                sectick = 0;
                turn++;
                DataBase.getInstance().UpdateManCount(rez);
                DataBase.getInstance().UpdateTurn(turn);
                man.setSpeaking(false);
                return rez;
            }
        return ManSpeechCount;
    }
    //manifestari asemanatoare cu functia de sus
    private int speechState(int PlayerSpeechCount) {
        player.stop();
        player.setSpeech(PlayerSpeechCount);
        girl.setSpeech(0);
        boy.setSpeech(0);
        if (sectick < 60) {
            blockInput = true;
            return PlayerSpeechCount;
        }
        int rez = PlayerSpeechCount;
        if (anyKeyPress()) {
            blockInput = false;
            player.setSpeech(0);
            rez += 1;
            sectick = 0;
            turn++;
            DataBase.getInstance().UpdatePlayerCount(rez);
            DataBase.getInstance().UpdateTurn(turn);
            //cand jucatorul vorbeste, pozitia acestuia este actualizata in baza de date ca un fel de auto save cu checkpoint
            DataBase.getInstance().UpdatePlayer(player.getx(), player.gety(), Entity.HUD.getScore(), player.getHealth(), player.getLives());
            return rez;
        }
        return PlayerSpeechCount;

    }
    //incarcam informatiile din baza de date in momentul in care apasam pe Load in meniu
    public void loadGame() {
        try {
            enemies.clear();
            //incarc informatiile despre jucator
            ResultSet r1 = DataBase.getInstance().LoadPlayer();
            player.setPosition(r1.getDouble("playerX"), r1.getDouble("playerY") - 0.01);
            Entity.HUD.setScore(r1.getInt("score"));
            player.setHealth(r1.getInt("health"));
            player.setLives(r1.getInt("lives"));

            //incarc variabile necesare pentru discursuri
            ResultSet flags = DataBase.getInstance().LoadFlags();
            PlayerSpeechCount = flags.getInt("playerspeech");
            ManSpeechCount = flags.getInt("manspeech");
            GirlSpeechCount = flags.getInt("girlspeech");
            BoySpeechCount = flags.getInt("boyspeech");
            draw1 = flags.getInt("draw1");
            turn = flags.getInt("turn");

            ResultSet enm = DataBase.getInstance().LoadEnemy();
            //incarc inamicii din baza de date in array-ul cu inamici
            while (enm.next()) {
                Scheleti skel;
                int id = enm.getInt("id");
                skel = new Scheleti(id, tileMap, player);
                skel.setPosition(enm.getDouble("enemyX"), enm.getDouble("enemyY"));
                enemies.add(skel);
            }

        } catch (Exception e) {
            //daca avem exceptie, incarcam o varianta default a nivelului
            e.printStackTrace();
            System.out.println("Nu s-a putut face incarcarea.");
            player.setPosition(200, 299.9);
            Entity.HUD.setScore(0);
            DataBase.getInstance().DeleteAllEnemies();
            populate();
        }
    }

    //verificam diverse evenimente care pot aparea
    public void eventCheck() {
        sectick++;//la fiecare apel, incrementam acest sectick folosit la discursuri pentru a vedea cand trec doua secunde
        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {//daca nu mai avem viata sau am cazut de pe harta, dam respawn
            shouldRespawn = blockInput = true;
        }
        if (shouldRespawn) respawn();
        //daca am ajuns intr-un anumit punct, trecem la urmatorul nivel
        if (player.getx() >= 4783 && player.gety() <= 124) nextLevel();

        //switch-ul pentru discursurile entitatilor
        switch (turn) {
            case 0 -> { }//nu facem nimic aici
            case 1 -> {
                if (player.getx() >= 200 && player.gety() <= 300 && PlayerSpeechCount == 1 && stop) {
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
                }
            }
            case 2 -> {
                if (player.getx() >= 764 && player.gety() <= 284 && PlayerSpeechCount == 2)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 3 -> {
                if (ManSpeechCount == 1) ManSpeechCount = ManSpeechState(ManSpeechCount);
            }
            case 4 -> {
                if (player.getx() >= 764 && player.gety() <= 284 && PlayerSpeechCount == 3)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 5 -> {
                if (ManSpeechCount == 2) ManSpeechCount = ManSpeechState(ManSpeechCount);
            }
            case 6 -> {
                if (ManSpeechCount == 3) ManSpeechCount = ManSpeechState(ManSpeechCount);
            }
            case 7 -> {
                if (player.getx() >= 764 && player.gety() <= 284 && PlayerSpeechCount == 4)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 8 -> {
                if (ManSpeechCount == 4) ManSpeechCount = ManSpeechState(ManSpeechCount);
            }
            case 9 -> {
                if (player.getx() >= 1388 && player.gety() <= 268 && PlayerSpeechCount == 5)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 10 -> {
                if (player.getx() >= 2181 && player.gety() <= 220 && PlayerSpeechCount == 6)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 11 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && BoySpeechCount == 1)
                    BoySpeechCount = BoySpeechState(BoySpeechCount);
            }
            case 12 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && GirlSpeechCount == 1)
                    GirlSpeechCount = GirlSpeechState(GirlSpeechCount);
            }
            case 13 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && PlayerSpeechCount == 7)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 14 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && PlayerSpeechCount == 8)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 15 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && BoySpeechCount == 2)
                    BoySpeechCount = BoySpeechState(BoySpeechCount);
            }
            case 16 -> {
                if (player.getx() >= 3148 && player.gety() <= 156 && GirlSpeechCount == 2)
                    GirlSpeechCount = GirlSpeechState(GirlSpeechCount);
            }
            case 17 -> {
                boy.setSpeech(0);
                girl.setSpeech(0);
                if (player.getx() >= 3997 && player.gety() <= 236 && PlayerSpeechCount == 9)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 18 -> {
                if (player.getx() >= 4669 && player.gety() <= 124 && PlayerSpeechCount == 10)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 19 -> {
                if (player.getx() >= 4669 && player.gety() <= 124 && PlayerSpeechCount == 11)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
        }
    }
    //functia care stabileste ce afisam pe ecran la pornirea nivelului
    public void startUpdate() {
        if (!stop) {
            tick++;
            blockInput = true;
            player.stop();
            if (tick > 60 && anyKeyPress()) {
                tick = 0;
                draw1++;
                DataBase.getInstance().UpdateDraw1(1);
            }
            if (draw1 == 2) {
                blockInput = false;
                stop = true;

                try {
                    turn = DataBase.getInstance().LoadFlags().getInt("turn");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                draw1 = 3;
                tick = 0;
            }
        }
    }

    public void update() {
        //daca apasam pe Play, incarcam in baza de date si facem operatiile necesare pentru inceperea nivelului
        if (!GameStateManager.shouldLoad() && !done) {
            player.setPosition(200, 299.9);
            DataBase.getInstance().UpdateState(1);
            DataBase.getInstance().InsertPlayer(200, 299.9, Entity.HUD.getScore(), player.getHealth(), player.getLives());
            DataBase.getInstance().InsertFlagsL1(1, 1, 1, 1, 1, 0);
            turn = 1;
            populate();

            done = true;
        }
        //daca apasam pe load, incarcam din baza de date
        if (GameStateManager.shouldLoad()) {
            loadGame();
            GameStateManager.setLoad(false);
            done = true;
        }
        startUpdate();

        player.update();
        man.update();
        tent.update();
        horse.update();
        boy.update();
        girl.update();

        handleInput();

        eventCheck();

        //actualizam pozitia fiecarei parti din fundal
        bush.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest1.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest2.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest3.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest4.setPosition(tileMap.getx(), tileMap.gety() + 40);
        sky.setPosition(tileMap.getx(), tileMap.gety() + 40);

        //mutam pozitia hartii pe ecran in functie de locatia jucatorului
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

        //daca atacam inamici
        player.checkSkelAttack(enemies);

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
            if (enemies.get(i).isDead()) {
                DataBase.getInstance().DeleteEnemy(enemies.get(i).getID());//daca un inamic este ucis, il stergem din baza de date
                enemies.remove(i);
                i--;
            }
        }

    }
    //resetam jucatorul la ultimul checkpoint
    private void reset() {
        player.reset();
        try {
            ResultSet res = DataBase.getInstance().LoadPlayer();
            player.setPosition(res.getDouble("playerX"), res.getDouble("playerY") - 0.01);

        } catch (Exception e) {
            e.printStackTrace();
            player.setPosition(200, 299.9);
        }

        blockInput = false;
        tick = 0;
    }

    public void draw(Graphics2D g) {
        //desenam fundalul
        //cerul(sky) cel mai in spate si tufisurile(bush) cele mai in fata, in contextul fundalului
        sky.draw(g);
        forest4.draw(g);
        forest3.draw(g);
        forest2.draw(g);
        forest1.draw(g);
        bush.draw(g);

        //desenam harta peste fundal
        tileMap.draw(g);
        tent.draw(g);
        //desenam jucatorul peste harta
        horse.draw(g);
        man.draw(g);
        boy.draw(g);
        girl.draw(g);
        player.draw(g);
        HUD.draw(g);

        for (Scheleti enemy : enemies) {
            if (!enemy.isDead())
                enemy.draw(g);
        }
        //desenam imaginea de game over
        if (dreamover)
            g.drawImage(dreamOver, 0, 0, null);

        //desenam imaginile de inceput de nivel
        if (draw1 == 0)
            g.drawImage(helpImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        if (draw1 == 1)
            g.drawImage(startImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
    }

    //functia pentru receptarea apasarii tastelor
    public void handleInput() {
        if (blockInput || player.getHealth() == 0) return;
        player.setJumping(Keys.keyState[Keys.W]);
        player.setLeft(Keys.keyState[Keys.A]);
        player.setRight(Keys.keyState[Keys.D]);
        if (Keys.isPressed(Keys.SHOOT)) player.setFiring(true);
    }


    private void respawn() {
        tick++;

        if (tick == 1) {
            player.setDead();
            player.stop();
        }
        if (tick >= 60) {
            if (player.getLives() == 1) {//daca nu mai avem vieti(vietile se epuizeaza la player.lives-1
                if (tick <= 240) {//afisam ecranul de game over
                    dreamover = true;
                }
                if (tick > 240) {//oprim melodia de joc si pornim melodia din meniu
                    Sound.stop("playmusic");
                    DataBase.getInstance().UpdateState(0);//actualizam in baza de date state-ul curent 0(meniu)
                    gsm.setState(GameStateManager.MENUSTATE);
                    Sound.loop("menumusic");
                }
            } else {//altfel, scadem o viata si resetam jucatorul
                shouldRespawn = blockInput = false;
                tick = 0;
                player.loseLife();
                DataBase.getInstance().UpdateLives(player.getLives());
                reset();
            }
        }
    }

    private void nextLevel() {
        tick++;

        if (tick == 1) {
            blockInput = true;
            player.stop();
        }
        if (tick > 60) {
            //dupa o secunda, se salveaza in baza de date informatii si se sterg toti inamicii din ea pentru a face loc
            //pentru cei din nivelul urmator
            DataBase.getInstance().UpdateState(2);
            DataBase.getInstance().UpdateLives(player.getLives());
            DataBase.getInstance().UpdateHealth(player.getHealth());
            DataBase.getInstance().DeleteAllEnemies();
            done=false;
            stop=false;
            gsm.setState(GameStateManager.LEVEL2STATE);
            blockInput = false;
        }
    }
}
