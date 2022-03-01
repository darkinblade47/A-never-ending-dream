package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Horse extends MapObject {

    private BufferedImage[] buff;

    public Horse(TileMap tm) {
        super(tm);
        facingRight = true;

        cwidth = 60;
        cheight = 40;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/horseidle.png"));
            buff = new BufferedImage[7];//punem fiecare frame al animatiei respective intr-un vector
            for (int i = 0; i < 7; i++)
                buff[i] = sprite.getSubimage(i * 82, 0, 82, 66);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(buff);
        animation.setDelay(200);
    }

    public void update() {
        checkTileMapCollision();
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
