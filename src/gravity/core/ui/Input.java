package gravity.core.ui;

import gravity.core.UpdateLoop;
import gravity.core.math.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    private final UpdateLoop UPDATE_LOOP;
    private final Camera CAMERA;

    public Input(UpdateLoop updateLoop, Camera camera) {
        UPDATE_LOOP = updateLoop;
        CAMERA = camera;
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
            case KeyEvent.VK_UP -> CAMERA.translate(Vector.DOWN);
            case KeyEvent.VK_DOWN -> CAMERA.translate(Vector.UP);
            case KeyEvent.VK_RIGHT -> CAMERA.translate(Vector.RIGHT);
            case KeyEvent.VK_LEFT -> CAMERA.translate(Vector.LEFT);

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
