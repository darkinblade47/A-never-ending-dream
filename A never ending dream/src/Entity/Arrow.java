package Entity;

import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Arrow extends MapObject {

    private boolean hit;//sageata a lovit ceva
    private boolean remove;//inlaturam sageata
    private BufferedImage[] sprite;

    public Arrow(TileMap tm, boolean right) {
        super(tm);
        facingRight = right;
        moveSpeed = 4.8;                //viteza proiectilului
        if (right)
            dx = moveSpeed;
        else
            dx = -moveSpeed;
        //dimensiunile frame-ului sagetii
        width = 33;
        height = 33;
        //dimensiunile hitboxului sagetii
        cwidth = 19;
        cheight = 2;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/arrowsprite.png"));
            sprite = new BufferedImage[6];
            for (int i = 0; i < sprite.length; i++)
                sprite[i] = spritesheet.getSubimage(i * width, 0, width, height);

            animation = new Animation();
            animation.setFrames(sprite);
            animation.setDelay(60);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setHit() {   //daca sageata a nimerit ceva
        if (hit) return;
        hit = true;
        dx = 0;

    }

    public boolean Remove() {
        return remove;
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        animation.update();

        if (dx == 0 && !hit)
            setHit();
        if (hit)
            remove = true;
        //daca sageata este in afara ecranului, aceasta este distrusa
        if(x+xmap-cwidth/2> GamePanel.WIDTH)
            remove=true;

    }

    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
    }
}
