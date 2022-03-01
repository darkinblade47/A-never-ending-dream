package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private static int score;
    private final Player player;
    private Color color;
    private BufferedImage heart;
    private BufferedImage life;


    public HUD(Player p) {
        player = p;
        try {
            color = new Color(128, 0, 0);
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/Background/HUD.png"));
            life = image.getSubimage(0, 0, 16, 16);
            heart = image.getSubimage(0, 16, 16, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addScore(int s) {
        score += s;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int s) {
        score = s;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < player.getLives(); i++) {
            g.drawImage(life, 16 + i * 16, 16, null);
        }
        for (int i = 0; i < player.getHealth(); i++) {
            g.drawImage(heart, 16 + i * 16, 32, null);
        }

        g.setColor(color);
        g.drawString("Scor:" + score, 280, 32);
    }

}








