package GameState;


import Audio.Sound;
import Input.Mouse;
import TileMap.Background;

import java.awt.*;

import static Audio.Sound.setVolume;

public class SettingsState extends GameState {

    private final Mouse mouse;
    private Background bg;
    private float volume;
    private Color color;
    private Font font;

    public SettingsState(GameStateManager gsm) {
        this.gsm = gsm;
        mouse = new Mouse();
        GameStateManager.getGamePanel().addMouseListener(mouse);

        try {
            bg = new Background("/Background/settings.png", 0);
            color = new Color(128, 128, 128);
            font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        volume=0.1f;
    }

    public void init() {

    }

    public void update() {
        bg.update();
        if (mouse.getMouseX() >= 2 * 238 && mouse.getMouseX() <= 2 * 247 && mouse.getMouseY() >= 2 * 101 && mouse.getMouseY() <= 2 * 111 && mouse.isLeftPressed()) {
            //daca apasam pe +, crestem volumul jocului
            if (volume <= 1) {
                volume += 0.01f;
                setVolume(volume);
            } else setVolume(1);
        }

        if (mouse.getMouseX() >= 2 * 365 && mouse.getMouseX() <= 2 * 373 && mouse.getMouseY() >= 2 * 101 && mouse.getMouseY() <= 2 * 111 && mouse.isLeftPressed()) {
            //daca apasam pe -, scadem volumul jocului
            if (volume > 0f) {
                volume -= 0.01f;
                setVolume(volume);
            } else setVolume(0);
        }
        if (mouse.getMouseX() >= 2 * 272 && mouse.getMouseX() <= 2 * 327 && mouse.getMouseY() >= 2 * 297 && mouse.getMouseY() <= 2 * 320 && mouse.isLeftPressed()) {
            //revenim in meniu prin apasarea butonului back
            gsm.setState(GameStateManager.MENUSTATE);
            GameStateManager.getGamePanel().requestFocusInWindow();
        }
    }


    public void draw(Graphics2D g) {
        bg.draw(g);
        g.setFont(font);
        g.setColor(color);
        g.drawString(String.valueOf((int) (volume * 100)), 306, 109);//afisam volumul sub forma 0-100
    }
    public void handleInput() {

    }
}