import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final double G;
    private List<Entity> entities;

    public Simulation(double g) {
        G = g;
        entities = new ArrayList<>();
    }

    public void initialize(int count) {
        double r, d, m;
        Vector p;
        for (int i = 0; i < count; i++) {
            r = Math.random() * Math.PI * 2;
            d = Math.random() * 320;
            p = new Vector(Math.cos(r), Math.sin(r)).product(d);
            m = Math.random() * 50 + 50;
            entities.add(new Entity(m, p));
        }
    }

    public void update(double deltaTime) {
        Entity a;
        for (int i = 0; i < entities.size(); i++) {
            Vector force = calculateDeltaGravityForce(i);
            a = entities.get(i);
            a.applyForce(force);
            a.update(deltaTime);
        }
    }

    private Vector calculateDeltaGravityForce(int i) {
        Vector force = Vector.ZERO;
        Entity a = entities.get(i);
        for (int j = 0; j < entities.size(); j++) {
            if (i == j) {
                continue;
            }
            Entity b = entities.get(j);

            Vector direction = a.getPosition().direction(b.getPosition());
            double g = calculateGravity(a, b);
            force.add(direction.product(g));
        }
        return force;
    }

    private double calculateGravity(Entity a, Entity b) {
        double r = a.getPosition().distance(b.getPosition());
        return G * ((a.getMass() * b.getMass()) / (r * r));
    }

    public void render(Graphics graphics) {
        entities.forEach((entity) -> entity.render(graphics));
    }
}
