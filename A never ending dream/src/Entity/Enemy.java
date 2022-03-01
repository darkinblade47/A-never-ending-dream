package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected boolean dying;
    protected boolean immune;
    protected int damage;

    public Enemy(TileMap tm) {
        super(tm);
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isDead() {
        return dead;
    }

    public int getDamage() {
        return damage;
    }

    public void hit(int damage) {
        if (dead) return;
        if (!immune) health -= damage;
        if (health < 0) health = 0;
        if (health == 0) dying = true;
    }

    public void update() {
    }

}
