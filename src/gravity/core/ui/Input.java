package gravity.core.ui;

import gravity.core.UpdateLoop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    private final UpdateLoop UPDATE_LOOP;

    public Input(UpdateLoop updateLoop) {
        UPDATE_LOOP = updateLoop;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                UPDATE_LOOP.stop();
                System.exit(0);
            }
            case KeyEvent.VK_SPACE -> UPDATE_LOOP.togglePaused();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
