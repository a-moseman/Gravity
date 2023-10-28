package gravity.core.quadtree;

import gravity.core.Statistics;
import gravity.core.math.Vector;

import java.awt.*;

public class Body {
    protected double posX;
    protected double posY;
    protected double mass;
    protected double velX;
    protected double velY;

    public Body(double posX, double posY, double mass) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.velX = 0;
        this.velY = 0;
    }

    public void applyForce(Vector force, double deltaTime) {
         velX += force.getX() * (deltaTime / mass);
         velY += force.getY() * (deltaTime / mass);
    }

    public Vector getPosition() {
        return new Vector(posX, posY);
    }

    public double getMass() {
        return mass;
    }

    public void render(Graphics graphics) {
        int size = (int) Math.sqrt(mass) + 1;
        graphics.setColor(Color.WHITE);
        graphics.fillOval((int) posX + 960 - size / 2, (int) posY + 540 - size / 2, size, size);
    }
}
