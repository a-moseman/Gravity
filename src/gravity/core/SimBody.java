package gravity.core;

import gravity.core.math.Vector;

import java.awt.*;

public class SimBody implements Updatable {
    public final double MASS;
    public final int RENDER_SIZE;
    public final Vector POSITION;
    public final Vector VELOCITY;
    private double speed;

    public SimBody(double mass, Vector initialPosition) {
        MASS = mass;
        RENDER_SIZE = (int) Math.sqrt(mass) + 1;
        POSITION = initialPosition;
        VELOCITY = Vector.zero();
    }

    public void applyForce(Vector force, double deltaTime) {
        VELOCITY.add(force.product(deltaTime / MASS));
        speed = VELOCITY.magnitude();
    }

    public void applyForce(Vector force) {
        applyForce(force, 1);
    }

    @Override
    public void update(double deltaTime) {
        POSITION.add(VELOCITY.product(deltaTime));
    }

    public double getSpeed() {
        return speed;
    }
}
