package GameState;

import Audio.Sound;
import Entity.Enemies.Soldati;
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

public class Level3State extends GameState {
    private Background bush, forest1, forest2, forest3, forest4, sky;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Soldati> enemies;
    private Entity.HUD HUD;

    private boolean blockInput = false;
    private boolean shouldRespawn;
    private boolean dreamover = false;
    private boolean stop = false;
    private boolean done = false;

    private int draw1 = 0;
    private int tick = 0;

    private BufferedImage startImage;
    private BufferedImage dreamOver;

    public Level3State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(16);
        tileMap.loadTiles("/Tilesets/castle.png");//incarcam tileset-ul
        tileMap.loadMap("/Maps/map3.map");//incarcam matricea hartii
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
            startImage = ImageIO.read(getClass().getResourceAsStream("/Background/level3.png"));
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
        Soldati soldat;

        soldat = new Soldati(46, tileMap, player);
        soldat.setPosition(400, 145);
        DataBase.getInstance().InsertEnemy(46, 400, 145);
        enemies.add(soldat);
        soldat = new Soldati(47, tileMap, player);
        soldat.setPosition(1149, 209);
        DataBase.getInstance().InsertEnemy(47, 1149, 209);
        enemies.add(soldat);
        soldat = new Soldati(48, tileMap, player);
        soldat.setPosition(1730, 305);
        DataBase.getInstance().InsertEnemy(48, 1730, 305);
        enemies.add(soldat);
        soldat = new Soldati(49, tileMap, player);
        soldat.setPosition(2292, 369);
        DataBase.getInstance().InsertEnemy(49, 2292, 369);
        enemies.add(soldat);
        soldat = new Soldati(50, tileMap, player);
        soldat.setPosition(2567, 369);
        DataBase.getInstance().InsertEnemy(50, 2567, 369);
        enemies.add(soldat);
        soldat = new Soldati(51, tileMap, player);
        soldat.setPosition(2839, 369);
        DataBase.getInstance().InsertEnemy(51, 2839, 369);
        enemies.add(soldat);
        soldat = new Soldati(52, tileMap, player);
        soldat.setPosition(3037, 337);
        DataBase.getInstance().InsertEnemy(52, 3037, 337);
        enemies.add(soldat);
        soldat = new Soldati(53, tileMap, player);
        soldat.setPosition(3279, 337);
        DataBase.getInstance().InsertEnemy(53, 3279, 337);
        enemies.add(soldat);
        soldat = new Soldati(54, tileMap, player);
        soldat.setPosition(3616, 337);
        DataBase.getInstance().InsertEnemy(54, 3616, 337);
        enemies.add(soldat);
        soldat = new Soldati(55, tileMap, player);
        soldat.setPosition(4048, 353);
        DataBase.getInstance().InsertEnemy(55, 4048, 353);
        enemies.add(soldat);
        soldat = new Soldati(56, tileMap, player);
        soldat.setPosition(4384, 385);
        DataBase.getInstance().InsertEnemy(56, 4384, 385);
        enemies.add(soldat);
        soldat = new Soldati(57, tileMap, player);
        soldat.setPosition(4519, 417);
        DataBase.getInstance().InsertEnemy(57, 4519, 417);
        enemies.add(soldat);
        soldat = new Soldati(58, tileMap, player);
        soldat.setPosition(4758, 417);
        DataBase.getInstance().InsertEnemy(58, 4758, 417);
        enemies.add(soldat);


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
                Soldati sold;
                int id = enm.getInt("id");
                sold = new Soldati(id, tileMap, player);
                sold.setPosition(enm.getDouble("enemyX"), enm.getDouble("enemyY"));
                enemies.add(sold);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nu s-a putut face incarcarea.");
            player.setPosition(200, 139.9);
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
        if (player.getx() >= 4771 && player.gety() >= 427) nextLevel();

        switch (turn) {
            case 24 -> {
                if (player.getx() >= 200 && player.gety() <= 140 && PlayerSpeechCount == 16 && stop)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 25 -> {
                if (player.getx() >= 200 && player.gety() <= 140 && PlayerSpeechCount == 17)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 26 -> {
                if (player.getx() >= 200 && player.gety() <= 140 && PlayerSpeechCount == 18)
                    PlayerSpeechCount = speechState(PlayerSpeechCount);
            }
            case 27 -> {
                if (player.getx() >= 4676 && player.gety() <= 428 && PlayerSpeechCount == 19)
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
            player.setPosition(200, 139.9);
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
            DataBase.getInstance().UpdatePlayer(200, 139.9, Entity.HUD.getScore(), player.getHealth(), player.getLives());
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
        player.checkSoldAttack(enemies);

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
            player.setPosition(200, 139.9);
        }
        blockInput = false;
        tick = 0;
    }

    public void draw(Graphics2D g) {
        //desenam fundalul
        //cerul(sky) cel mai in spate si tufisurile(bush) cele mai in fata, in contextul fundalului
        sky.draw(g);
        //forest5.draw(g);
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

        for (Soldati enemy : enemies)
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
        if (tick > 120) {
            DataBase.getInstance().UpdateState(4);
            DataBase.getInstance().UpdateLives(player.getLives());
            DataBase.getInstance().UpdateHealth(player.getHealth());
            DataBase.getInstance().DeleteAllEnemies();
            done=false;
            stop=false;
            gsm.setState(GameStateManager.LEVEL4STATE);
            blockInput = true;
        }
    }
}

