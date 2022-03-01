package Input;

import java.awt.event.KeyEvent;

public class Keys {

    public static final int nr_keys = 5;

    public static boolean[] keyState = new boolean[nr_keys];
    public static boolean[] prevKeyState = new boolean[nr_keys];

    public static int W = 0;
    public static int A = 1;
    public static int D = 2;
    public static int SHOOT = 3;
    public static int ENTER = 4;

    public static void keySet(int i, boolean b) {
        if (i == KeyEvent.VK_W) keyState[W] = b;
        else if (i == KeyEvent.VK_A) keyState[A] = b;
        else if (i == KeyEvent.VK_D) keyState[D] = b;
        else if (i == KeyEvent.VK_SPACE) keyState[SHOOT] = b;
        else if (i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
    }

    public static void update() {
        for (int i = 0; i < nr_keys; i++) {
            prevKeyState[i] = keyState[i];
        }
    }

    public static boolean isPressed(int i) {
        return keyState[i] && !prevKeyState[i];
    }

    public static boolean anyKeyPress() {
        for (int i = 0; i < nr_keys; i++) {
            if (keyState[i]) return true;
        }
        return false;
    }
}
