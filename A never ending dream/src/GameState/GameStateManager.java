package GameState;

import Audio.Sound;
import Main.DataBase;
import Main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

import static Audio.Sound.setVolume;

public class GameStateManager {
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public static final int LEVEL2STATE = 2;
    public static final int LEVEL3STATE = 3;
    public static final int LEVEL4STATE = 4;
    public static final int FINALSTATE = 5;
    public static final int SETTINGSSTATE = 6;
    private static int currentState;
    private static GamePanel gamePanel; //referinta catre gamepanel
    private static boolean gameloaded = false;
    private final ArrayList<GameState> gameStates;

    public GameStateManager(GamePanel gamePanel) {
        Sound.init();
        setVolume(0.1f);

        GameStateManager.gamePanel = gamePanel;
        DataBase.getInstance();

        gameStates = new ArrayList<>();

        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));
        gameStates.add(new Level2State(this));
        gameStates.add(new Level3State(this));
        gameStates.add(new Level4State(this));
        gameStates.add(new FinalState(this));
        gameStates.add(new SettingsState(this));

    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }

    public static void setLoad(boolean s) {
        gameloaded = s;
    }

    public static boolean shouldLoad() {
        return gameloaded;
    }

    public void setState(int state)
    //setam stadiul curent al jocului
    {
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update() {

        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }

}
