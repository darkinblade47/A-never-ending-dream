package Entity;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int passed = 0;
    private long startTime;     //momentul inceperii animatiei
    private long delay;         //intervalul de timp dintre cadrele animatiei

    private boolean playedOnce; //daca o animatie s-a produs complet

    public Animation() {
        playedOnce = false;
    }

    public void setDelay(long d) {
        delay = d;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public void update() {
        if (delay == -1) return;

        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > delay) {                                      //cand a trecut timpul dintre cadre,
            currentFrame++;                                         //se trece la urmatorul cadru
            passed++;
            startTime = System.nanoTime();                          //se reseteaza momentul inceperii animatiei
        }
        if (currentFrame == frames.length) {                        //daca s-a ajung la ultimul cadru din vector,
            currentFrame = 0;                                       //se reincepe animatia incepand cu primul cadru;
            playedOnce = true;                                      //animatia s-a produs complet;
            passed = 0;
        }

    }

    public int getFrames() {
        return currentFrame;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }

    public int Passed1() {
        return passed;
    }

    public BufferedImage getImage() {
        return frames[currentFrame];
    }

    public boolean PlayedOnce() {
        return playedOnce;
    }

}
