package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {
    private final int tileSize;//dimensiunea in pixeli a unui tile
    private final int numRowsToDraw;//cate linii sa desenam pe ecran
    private final int numColsToDraw;//cate coloane sa desenam pe ecran
    //bounds
    public int xmin;
    //position
    private double x;
    private double y;
    private int ymin;
    private int xmax;
    private int ymax;
    private double tween;//variabila folosita pentru a face camera sa urmareasca mai fluid caracterul
    //map
    private int[][] map;
    private int numRows;//numarul de linii ale hartii
    private int numCols;//numarul de coloane ale gartii
    private int width;//latimea hartii
    private int height;//inaltimea hartii
    //tileset
    private BufferedImage tileset;
    private int numTilesAcross;//numarul de tiles de pe o linie a tileset-ului
    private Tile[][] tiles;//[numarul de linii din tileset][...]
    //drawing
    private int rowOffset;//de la a cata linie sa inceapa desenarea hartii pe ecran pentru a nu desena toata harta si a supraincarca memoria
    private int colOffset;//de la a cata coloana sa inceapa desenarea hartii pe ecran pentru a nu desena toata harta si a supraincarca memoria

    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2; //numarul de linii de desenat pe ecran
        // +2 ca sa nu desenam numarul exact si sa se intample sa avem linii lipsa in unele momente ale jocului

        numColsToDraw = GamePanel.WIDTH / tileSize + 2;   //numarul de linii de desenat pe ecran
        tween = 0.07;
    }

    public void loadTiles(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[26][numTilesAcross];
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++)
                for (int i = 0; i < 4; i++) {
                    subimage = tileset.getSubimage(col * tileSize, i * tileSize, tileSize, tileSize);
                    tiles[i][col] = new Tile(subimage, Tile.NORMAL);//primele 4 linii de tiles nu opresc miscarea caracterului in cazul coliziunilor
                }
            for (int col = 0; col < numTilesAcross; col++)
                for (int i = 4; i < 8; i++) {
                    subimage = tileset.getSubimage(col * tileSize, i * tileSize, tileSize, tileSize);
                    tiles[i][col] = new Tile(subimage, Tile.BLOCKED);//urmatoarele 4 opresc micarea caracterului in cazul coliziunilor
                }
            for (int col = 0; col < numTilesAcross; col++)
                for (int i = 8; i < 12; i++) {
                    subimage = tileset.getSubimage(col * tileSize, i * tileSize, tileSize, tileSize);
                    tiles[i][col] = new Tile(subimage, Tile.NORMAL);//nu opresc
                }
            for (int col = 0; col < numTilesAcross; col++)
                for (int i = 12; i < 16; i++) {
                    subimage = tileset.getSubimage(col * tileSize, i * tileSize, tileSize, tileSize);
                    tiles[i][col] = new Tile(subimage, Tile.BLOCKED);//opresc
                }
            for (int col = 0; col < numTilesAcross; col++)
                for (int i = 16; i < 25; i++) {
                    subimage = tileset.getSubimage(col * tileSize, i * tileSize, tileSize, tileSize);
                    tiles[i][col] = new Tile(subimage, Tile.NORMAL);//nuopresc
                }
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize, 25 * tileSize, tileSize, tileSize);
                tiles[25][col] = new Tile(subimage, Tile.BLOCKED);//opresc
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String s) {
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;

            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split("\\s+");

                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);  //convertesc fiecare ID String in ID Int
                    if (map[row][col] > 0)                          //verific daca ID-ul matricii este diferit de 0;
                        map[row][col] -= 1;                         //probleme de tile ID in matrice, o rezolv scazand 1 din ID-ul respectiv,
                    //numai ID-ul 0 nu are probleme, de asta verific sa nu fie 0
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getx() {
        return (int) x;
    }

    public int gety() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getxmin() {
        return xmin;
    }

    public int getHeight() {
        return height;
    }

    public int getType(int row, int col) {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();//returnez tipul unui tile: BLOCKED/NORMAL;
    }

    public void setPosition(double x, double y) {
        //actualizam pozitia hartii in functie de pozitia jucatorului si de variabila tween
        //care fluidizeaza aceasta "miscare"
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;

        fixBounds();

        colOffset = (int) -this.x / tileSize;
        rowOffset = (int) -this.y / tileSize;
    }

    public void fixBounds() {
        if (x < xmin)
            x = xmin;
        if (y < ymin)
            y = ymin;
        if (x > xmax)
            x = xmax;
        if (y > ymax)
            y = ymax;
    }

    public void draw(Graphics2D g) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {     //desenam pe ecran randurile din harta de la
            //rowOffset pana la rowOffset + numRowsToDraw;
            if (row >= numRows)
                break;
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) { //desenam pe ecran coloanele din harta de la
                //colOffset pana la colOffset + numColsToDraw
                if (col >= numCols)
                    break;
                if (map[row][col] == 0)
                    continue;
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
                //if(tiles[r][c].getType()==Tile.BLOCKED)
                //     g.drawRect((int)x+col*tileSize,(int)y+row*tileSize,tileSize,tileSize);

            }
        }
    }

    public void setTween(double tween) {
        this.tween = tween;
    }
}
