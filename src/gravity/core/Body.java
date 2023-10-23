package gravity.core;

import gravity.core.math.Vector;

import java.awt.*;

public class Body {
    public final double MASS;
    public final int RENDER_SIZE;
    private Vector position;
    private Vector velocity;
    private double speed;

    public Body(double mass, Vector position) {
        MASS = mass;
        RENDER_SIZE = (int) Math.sqrt(mass) + 1;
        this.position = position;
        this.velocity = Vector.zero();
    }

    public void applyForce(Vector force, double deltaTime) {
        velocity.add(force.product(deltaTime / MASS));
        speed = velocity.magnitude();
    }

    public void applyForce(Vector force) {
        applyForce(force, 1);
    }

    public void update(double deltaTime) {
        position.add(velocity.product(deltaTime));
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getSpeed() {
        return speed;
    }

    public void render(Graphics graphics, double avgVel) {
        double r = Math.min(1, speed / avgVel);
        double b = 1.0 - r;
        graphics.setColor(new Color((float) r, 0.0f, (float) b, 1.0f));
        graphics.fillOval((int) position.getX() + 960 - RENDER_SIZE / 2, (int) position.getY() + 540 - RENDER_SIZE / 2, RENDER_SIZE, RENDER_SIZE);
    }
}
