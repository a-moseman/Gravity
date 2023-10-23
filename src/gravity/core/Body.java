package gravity.core;

import gravity.core.math.Vector;

import java.awt.*;

public class Body {
    public final double MASS;
    public final int RENDER_SIZE;
    public final Vector POSITION;
    public final Vector VELOCITY;
    private double speed;

    public Body(double mass, Vector initialPosition) {
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

    public void update(double deltaTime) {
        POSITION.add(VELOCITY.product(deltaTime));
    }

    public double getSpeed() {
        return speed;
    }

    public void render(Graphics graphics, double avgVel) {
        double r = Math.min(1, speed / avgVel);
        double b = 1.0 - r;
        graphics.setColor(new Color((float) r, 0.0f, (float) b, 1.0f));
        graphics.fillOval((int) POSITION.getX() + 960 - RENDER_SIZE / 2, (int) POSITION.getY() + 540 - RENDER_SIZE / 2, RENDER_SIZE, RENDER_SIZE);
    }
}
