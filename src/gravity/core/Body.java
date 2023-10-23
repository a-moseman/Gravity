package gravity.core;

import java.awt.*;

public class Particle {
    private double mass;
    private Vector position;
    private Vector velocity;
    private int renderSize;

    public Particle(double mass, Vector position) {
        this.mass = mass;
        this.position = position;
        this.velocity = Vector.zero();
        this.renderSize = (int) mass / 10 + 1;
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

    public void render(Graphics graphics) {
        graphics.fillOval((int) position.getX() + 320, (int) position.getY() + 320, renderSize, renderSize);
    }
}
