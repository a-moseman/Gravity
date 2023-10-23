package gravity.core;

import gravity.core.math.Generator;
import gravity.core.math.Vector;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Simulation {
    private final ExecutorService EXECUTOR_SERVICE;
    private final double GRAVITATIONAL_CONSTANT;
    private final Body[] BODIES;
    private final Statistics STATISTICS;
    private final Future<Vector>[] WORKING_FUTURE_FORCES_ARRAY;
    private double lastDeltaTime;

    public Simulation(ExecutorService executorService, double gravitationalConstant, int count) {
        EXECUTOR_SERVICE = executorService;
        GRAVITATIONAL_CONSTANT = gravitationalConstant;
        BODIES = new Body[count];
        STATISTICS = new Statistics();
        WORKING_FUTURE_FORCES_ARRAY = new Future[count];
        initializeBodies();
    }

    private void initializeBodies() {
        for (int i = 0; i < BODIES.length; i++) {
            BODIES[i] = generateRandomBody();
        }
    }

    private Body generateRandomBody() {
        Vector initialPosition = Vector.fromAngle(Generator.randomAngle()).product(Generator.randomDouble(0, 320));
        Vector initialForce = Vector.fromAngle(Generator.randomAngle()).product(Generator.randomDouble(0, 10));
        Body body = new Body(Generator.randomDouble(25, 100), initialPosition);
        body.applyForce(initialForce);
        return body;
    }

    public void update(double deltaTime) {
        lastDeltaTime = deltaTime;
        updateBodyVelocities(deltaTime);
        updateBodyPositions(deltaTime);
    }

    private void updateBodyVelocities(double deltaTime) {
        for (int i = 0; i < BODIES.length; i++) {
            WORKING_FUTURE_FORCES_ARRAY[i] = calculateDeltaGravityForce(i);
        }
        Body a;
        for (int i = 0; i < BODIES.length; i++) {
            a = BODIES[i];
            try {
                a.applyForce(WORKING_FUTURE_FORCES_ARRAY[i].get(), deltaTime);
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBodyPositions(double deltaTime) {
        STATISTICS.reset();
        for (Body body : BODIES) {
            body.update(deltaTime);
            STATISTICS.addToSum(body);
        }
        STATISTICS.calcAvg(BODIES.length);
    }

    private Future<Vector> calculateDeltaGravityForce(int i) {
        return EXECUTOR_SERVICE.submit(() -> {
            Vector accumulatedForce = Vector.zero();
            Body targetBody = BODIES[i];
            Body otherBody;
            Vector direction;
            double gravitationalForce;
            for (int j = 0; j < BODIES.length; j++) {
                if (i == j) {
                    continue;
                }
                otherBody = BODIES[j];
                direction = targetBody.POSITION.direction(otherBody.POSITION);
                gravitationalForce = calculateGravity(targetBody, otherBody);
                accumulatedForce.add(direction.product(gravitationalForce));
            }
            return accumulatedForce;
        });
    }

    private double calculateGravity(Body a, Body b) {
        double r = a.POSITION.distance(b.POSITION);
        double m = a.MASS / 2 + b.MASS / 2;
        r = Math.max(r, m);
        return GRAVITATIONAL_CONSTANT * ((a.MASS * b. MASS) / (r * r));
    }

    public void render(Graphics graphics) {
        for (Body body : BODIES) {
            body.render(graphics, STATISTICS.getAvgVelocity());
        }
        renderDebugInfo(graphics);
    }

    private void renderDebugInfo(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.drawString(String.format("DT: %.4f", lastDeltaTime), 32, 32);
        graphics.drawString(String.format("TPS: %.4f", 1.0 / lastDeltaTime), 32, 64);
        graphics.drawString(String.format("SUM V: %.4f", STATISTICS.getSumVelocity()), 32, 96);
        graphics.drawString(String.format("AVG V: %.4f", STATISTICS.getAvgVelocity()), 32, 128);
    }
}
