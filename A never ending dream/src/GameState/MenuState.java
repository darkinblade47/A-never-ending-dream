package GameState;

import Audio.Sound;
import Input.Mouse;
import Main.DataBase;
import TileMap.Background;

import java.awt.*;
import java.sql.ResultSet;

public class MenuState extends GameState {

    private Background bg;
    private Mouse mouse;
    //private boolean leftPressed=false;
    // private boolean rightPressed=false;

    private int currentChoice = 0;
    private String[] options = {"Start", "Help", "Quit"};

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        mouse = new Mouse();
        GameStateManager.getGamePanel().addMouseListener(mouse);

        try {
            bg = new Background("/Background/menu.png", 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
            Sound.load("/SoundEffects/MenuMusic.mp3", "menumusic");
            Sound.loop("menumusic");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

    }

    public void update() {
        bg.update();

        if (mouse.getMouseX() >= 2 * 252 && mouse.getMouseX() <= 2 * 347 && mouse.getMouseY() >= 2 * 160 && mouse.getMouseY() <= 2 * 185 && mouse.isLeftPressed()) {
            //daca apasam pe butonul de Play, incepem un joc nou
            //stergem state-ul din baza de date si il inseram pe cel nou
            DataBase.getInstance().DeleteState();
            DataBase.getInstance().InsertState();
            //ne asiguram ca avem doar un jucator stocat in baza de date, stergem toti inamicii si flag=urile
            DataBase.getInstance().DeletePlayer(1);
            DataBase.getInstance().DeleteAllEnemies();
            DataBase.getInstance().DeleteFlags();

            Sound.stop("menumusic");
            Sound.loop("playmusic");
            gsm.setState(GameStateManager.LEVEL1STATE);
            gsm.getGamePanel().requestFocusInWindow();
        }
        if (mouse.getMouseX() >= 2 * 252 && mouse.getMouseX() <= 2 * 347 && mouse.getMouseY() >= 2 * 205 && mouse.getMouseY() <= 2 * 230 && mouse.isLeftPressed()) {
            //daca apasam pe butonul de Settings, intram in state-ul de setari
            gsm.setState(GameStateManager.SETTINGSSTATE);
            gsm.getGamePanel().requestFocusInWindow();
        }
        if (mouse.getMouseX() >= 2 * 252 && mouse.getMouseX() <= 2 * 347 && mouse.getMouseY() >= 2 * 250 && mouse.getMouseY() <= 2 * 275 && mouse.isLeftPressed()) {
            //daca apasam pe load, cautam sa vedem in ce nivel trebuie sa incarcam jocul
            switch (DataBase.getInstance().LoadState()) {
                case 0: {
                    gsm.setState(GameStateManager.MENUSTATE);
                    gsm.getGamePanel().requestFocusInWindow();
                    break;
                }
                case 1: {
                    GameStateManager.setLoad(true);
                    Sound.stop("menumusic");
                    Sound.loop("playmusic");
                    gsm.setState(GameStateManager.LEVEL1STATE);
                    gsm.getGamePanel().requestFocusInWindow();
                    break;
                }
                case 2: {
                    GameStateManager.setLoad(true);
                    Sound.stop("menumusic");
                    Sound.loop("playmusic");
                    gsm.setState(GameStateManager.LEVEL2STATE);
                    gsm.getGamePanel().requestFocusInWindow();
                    break;
                }
                case 3: {
                    GameStateManager.setLoad(true);
                    Sound.stop("menumusic");
                    Sound.loop("playmusic");
                    gsm.setState(GameStateManager.LEVEL3STATE);
                    gsm.getGamePanel().requestFocusInWindow();
                    break;
                }
                case 4: {
                    GameStateManager.setLoad(true);
                    ResultSet flg = DataBase.getInstance().LoadFlags();
                    if (flg != null) {
                        try {
                            boolean map2 = flg.getBoolean("map2loaded");
                            boolean map3 = flg.getBoolean("map3loaded");
                            if (map2 && !map3) {
                                Sound.stop("menumusic");
                                Sound.loop("bossmusic");
                            } else {
                                Sound.stop("menumusic");
                                Sound.loop("playmusic");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    gsm.setState(GameStateManager.LEVEL4STATE);
                    gsm.getGamePanel().requestFocusInWindow();
                    break;
                }
            }

        }
    }

    public void draw(Graphics2D g) {
        bg.draw(g);

    }

    public void handleInput() {
    }

}
