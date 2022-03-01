package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Girl extends MapObject {
    private static final int[] nr3 = {0, 1, 2};
    private static boolean speaking = false;
    private final Player player;
    private BufferedImage[] buff;
    private BufferedImage[] speeches3;
    private int speech3 = nr3[0];

    public Girl(TileMap tm, Player p) {
        super(tm);
        player = p;

        width = 48;
        height = 48;
        //dimensiunile hitboxului
        cwidth = 24;
        cheight = 32;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/girlidle.png"));
            buff = new BufferedImage[4];//punem fiecare frame al animatiei respective intr-un vector
            for (int i = 0; i < 4; i++)
                buff[i] = sprite.getSubimage(i * width, 0, width, height);
            speeches3 = new BufferedImage[5];
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aK2.png"));
            speeches3[1] = sprite.getSubimage(0, 0, 164, 40);
            sprite = ImageIO.read(getClass().getResourceAsStream("/GoodSpeech/aK6.png"));
            speeches3[2] = sprite.getSubimage(0, 0, 135, 41);
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
        speech3 = nr3[s];
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        facingRight = !(player.getx() < x);
        animation.update();

    }

    public void draw(Graphics2D g) {
        setMapPosition();

        if (speech3 == nr3[1]) {
            g.drawImage(speeches3[1], (int) (x + xmap - cwidth - 100), (int) (y + ymap - 30), null);
        }
        if (speech3 == nr3[2]) {
            g.drawImage(speeches3[2], (int) (x + xmap - cwidth - 75), (int) (y + ymap - 30), null);
        }
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
