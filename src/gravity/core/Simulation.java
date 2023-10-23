package gravity.core;

import gravity.core.math.Generator;
import gravity.core.math.Physics;
import gravity.core.math.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Simulation implements Updatable, Renderable {
    private final ExecutorService EXECUTOR_SERVICE;
    private final Body[] BODIES;
    private final Future<Vector>[] WORKING_FUTURE_FORCES_ARRAY;
    private double lastDeltaTime;

    public Simulation(ExecutorService executorService, int count) {
        EXECUTOR_SERVICE = executorService;
        BODIES = new Body[count];
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

    @Override
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
        Statistics.reset();
        for (Body body : BODIES) {
            body.update(deltaTime);
            Statistics.addToSum(body);
        }
        Statistics.calcAvg(BODIES.length);
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
                gravitationalForce = Physics.gravityForceBetweenBodies(targetBody.POSITION, otherBody.POSITION, targetBody.MASS, otherBody.MASS);
                accumulatedForce.add(direction.product(gravitationalForce));
            }
            return accumulatedForce;
        });
    }

    @Override
    public void render(Graphics graphics) {
        BufferedImage screenBuffer = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        Graphics bufferGraphics = screenBuffer.getGraphics();
        for (Body body : BODIES) {
            body.render(bufferGraphics);
        }
        graphics.drawImage(screenBuffer, 0, 0, null);
        bufferGraphics.dispose();

        renderDebugInfo(graphics);
    }

    private void renderDebugInfo(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.drawString(String.format("DT: %.4f", lastDeltaTime), 32, 32);
        graphics.drawString(String.format("TPS: %.4f", 1.0 / lastDeltaTime), 32, 64);
        graphics.drawString(String.format("SUM V: %.4f", Statistics.getSumVelocity()), 32, 96);
        graphics.drawString(String.format("AVG V: %.4f", Statistics.getAvgVelocity()), 32, 128);
    }
}
