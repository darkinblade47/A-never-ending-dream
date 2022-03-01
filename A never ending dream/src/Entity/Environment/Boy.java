package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Boy extends MapObject {
    private static final int[] nr2 = {0, 1, 2};
    private static boolean speaking = false;
    private final Player player;
    private BufferedImage[] buff;
    private BufferedImage[] speeches2;
    private int speech2 = nr2[0];

    public Boy(TileMap tm, Player p) {
        super(tm);
        player = p;

        width = 48;
        height = 48;
        //dimensiunile hitboxului
        cwidth = 24;
        cheight = 32;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/boyidle.png"));
            buff = new BufferedImage[4];//punem fiecare frame al animatiei respective intr-un vector
            for (int i = 0; i < 4; i++)
                buff[i] = sprite.getSubimage(i * width, 0, width, height);

            speeches2 = new BufferedImage[5];
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aK1.png"));
            speeches2[1] = sprite.getSubimage(0, 0, 166, 51);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aK5.png"));
            speeches2[2] = sprite.getSubimage(0, 0, 189, 45);

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

    public void setSpeech(int s) {
        speech2 = nr2[s];
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        facingRight = !(player.getx() < x);//entitatea se uita dupa jucator
        animation.update();

    }

    public void draw(Graphics2D g) {
        setMapPosition();

        if (speech2 == nr2[1]) {
            g.drawImage(speeches2[1], (int) (x + xmap - cwidth - 94), (int) (y + ymap - 35), null);
        }
        if (speech2 == nr2[2]) {
            g.drawImage(speeches2[2], (int) (x + xmap - cwidth - 110), (int) (y + ymap - 30), null);
        }
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
