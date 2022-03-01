package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Man extends MapObject {
    private static final int[] nr1 = {0, 1, 2, 3, 4};
    private static boolean speaking = false;
    private final Player player;
    private BufferedImage[] buff;
    private BufferedImage[] speeches1;
    private int speech1 = nr1[0];

    public Man(TileMap tm, Player p) {
        super(tm);
        player = p;

        width = 48;
        height = 48;
        //dimensiunile hitboxului
        cwidth = 24;
        cheight = 32;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/manidle.png"));
            buff = new BufferedImage[4];//punem fiecare frame al animatiei respective intr-un vector
            for (int i = 0; i < 4; i++)
                buff[i] = sprite.getSubimage(i * width, 0, width, height);

            speeches1 = new BufferedImage[5];
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aM2.png"));
            speeches1[1] = sprite.getSubimage(0, 0, 174, 39);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aM4.png"));
            speeches1[2] = sprite.getSubimage(0, 0, 160, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aM5.png"));
            speeches1[3] = sprite.getSubimage(0, 0, 157, 46);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aM7.png"));
            speeches1[4] = sprite.getSubimage(0, 0, 114, 37);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(buff);
        animation.setDelay(200);
    }

    public static boolean isSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean s) {
        speaking = s;
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        facingRight = !(player.getx() < x);
        animation.update();

    }

    public void setSpeech(int s) {
        speech1 = nr1[s];
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        if (speech1 == nr1[1]) {
            g.drawImage(speeches1[1], (int) (x + xmap - cwidth - 32), (int) (y + ymap - 45), null);
        }
        if (speech1 == nr1[2]) {
            g.drawImage(speeches1[2], (int) (x + xmap - cwidth - 24), (int) (y + ymap - 45), null);
        }
        if (speech1 == nr1[3]) {
            g.drawImage(speeches1[3], (int) (x + xmap - cwidth - 22), (int) (y + ymap - 45), null);
        }
        if (speech1 == nr1[4]) {
            g.drawImage(speeches1[4], (int) (x + xmap - cwidth - 13), (int) (y + ymap - 38), null);
        }
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
