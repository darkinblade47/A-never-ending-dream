package Main;


import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame("A never ending dream");
        window.getContentPane().add(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        window.setFocusable(true);

        if (window.isVisible())
            window.requestFocusInWindow();

    }
}
