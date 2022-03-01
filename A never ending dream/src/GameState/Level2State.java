package GameState;

import Audio.Sound;
import Entity.Enemies.Scheleti;
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

public class Level2State extends GameState {

    private Background bush, forest1, forest2, forest3, forest4, sky;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Scheleti> enemies;
    private Entity.HUD HUD;


    private boolean blockInput = false;
    private boolean shouldRespawn;
    private boolean dreamover = false;
    private boolean stop = false;
    private boolean done = false;

    private int tick = 0;
    private int draw1 = 0;

    private BufferedImage startImage;
    private BufferedImage dreamOver;

    public Level2State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(16);
        tileMap.loadTiles("/Tilesets/castle.png");//incarcam tileset-ul
        tileMap.loadMap("/Maps/map2.map");//incarcam matricea hartii
        tileMap.setPosition(0, 0);//setam pozitia hartii

        //incarcam fiecare parte din fundal
        //partile de fundal mai indepartate isi schimba pozitia mai greu decat cele din fata(Parallax Scrolling)
        bush = new Background("/Background/PS/02_Bushes.png", 0.7);
        forest1 = new Background("/Background/PS/04_Forest.png", 0.6);
        forest2 = new Background("/Background/PS/06_Forest.png", 0.5);
        forest3 = new Background("/Background/PS/07_Forest.png", 0.4);
        forest4 = new Background("/Background/PS/08_Forest.png", 0.3);
        sky = new Background("/Background/PS/10_Sky.png", 0.1);

        try {
            startImage = ImageIO.read(getClass().getResourceAsStream("/Background/level2.png"));
            dreamOver = ImageIO.read(getClass().getResourceAsStream("/Background/dreamover.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player = new Player(tileMap);
        HUD = new HUD(player);

        enemies = new ArrayList<>();

    }

    private void populate() {
        enemies.clear();
        Scheleti skel;

        skel = new Scheleti(14, tileMap, player);
        skel.setPosition(534, 267.9);
        DataBase.getInstance().InsertEnemy(14, 534, 267.9);
        enemies.add(skel);
        skel = new Scheleti(15, tileMap, player);
        skel.setPosition(612, 267.9);
        DataBase.getInstance().InsertEnemy(15, 612, 267.9);
        enemies.add(skel);
        skel = new Scheleti(16, tileMap, player);
        skel.setPosition(676, 267.9);
        DataBase.getInstance().InsertEnemy(16, 676, 267.9);
        enemies.add(skel);
        skel = new Scheleti(17, tileMap, player);
        skel.setPosition(736, 267.9);
        DataBase.getInstance().InsertEnemy(17, 736, 267.9);
        enemies.add(skel);

        skel = new Scheleti(18, tileMap, player);
        skel.setPosition(986, 235.9);
        DataBase.getInstance().InsertEnemy(18, 986, 235.9);
        enemies.add(skel);
        skel = new Scheleti(19, tileMap, player);
        skel.setPosition(1061, 235.9);
        DataBase.getInstance().InsertEnemy(19, 1061, 235.9);
        enemies.add(skel);

        skel = new Scheleti(20, tileMap, player);
        skel.setPosition(1210, 267.9);
        DataBase.getInstance().InsertEnemy(20, 1210, 267.9);
        enemies.add(skel);
        skel = new Scheleti(21, tileMap, player);
        skel.setPosition(1280, 267.9);
        DataBase.getInstance().InsertEnemy(21, 1280, 267.9);
        enemies.add(skel);
        skel = new Scheleti(22, tileMap, player);
        skel.setPosition(1345, 267.9);
        DataBase.getInstance().InsertEnemy(22, 1345, 267.9);
        enemies.add(skel);

        skel = new Scheleti(23, tileMap, player);
        skel.setPosition(1435, 235.9);
        DataBase.getInstance().InsertEnemy(23, 1435, 235.9);
        enemies.add(skel);
        skel = new Scheleti(24, tileMap, player);
        skel.setPosition(1499, 203.9);
        DataBase.getInstance().InsertEnemy(24, 1499, 203.9);
        enemies.add(skel);

        skel = new Scheleti(25, tileMap, player);
        skel.setPosition(1571, 203.9);
        DataBase.getInstance().InsertEnemy(25, 1571, 203.9);
        enemies.add(skel);
        skel = new Scheleti(26, tileMap, player);
        skel.setPosition(1637, 203.9);
        DataBase.getInstance().InsertEnemy(26, 1637, 203.9);
        enemies.add(skel);
        skel = new Scheleti(27, tileMap, player);
        skel.setPosition(1716, 203.9);
        DataBase.getInstance().InsertEnemy(27, 1716, 203.9);
        enemies.add(skel);
        skel = new Scheleti(28, tileMap, player);
        skel.setPosition(1791, 203.9);
        DataBase.getInstance().InsertEnemy(28, 1791, 203.9);
        enemies.add(skel);
        skel = new Scheleti(29, tileMap, player);
        skel.setPosition(1878, 187.9);
        DataBase.getInstance().InsertEnemy(29, 1878, 187.9);
        enemies.add(skel);
        skel = new Scheleti(30, tileMap, player);
        skel.setPosition(2987, 299.9);
        DataBase.getInstance().InsertEnemy(30, 2987, 299.9);
        enemies.add(skel);
        skel = new Scheleti(31, tileMap, player);
        skel.setPosition(3063, 299.9);
        DataBase.getInstance().InsertEnemy(31, 3063, 299.9);
        enemies.add(skel);
        skel = new Scheleti(32, tileMap, player);
        skel.setPosition(3134, 299.9);
        DataBase.getInstance().InsertEnemy(32, 3134, 299.9);
        enemies.add(skel);
        skel = new Scheleti(33, tileMap, player);
        skel.setPosition(3208, 283.9);
        DataBase.getInstance().InsertEnemy(33, 3208, 283.9);
        enemies.add(skel);
        skel = new Scheleti(34, tileMap, player);
        skel.setPosition(3342, 251.9);
        DataBase.getInstance().InsertEnemy(34, 3342, 251.9);
        enemies.add(skel);
        skel = new Scheleti(35, tileMap, player);
        skel.setPosition(3472, 283.9);
        DataBase.getInstance().InsertEnemy(35, 3472, 283.9);
        enemies.add(skel);
        skel = new Scheleti(36, tileMap, player);
        skel.setPosition(3550, 283.9);
        DataBase.getInstance().InsertEnemy(36, 3550, 283.9);
        enemies.add(skel);
        skel = new Scheleti(37, tileMap, player);
        skel.setPosition(3619, 283.9);
        DataBase.getInstance().InsertEnemy(37, 3619, 283.9);
        enemies.add(skel);
        skel = new Scheleti(38, tileMap, player);
        skel.setPosition(3902, 347.9);
        DataBase.getInstance().InsertEnemy(38, 3902, 347.9);
        enemies.add(skel);
        skel = new Scheleti(39, tileMap, player);
        skel.setPosition(3975, 347.9);
        DataBase.getInstance().InsertEnemy(39, 3975, 347.9);
        enemies.add(skel);
        skel = new Scheleti(40, tileMap, player);
        skel.setPosition(4054, 347.9);
        DataBase.getInstance().InsertEnemy(40, 4054, 347.9);
        enemies.add(skel);
        skel = new Scheleti(41, tileMap, player);
        skel.setPosition(4421, 315.9);
        DataBase.getInstance().InsertEnemy(41, 4421, 315.9);
        enemies.add(skel);
        skel = new Scheleti(42, tileMap, player);
        skel.setPosition(4512, 315.9);
        DataBase.getInstance().InsertEnemy(42, 4512, 315.9);
        enemies.add(skel);
        skel = new Scheleti(43, tileMap, player);
        skel.setPosition(4595, 315.9);
        DataBase.getInstance().InsertEnemy(43, 4595, 315);
        enemies.add(skel);
        skel = new Scheleti(44, tileMap, player);
        skel.setPosition(4549, 315.9);
        DataBase.getInstance().InsertEnemy(44, 4549, 315.9);
        enemies.add(skel);
        skel = new Scheleti(45, tileMap, player);
        skel.setPosition(4467, 315.9);
        DataBase.getInstance().InsertEnemy(45, 4467, 315);
        enemies.add(skel);
    }

    private int speechState(int PlayerSpeechCount) {
        player.stop();
        player.setSpeech(PlayerSpeechCount);
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
            ResultSet r1 = DataBase.getInstance().LoadPlayer();
            player.setPosition(r1.getDouble("playerX"), r1.getDouble("playerY") - 0.01);
            Entity.HUD.setScore(r1.getInt("score"));
            player.setHealth(r1.getInt("health"));
            player.setLives(r1.getInt("lives"));

            ResultSet flags = DataBase.getInstance().LoadFlags();
            PlayerSpeechCount = flags.getInt("playerspeech");
            turn = flags.getInt("turn");

            ResultSet enm = DataBase.getInstance().LoadEnemy();
            while (enm.next()) {
                Scheleti skel;
                int id = enm.getInt("id");
                skel = new Scheleti(id, tileMap, player);
                skel.setPosition(enm.getDouble("enemyX"), enm.getDouble("enemyY"));
                enemies.add(skel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nu s-a putut face incarcarea.");
            player.setPosition(200, 267.9);
            Entity.HUD.setScore(0);
            DataBase.getInstance().DeleteAllEnemies();
            populate();
        }


    }

    private void eventCheck() {
        sectick++;

        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
            shouldRespawn = blockInput = true;
        }
        if (shouldRespawn) respawn();
        if (player.getx() >= 4780 && player.gety() >= 300) nextLevel();

        switch (turn) {
            case 20 -> {
                if (player.getx() >= 200 && player.gety() <= 268 && PlayerSpeechCount == 12 && stop)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 21 -> {
                if (player.getx() >= 200 && player.gety() <= 268 && PlayerSpeechCount == 13)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 22 -> {
                if (player.getx() >= 404 && player.gety() <= 252 && PlayerSpeechCount == 14)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 23 -> {
                if (player.getx() >= 4670 && player.gety() <= 300 && PlayerSpeechCount == 15)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
        }
    }

    public void startUpdate() {

        if (!stop) {
            tick++;
            blockInput = true;
            player.stop();
            if (tick > 60 && anyKeyPress()) {
                tick = 0;
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
            player.setPosition(200, 267.9);
            try {
                ResultSet rez = DataBase.getInstance().LoadPlayer();
                player.setHealth(rez.getInt("health"));
                player.setLives(rez.getInt("lives"));
                Entity.HUD.setScore(rez.getInt("score"));
                populate();
            } catch (Exception e) {
                e.printStackTrace();
                populate();
            }
            DataBase.getInstance().UpdatePlayer(200, 267.9, Entity.HUD.getScore(), player.getHealth(), player.getLives());
            done = true;
        }


        if (GameStateManager.shouldLoad()) {
            loadGame();
            GameStateManager.setLoad(false);
            done = true;
        }
        startUpdate();

        player.update();
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
                DataBase.getInstance().DeleteEnemy(enemies.get(i).getID());
                enemies.remove(i);
                i--;
            }
        }
    }

    private void reset() {
        player.reset();
        try {
            ResultSet res = DataBase.getInstance().LoadPlayer();
            player.setPosition(res.getDouble("playerX"), res.getDouble("playerY") - 0.01);

        } catch (Exception e) {
            e.printStackTrace();
            player.setPosition(200, 267.9);
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

        //desenam jucatorul peste harta
        player.draw(g);
        HUD.draw(g);

        for (Scheleti enemy : enemies)
            if (!enemy.isDead())
                enemy.draw(g);

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
        if (Keys.isPressed(Keys.SHOOT)) player.setFiring(true);
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
        }
        if (tick > 60) {
            DataBase.getInstance().UpdateState(3);
            DataBase.getInstance().UpdateLives(player.getLives());
            DataBase.getInstance().UpdateHealth(player.getHealth());
            DataBase.getInstance().DeleteAllEnemies();
            done=false;
            stop=false;
            gsm.setState(GameStateManager.LEVEL3STATE);
            blockInput = false;
        }
    }
}
