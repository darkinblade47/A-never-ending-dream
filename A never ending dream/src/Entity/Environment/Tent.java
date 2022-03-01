package Entity.Environment;

import Entity.Animation;
import Entity.MapObject;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tent extends MapObject {
    private BufferedImage[] tent;

    public Tent(TileMap tm) {
        super(tm);
        facingRight = true;
        cwidth = 133;
        cheight = 80;

        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/Sprites/tent.png"));
            tent = new BufferedImage[1];
            tent[0] = img.getSubimage(0, 0, 133, 80);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(tent);
    }

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        if(x+xmap-width> GamePanel.WIDTH) return;//daca entitatea este in afara ecranului, nu o desenam
        super.draw(g);
    }
}
