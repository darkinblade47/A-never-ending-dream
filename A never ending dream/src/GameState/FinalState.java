package GameState;

import Audio.Sound;
import Entity.Environment.Tent;
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
import java.io.IOException;

import static Input.Keys.anyKeyPress;

public class FinalState extends GameState {
    private Background bush, forest1, forest2, forest3, forest4, sky;
    private TileMap tileMap;
    private Tent tent;
    private Player player;
    private Entity.HUD HUD;

    // events
    private boolean blockInput = false;

    private int tick = 0;

    public FinalState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(16);
        tileMap.loadTiles("/Tilesets/castle.png");//incarcam tileset-ul
        tileMap.loadMap("/Maps/map111.map");//incarcam matricea hartii
        tileMap.setPosition(0, 0);//setam pozitia hartii

        bush = new Background("/Background/PS/02_Bushes.png", 0.7);
        forest1 = new Background("/Background/PS/04_Forest.png", 0.6);
        forest2 = new Background("/Background/PS/06_Forest.png", 0.5);
        forest3 = new Background("/Background/PS/07_Forest.png", 0.4);
        forest4 = new Background("/Background/PS/08_Forest.png", 0.3);
        sky = new Background("/Background/PS/10_Sky.png", 0.1);

        HUD = new HUD(player);

        player = new Player(tileMap);
        player.setPosition(200, 299.9);

        tent = new Tent(tileMap);
        tent.setPosition(121, 253);


    }


    public void update() {
        sectick++;
        handleInput();

        player.update();
        tent.update();

        bush.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest1.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest2.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest3.setPosition(tileMap.getx(), tileMap.gety() + 40);
        forest4.setPosition(tileMap.getx(), tileMap.gety() + 40);
        sky.setPosition(tileMap.getx(), tileMap.gety() + 40);

        if (player.getx() >= 200 && player.gety() <= 300 && PlayerSpeechCount == 28) {
            nextLevel();
            player.stop();
            player.setSpeech(PlayerSpeechCount);
            if (sectick < 240) {
                blockInput = true;
            }
        }

        //mutam pozitia hartii pe ecran in functie de locatia jucatorului
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

    }

    public void draw(Graphics2D g) {

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
        player.draw(g);
        HUD.draw(g);

    }

    //functia pentru receptarea apasarii tastelor
    public void handleInput() {
        if (blockInput || player.getHealth() == 0) return;
        player.setJumping(Keys.keyState[Keys.W]);
        player.setLeft(Keys.keyState[Keys.A]);
        player.setRight(Keys.keyState[Keys.D]);
        if (Keys.isPressed(Keys.SHOOT)) player.setFiring(true);
    }


    private void nextLevel() {
        tick++;

        if (tick == 1) {
            blockInput = true;
            player.stop();
        }
        if (tick > 480) {
            DataBase.getInstance().UpdateState(0);
            Sound.stop("playmusic");
            gsm.setState(GameStateManager.MENUSTATE);
            Sound.loop("menumusic");
            blockInput = true;
        }
    }
}

