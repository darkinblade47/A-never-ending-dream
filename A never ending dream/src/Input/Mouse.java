package Input;

import Audio.Sound;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    private boolean leftPressed;
    private int mouseX;
    private int mouseY;

    public Mouse() {
        Sound.load("/SoundEffects/click.mp3", "click");
        leftPressed = false;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }


    public void mousePressed(MouseEvent e) {
        mouseMoved(e);
        if (e.getButton() == MouseEvent.BUTTON1)//butonul din stanga
        {
            leftPressed = true;
            Sound.play("click");
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)//butonul din stanga
            leftPressed = false;
    }

    public void mouseClicked(MouseEvent e) {
    }


    public void mouseDragged(MouseEvent e) {
    }


    public void mouseExited(MouseEvent e) {
    }


    public void mouseEntered(MouseEvent e) {
    }

}