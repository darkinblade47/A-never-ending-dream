package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Ciorescu extends MapObject {

    private static final int[] nr5 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static boolean speaking = false;
    private BufferedImage[] buff;
    private BufferedImage[] speeches5;
    private int speech5 = nr5[0];

    public Ciorescu(TileMap tm) {
        super(tm);
        facingRight = false;
        width = 32;
        height = 32;

        cwidth = 24;
        cheight = 24;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/ciorescu.png"));
            buff = new BufferedImage[6];//punem fiecare frame al animatiei respective intr-un vector
            for (int i = 0; i < 6; i++)
                buff[i] = sprite.getSubimage(i * width, 0, width, height);

            speeches5 = new BufferedImage[3];
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aC2.png"));
            speeches5[1] = sprite.getSubimage(0, 0, 160, 45);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aC3.png"));
            speeches5[2] = sprite.getSubimage(0, 0, 115, 39);

        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(buff);
        animation.setDelay(65);
    }

    public static boolean isSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean s) {
        speaking = s;
    }

    public void setSpeech(int s) {
        speech5 = nr5[s];
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        animation.update();

    }

    public void draw(Graphics2D g) {
        setMapPosition();

        if (speech5 == nr5[1]) {
            g.drawImage(speeches5[1], (int) (x + xmap - cwidth - 93), (int) (y + ymap - 43), null);
        }
        if (speech5 == nr5[2]) {
            g.drawImage(speeches5[2], (int) (x + xmap - cwidth - 64), (int) (y + ymap - 31), null);
        }
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
