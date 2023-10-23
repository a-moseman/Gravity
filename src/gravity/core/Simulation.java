package gravity.core;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Simulation {
    private final SecureRandom RANDOM;
    private final double G;
    private Body[] bodies;
    private double lastDeltaTime;
    private ExecutorService executorService;

    public Simulation(double g, int count, ExecutorService executorService) {
        RANDOM = new SecureRandom();
        G = g;
        bodies = new Body[count];
        initialize();
        this.executorService = executorService;
    }

    private void initialize() {
        double r, d, m, r2, d2;
        Vector p;
        for (int i = 0; i < bodies.length; i++) {
            r = RANDOM.nextDouble() * Math.PI * 2;
            d = RANDOM.nextDouble() * 320;
            p = new Vector(Math.cos(r), Math.sin(r)).product(d);
            m = RANDOM.nextDouble() * 100 + 25;
            bodies[i] = new Body(m, p);

            r2 = RANDOM.nextDouble() * Math.PI * 2;
            d2 = RANDOM.nextDouble() * 10;
            bodies[i].applyForce(new Vector(Math.cos(r2), Math.sin(r2)).product(d2), 1);
        }
    }

    public void update(double deltaTime) {
        lastDeltaTime = deltaTime;
        List<Future<Vector>> forces = new ArrayList<>();
        for (int i = 0; i < bodies.length; i++) {
            forces.add(calculateDeltaGravityForce(i));
        }
        Body a;
        for (int i = 0; i < bodies.length; i++) {
            a = bodies[i];
            try {
                a.applyForce(forces.get(i).get(), deltaTime);
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            a.update(deltaTime);
        }
    }

    private Future<Vector> calculateDeltaGravityForce(int i) {
        return executorService.submit(() -> {
            Vector force = Vector.zero();
            Body a = bodies[i];
            Body b;
            Vector direction;
            double g;
            for (int j = 0; j < bodies.length; j++) {
                if (i == j) {
                    continue;
                }
                b = bodies[j];
                direction = a.getPosition().direction(b.getPosition());
                g = calculateGravity(a, b);
                force.add(direction.product(g));
            }
            return force;
        });
    }

    private double calculateGravity(Body a, Body b) {
        double r = a.getPosition().distance(b.getPosition());
        double m = a.getMass() / 2 + b.getMass() / 2;
        r = Math.max(r, m);
        return G * ((a.getMass() * b.getMass()) / (r * r));
    }

    public void render(Graphics graphics) {
        double avgVel = 0;
        for (Body body : bodies) {
            avgVel += body.getVelocity().magnitude();
        }
        avgVel /= bodies.length;

        for (int i = 0; i < bodies.length; i++) {
            bodies[i].render(graphics, avgVel);
        }
        renderDebugInfo(graphics);
    }

    private void renderDebugInfo(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.drawString(String.format("DT: %.4f", lastDeltaTime), 32, 32);
        graphics.drawString(String.format("TPS: %.4f", 1.0 / lastDeltaTime), 32, 64);
        double sumVelMagn = 0;
        for (Body body : bodies) {
            sumVelMagn += body.getVelocity().magnitude();
        }
        graphics.drawString(String.format("V: %.4f", sumVelMagn), 32, 96);
    }
}
