package Main;

import GameState.GameStateManager;
import Input.Keys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //dimensiunile ferestrei de joc
    public static final int WIDTH = 595;
    public static final int HEIGHT = 335;
    public static final int SCALE = 2;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;
    //thread
    private Thread thread;
    private boolean running;//daca jocul ruleaza
    //image
    private BufferedImage image;
    private Graphics2D g;

    //game state manager
    private GameStateManager gsm;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();//fereastra primeste focus si permite primirea de key events
    }

    public void addNotify() {
        super.addNotify();
        //daca nu avem un thread, facem unul
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;

        gsm = new GameStateManager(this);
    }

    public void run() {
        init();
        long start;
        long elapsed;
        long wait;
        //Game loop
        while (running) {
            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if (wait < 0)
                wait = 0;//nu are niciun impact, doar rezolv o exceptie

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void update() {
        gsm.update();
    }

    private void draw() {

        gsm.draw(g);
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }


    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        Keys.keySet(key.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent key) {
        Keys.keySet(key.getKeyCode(), false);
    }


}
