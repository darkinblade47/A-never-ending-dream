package GameState;

import Audio.Sound;
import Entity.Enemies.Boss;
import Entity.Enemies.Soldati;
import Entity.Enemy;
import Entity.Environment.Ciorescu;
import Entity.HUD;
import Entity.Player;
import Input.Keys;
import Main.DataBase;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import static Entity.Enemies.Boss.isDashing;
import static Input.Keys.anyKeyPress;

public class Level4State extends GameState {
    private TileMap tileMap;
    private Player player;
    private Boss boss;
    private Ciorescu ciorescu;
    private ArrayList<Enemy> enemies;
    private Entity.HUD HUD;

    // events
    private boolean blockInput = false;
    private boolean shouldRespawn;
    private boolean map2loaded = false;
    private boolean map3loaded = false;
    private boolean stop = false;
    private boolean dreamover = false;
    private boolean done=false;

    private int draw1 = 0;
    private int tick = 0;
    private int BossSpeechCount = 1;
    private int CiorescuSpeechCount = 1;

    private BufferedImage startImage;
    private BufferedImage dreamOver;
    private BufferedImage bosshead;
    private Font font;

    public Level4State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        tileMap = new TileMap(16);
        tileMap.loadTiles("/Tilesets/castle.png");//incarcam tileset-ul
        tileMap.loadMap("/Maps/map4a.map");//incarcam matricea hartii
        tileMap.setPosition(0, 0);//setam pozitia hartii
        try {
            font = new Font("Arial", Font.PLAIN, 12);
            BufferedImage bosslife = ImageIO.read(getClass().getResourceAsStream("/Background/boss1613px.png"));
            bosshead = bosslife.getSubimage(0, 0, 16, 13);

            startImage = ImageIO.read(getClass().getResourceAsStream("/Background/level4.png"));
            dreamOver = ImageIO.read(getClass().getResourceAsStream("/Background/dreamover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        player = new Player(tileMap);
        HUD = new HUD(player);

        boss=new Boss(tileMap,player);
        ciorescu = new Ciorescu(tileMap);
        enemies = new ArrayList<>();

        Sound.load("/SoundEffects/BossMusic.mp3", "bossmusic");

    }

    public void populate() {
        enemies.clear();
        boss.setPosition(1020, 300);
        DataBase.getInstance().InsertEnemy(1,1020,299.9);
        enemies.add(boss);
    }
    //functie separata de discurs pentru boss cand isi da dash
    private int BossDashSpeech(int BossSpeechCount) {
        player.setSpeech(0);
        boss.setSpeech(BossSpeechCount);
        boss.setSpeaking(true);
        int rez = BossSpeechCount;
        if (boss.getRageturn() == 3 && sectick > 60) {
            rez += 1;
            sectick = 0;
            boss.setSpeaking(false);
            boss.setSpeech(0);
            return rez;
        }

        if (sectick < 120)
            return BossSpeechCount;

        if (sectick > 120) {
            rez += 1;
            sectick = 0;
            boss.setSpeaking(false);
            boss.setSpeech(0);
            return rez;
        }
        return BossSpeechCount;
    }

    private int BossSpeechState(int BossSpeechCount) {
        player.stop();
        player.setSpeech(0);
        boss.setSpeech(BossSpeechCount);
        boss.setSpeaking(true);
        if (sectick < 60) {
            blockInput = true;
            return BossSpeechCount;
        }
        int rez = BossSpeechCount;
        if (sectick > 60)
            if (anyKeyPress()) {
                blockInput = false;
                rez += 1;
                sectick = 0;
                if (!isDashing())
                    turn++;
                boss.setSpeaking(false);
                return rez;
            }
        return BossSpeechCount;

    }

    private int CiorescuSpeechState(int CiorescuSpeechCount) {
        player.stop();
        player.setSpeech(0);
        ciorescu.setSpeech(CiorescuSpeechCount);
        ciorescu.setSpeaking(true);
        if (sectick < 60) {
            blockInput = true;
            return CiorescuSpeechCount;
        }
        int rez = CiorescuSpeechCount;
        if (sectick > 60)
            if (anyKeyPress()) {
                blockInput = false;
                rez += 1;
                sectick = 0;
                turn++;
                ciorescu.setSpeaking(false);
                return rez;
            }
        return CiorescuSpeechCount;

    }

    private int speechState(int PlayerSpeechCount) {
        player.stop();
        player.setSpeech(PlayerSpeechCount);
        boss.setSpeech(0);
        ciorescu.setSpeech(0);
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
            DataBase.getInstance().UpdatePlayer(player.getx(), player.gety(), Entity.HUD.getScore(), player.getHealth(), player.getLives());
            return rez;
        }
        return PlayerSpeechCount;

    }

    public void loadGame() {
        try {
            enemies.clear();

            ResultSet flags = DataBase.getInstance().LoadFlags();
            PlayerSpeechCount = flags.getInt("playerspeech");
            turn = flags.getInt("turn");
            //incarcam harta de final daca boss-ul a fost invins
            int map2=flags.getInt("map2loaded");
            int map3=flags.getInt("map3loaded");
            map2loaded= map2 == 1;
            map3loaded= map3 == 1;
            if(map3loaded) tileMap.loadMap("/Maps/map4c.map");

            ResultSet r1 = DataBase.getInstance().LoadPlayer();
            player.setPosition(r1.getDouble("playerX"), r1.getDouble("playerY") - 0.01);
            Entity.HUD.setScore(r1.getInt("score"));
            player.setHealth(r1.getInt("health"));
            player.setLives(r1.getInt("lives"));

            ResultSet enm = DataBase.getInstance().LoadEnemy();
            while (enm.next()) {
                boss = new Boss(tileMap, player);
                boss.setPosition(enm.getDouble("enemyX"), enm.getDouble("enemyY"));
                enemies.add(boss);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nu s-a putut face incarcarea.");
            player.setPosition(200, 299.9);
            Entity.HUD.setScore(0);
            DataBase.getInstance().DeleteAllEnemies();
            populate();
        }
    }

    private void eventCheck() {
        sectick++;
        if (player.getHealth() == 0) {
            shouldRespawn = blockInput = true;
        }

        if (shouldRespawn) respawn();
        //daca am ajuns intr-un anumit punct si harta 2 nu a fost incarcata, o incarcam
        //si schimbam pozitiile caracterelor datorita schimbarii coordonatelor
        if (player.getx() >= 503.9 && player.gety() <= 300.1 && !map2loaded) {
            blockInput = true;
            tileMap.loadMap("/Maps/map4b.map");
            map2loaded = true;
            player.setPosition(24, 283.9);
            boss.setPosition(476, 300);
            blockInput = false;
            Sound.loop("bossmusic");
            Sound.stop("playmusic");

        }
        //daca boss-ul este mort, incarcam harta 3 si il pozitionam pe ciorescu in ea
        if (boss.isDead()) {
            tileMap.loadMap("/Maps/map4c.map");
            map3loaded = true;
            blockInput = false;
            ciorescu.setPosition(959, 250);
        }

        switch (turn) {
            case 28 -> {
                if (player.getx() >= 200 && player.gety() <= 300 && PlayerSpeechCount == 20 && stop)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 29 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && PlayerSpeechCount == 21 && map2loaded)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 30 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 1)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 31 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && PlayerSpeechCount == 22 && map2loaded)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 32 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 2)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 33 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 3)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 34 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 4)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 35 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 5)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 36 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && PlayerSpeechCount == 23 && map2loaded)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 37 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 6)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 38 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && BossSpeechCount == 7)
                    BossSpeechCount = BossSpeechState(BossSpeechCount);
            }
            case 39 -> {
                if (player.getx() >= 113 && player.gety() <= 300 && PlayerSpeechCount == 24 && map2loaded)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 40 -> {
                if (player.getx() >= 844 && player.gety() <= 300 && PlayerSpeechCount == 26)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 41 -> {
                if (CiorescuSpeechCount == 1) CiorescuSpeechCount = CiorescuSpeechState(CiorescuSpeechCount);
            }
            case 42 -> {
                if (CiorescuSpeechCount == 2) CiorescuSpeechCount = CiorescuSpeechState(CiorescuSpeechCount);
            }
            case 43 -> {
                if (player.getx() >= 844 && player.gety() <= 300 && PlayerSpeechCount == 27)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 44 -> {
                if (PlayerSpeechCount == 28) nextLevel();
            }
        }
        //switch separat pentru discursul boss-ului cand da dash
        switch (boss.getRageturn()) {
            case 1 -> {
                if (isDashing() && BossSpeechCount == 8) BossSpeechCount = BossDashSpeech(BossSpeechCount);
            }
            case 2 -> {
                if (isDashing() && BossSpeechCount == 9) BossSpeechCount = BossDashSpeech(BossSpeechCount);
            }
            case 3 -> {
                if (isDashing() && BossSpeechCount == 10) BossSpeechCount = BossDashSpeech(BossSpeechCount);
            }
        }

    }

    public void startUpdate() {

        if (!stop) {
            tick++;
            blockInput = true;
            player.stop();
            if (tick > 60 && anyKeyPress()) {
                draw1 = 2;
                stop = true;
                try {
                    turn = DataBase.getInstance().LoadFlags().getInt("turn");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                blockInput = false;
            }
        }
    }



    public void update() {
        if (!GameStateManager.shouldLoad() && !done) {
            boss=new Boss(tileMap,player);
            player.setPosition(200, 299.9);
            try {
                ResultSet rez = DataBase.getInstance().LoadPlayer();
                player.setHealth(rez.getInt("health"));
                player.setLives(rez.getInt("lives"));
                Entity.HUD.setScore(rez.getInt("score"));
            } catch (Exception e) {
                e.printStackTrace();
                populate();
            }
            populate();
            DataBase.getInstance().UpdatePlayer(200, 299.9, Entity.HUD.getScore(), player.getHealth(), player.getLives());
            DataBase.getInstance().UpdateBossCount(1);
            DataBase.getInstance().UpdateCiorescuCount(1);
            DataBase.getInstance().UpdateMap2(0);
            DataBase.getInstance().UpdateMap3(0);
            done = true;
        }

        if (GameStateManager.shouldLoad()) {
            loadGame();
            GameStateManager.setLoad(false);
            done = true;
        }

        startUpdate();

        player.update();
        if (!boss.isDead())
            boss.update();
        if (boss.isDead())
            ciorescu.update();

        handleInput();
        eventCheck();

        //mutam pozitia hartii pe ecran in functie de locatia jucatorului
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

        //daca atacam inamici
        player.checkAttack(enemies);

        //daca boss-ul este pe moarte, facem modificari in baza de date si asupra altor variabile
        if (boss.isDying()) {
            if (PlayerSpeechCount == 25 && !map3loaded) {
                player.stop();
                player.setSpeech(PlayerSpeechCount);
                if (sectick < 120) {
                    blockInput = true;
                } else {
                    blockInput = false;
                    player.setSpeech(0);
                    PlayerSpeechCount++;
                    DataBase.getInstance().UpdatePlayerCount(PlayerSpeechCount);
                    DataBase.getInstance().UpdateTurn(turn);
                    DataBase.getInstance().UpdatePlayer(player.getx(), player.gety(), Entity.HUD.getScore(), player.getHealth(), player.getLives());
                    DataBase.getInstance().UpdateMap2(1);
                    DataBase.getInstance().UpdateMap3(1);
                    sectick = 0;
                }
            }
        }
        //rezolv o problema de incarcare defectuasa din vina momentelor nepotrivite de a inchide jocul
        if((boss.getx()==0.0 && boss.gety()==12.0) && !boss.isDying())
            boss.setDead(true);
        //stergem boss-ul din baza de date daca acesta este mort
        if (boss.isDead()) {
            DataBase.getInstance().DeleteAllEnemies();
            enemies.clear();
        }

    }

    private void reset() {
        player.reset();
        player.setPosition(200, 299.9);
        DataBase.getInstance().DeleteAllEnemies();
        populate();
        boss.setPosition(476, 300);
        blockInput = false;
        tick = 0;
        turn = 40;
        BossSpeechCount = 8;
        boss.setSpeech(0);
        player.setSpeech(0);
        boss.setRageturn(0);
    }

    public void draw(Graphics2D g) {

        //desenam harta peste fundal
        tileMap.draw(g);

        //desenam jucatorul peste harta
        if (boss.isDead() || map3loaded) //
            ciorescu.draw(g);
        player.draw(g);
        HUD.draw(g);
        //cat timp boss-ul este in viata si am incarcat harta 2, afisam pe ecran numarul de vieti sub forma "x""vieti"
        if (map2loaded && !boss.isDead()) {
            g.drawImage(bosshead, 563, 16, null);
            g.setFont(font);
            g.drawString("x" + boss.getHealth(), 563, 48);
        }

        if (!boss.isDead())
            boss.draw(g);

        if (dreamover)
            g.drawImage(dreamOver, 0, 0, null);

        if (draw1 == 0)
            g.drawImage(startImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);



    }

    //functia pentru receptarea apasarii tastelor
    public void handleInput() {
        if (blockInput || player.getHealth() == 0) return;
        player.setJumping(Keys.keyState[Keys.W]);
        player.setLeft(Keys.keyState[Keys.A]);
        player.setRight(Keys.keyState[Keys.D]);
        if (Keys.isPressed(Keys.SHOOT) && turn > 39) player.setFiring(true);
    }

    private void respawn() {
        tick++;

        if (tick == 1) {
            player.setDead();
            player.stop();
        }
        if (tick >= 60) {
            if (player.getLives() == 1) {
                if (tick <= 240) {
                    dreamover = true;
                }
                if (tick > 240) {
                    Sound.stop("playmusic");
                    DataBase.getInstance().UpdateState(0);
                    gsm.setState(GameStateManager.MENUSTATE);
                    Sound.loop("menumusic");
                }
            } else {
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
            tileMap.setTween(1);
        }
        if (tick > 120) {
            DataBase.getInstance().UpdateState(5);
            DataBase.getInstance().UpdateLives(player.getLives());
            DataBase.getInstance().UpdateHealth(player.getHealth());
            DataBase.getInstance().DeleteAllEnemies();
            done=false;
            stop=false;
            gsm.setState(GameStateManager.FINALSTATE);
            blockInput = true;
        }
    }
}

