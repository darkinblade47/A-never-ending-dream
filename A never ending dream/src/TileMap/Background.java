package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage image;
    private double x;//pozitia pe axa X a fundalului
    private double y;//pozitia pe axa Y a fundalului
    private double dx;
    private double dy;

    private double moveScale;

    public Background(String s, double ms) {
        try {
            moveScale = ms;
            image = ImageIO.read(getClass().getResourceAsStream(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale / 32) % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
        /*
        x += dx;
        while(x <= -GamePanel.WIDTH) x += GamePanel.WIDTH;
        while(x >= GamePanel.WIDTH) x -= GamePanel.WIDTH;
        y += dy;
        while(y <= -GamePanel.HEIGHT) y += GamePanel.HEIGHT;
        while(y >= GamePanel.HEIGHT) y -= GamePanel.HEIGHT;

         */
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, null);
        if (x < 0) {
            g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
        }
        if (x > 0) {
            g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
        }
        /*if(y < 0) {
            g.drawImage(image, (int)x, (int)y + GamePanel.HEIGHT, null);
        }
        if(y > 0) {
            g.drawImage(image, (int)x, (int)y - GamePanel.HEIGHT, null);
        }
        */
    }
}
