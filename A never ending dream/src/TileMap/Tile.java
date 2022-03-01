package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
    //tile types
    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;
    private final BufferedImage image;
    private final int type;

    public Tile(BufferedImage image, int type) {
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getType() {
        return type;
    }
}
