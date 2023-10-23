package gravity.core.ui;

import gravity.core.math.Vector;

public class Camera {
    private Vector position;

    public Camera() {
        position = Vector.zero();
    }

    public void translate(Vector translation) {
        position.add(translation);
    }

    public Vector getPosition() {
        return position;
    }
}
