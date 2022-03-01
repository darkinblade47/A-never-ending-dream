package Entity;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;

//clasa abstracta din care fiecare entitate ii va mosteni proprietatile;
public abstract class MapObject {

    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    //dimensiunile frame-ului;
    protected int width;
    protected int height;
    //dimensiunile hitboxului;
    protected int cwidth;
    protected int cheight;
    //proprietatile hartii;
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;//daca coltul din stanga sus se loveste de ceva;
    protected boolean topRight;//daca coltul din dreapta sus se loveste de ceva;
    protected boolean bottomLeft;//daca coltul din stanga jos se loveste de ceva;
    protected boolean bottomRight;//daca coltul din dreapta jos se loveste de ceva;

    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;

    //variabile boolean pentru miscare;
    protected boolean left;
    protected boolean right;
    protected boolean jumping;
    protected boolean falling;
    protected boolean firing;
    //vitezele entitatii;
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    //ce creeaza un nou dreptunghi de coliziune;
    public Rectangle getRectangle() {
        return new Rectangle((int) x - cwidth / 2, (int) y - cheight / 2, cwidth, cheight);
    }

    public void calculateCorners(double x, double y) {
        int leftTile = ((int) x - cwidth / 2) / tileSize;           //coloana din stanga lui currCol;
        int rightTile = ((int) x + cwidth / 2 - 1) / tileSize;      //coloana din dreapta lui currCol;
        int topTile = ((int) y - cheight / 2) / tileSize;           //randul de deasupra lui currRow;
        int bottomTile = ((int) y + cheight / 2 - 1) / tileSize;    //randul de sub currRow;

        //in cazul in care caracterul iese de pe harta, sa nu primesc o exceptie
        if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
            topLeft = topRight = bottomLeft = bottomRight = false;
            return;
        }

        int tl = tileMap.getType(topTile, leftTile);                //ia valoarea 0/1 daca tile-ul din stanga sus este BLOCKED/NORMAL;
        int tr = tileMap.getType(topTile, rightTile);               //ia valoarea 0/1 daca tile-ul din dreapta sus este BLOCKED/NORMAL;
        int bl = tileMap.getType(bottomTile, leftTile);             //ia valoarea 0/1 daca tile-ul din stanga jos este BLOCKED/NORMAL;
        int br = tileMap.getType(bottomTile, rightTile);            //ia valoarea 0/1 daca tile-ul din dreapta jos este BLOCKED/NORMAL;

        topLeft = tl == Tile.BLOCKED;                               //converteste pe tl din int in boolean;
        //if(tl==tile.BLOCKED) topLeft=true; else topLeft=false;
        topRight = tr == Tile.BLOCKED;                              //converteste pe tr din int in boolean;
        bottomLeft = bl == Tile.BLOCKED;                            //converteste pe bl din int in boolean;
        bottomRight = br == Tile.BLOCKED;                           //converteste pe br din int in boolean;

    }

    public void checkTileMapCollision() {
        currCol = (int) x / tileSize;
        currRow = (int) y / tileSize;
        //destinatia entitatii este deplasarea adunata la coordonata acesteia;
        xdest = x + dx;
        ydest = y + dy;
        //variabile temporare;
        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);                                     //calculam coliziunile in colturile de sus si jos(ydest);
        if (dy < 0) {                                                   //daca entitatea se deplaseaza in sus,
            if (topLeft || topRight) {                                  //verificam daca unul din colturile superioare se loveste de ceva;
                dy = 0.000001;                                          //setam deplasarea pe ceva extrem de mic, pentru ca daca o setez pe 0,
                // pe perioada in care sta cu dy=0,entitatea intra in IDLE si se vede dubios;
                ytemp = (currRow - 1) * tileSize + cheight / 2;         //punem pozitia entitatii fix sub tile-ul de care s-a lovit;
            } else ytemp += dy;                                         //altfel, se continua deplasarea in sus;
        }

        if (dy > 0) {                                               //daca entitatea se deplaseaza in jos,
            if (bottomLeft || bottomRight) {                        //verificam daca unul din colturile inferioare se loveste de ceva;
                dy = 0;                                             //oprim deplasarea,
                falling = false;                                    //entitatea nu mai este in stadiul de cadere;
                ytemp = (currRow + 2) * tileSize - cheight / 2;     //punem pozitia entitatii fix deasupra tile-ului de care s-a lovit;
            } else ytemp += dy;                                     //altfel, se continua deplasarea in jos;

        }

        calculateCorners(xdest, y);                                 //calculam coliziunile in colturile laterale(ydest);
        if (dx < 0) {                                               //daca entitatea se deplaseaza spre stanga,
            if (topLeft || bottomLeft) {                            //verificam daca unul din colturile din stanga se loveste de ceva;
                dx = 0;                                             //oprim deplasarea;
                xtemp = currCol * tileSize + cwidth / 2;            //punem pozitia entitatii fix in dreapta tile-ului de care s-a lovit;
            } else xtemp += dx;                                     //altfel, se continua deplasarea spre stanga;


        }

        if (dx > 0) {                                               //daca entitatea se deplaseaza spre dreapta,
            if (topRight || bottomRight) {                          //verificam daca unul din colturile din dreapta se loveste de ceva;
                dx = 0;                                             //oprim deplasarea;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;      //punem pozitia entitatii fix in stanga tile-ului de care s-a lovit;
            } else xtemp += dx;                                     //altfel, se continua deplasarea spre dreapta;

        }

        if (!falling) {                                             //daca entitatea nu este in cadere,
            calculateCorners(x, ydest + 1);                       //calculam coliziunile cu randul sub care se afla entitatea;
            if (!bottomLeft && !bottomRight) {                      //daca sub entitate nu exista nimic care sa produca coliziuni,
                falling = true;                                     //atunci aceasta intra in stadiun de cadere;
            }
        }

    }

    public boolean getFacing() {
        return facingRight;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCWidth() {
        return cwidth;
    }

    public int getCHeight() {
        return cheight;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition() {
        //setam coordonatele hitboxurilor hartii
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    //metode care seteaza stadiul de miscare al entitatii
    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setJumping(boolean b) {
        jumping = b;
    }

    public void setFiring(boolean b) {
        firing = b;
    }

    public void draw(Graphics2D g) {
        if (facingRight) {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        } else {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
        }
        //cutia de coliziuni
        //Rectangle r = getRectangle();r.x += xmap;r.y += ymap;g.draw(r);
    }


}
