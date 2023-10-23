package gravity.core;

import java.awt.*;

public class Body {
    private double mass;
    private Vector position;
    private Vector velocity;
    private int renderSize;

    public Body(double mass, Vector position) {
        this.mass = mass;
        this.position = position;
        this.velocity = Vector.zero();
        this.renderSize = (int) Math.sqrt(mass) + 1;
    }

    public void applyForce(Vector force, double deltaTime) {
        velocity.add(force.product(deltaTime / mass));
    }

    public void update(double deltaTime) {
        position.add(velocity.product(deltaTime));
    }

    public double getMass() {
        return mass;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void render(Graphics graphics, double avgVel) {
        double speed = velocity.magnitude();
        double r = Math.min(1, speed / avgVel);
        double b = 1.0 - r;
        graphics.setColor(new Color((float) r, 0.0f, (float) b, 1.0f));
        graphics.fillOval((int) position.getX() + 960 - renderSize / 2, (int) position.getY() + 540 - renderSize / 2, renderSize, renderSize);
    }
}
