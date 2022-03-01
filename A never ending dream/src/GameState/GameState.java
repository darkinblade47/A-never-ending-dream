package GameState;

import java.awt.*;

public abstract class GameState {
    public static int sectick = 0;
    public static int PlayerSpeechCount = 1;
    public static int turn;

    protected GameStateManager gsm;

    public abstract void init();

    public abstract void update();

    public abstract void draw(Graphics2D g);

    public abstract void handleInput();

}
