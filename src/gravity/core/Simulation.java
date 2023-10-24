package gravity.core;

import gravity.core.math.Generator;
import gravity.core.math.Physics;
import gravity.core.math.Vector;
import gravity.core.quadtree.Attractor;
import gravity.core.quadtree.Quadtree;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Simulation implements Updatable, Renderable {
    private final ExecutorService EXECUTOR_SERVICE;
    private final Body[] BODIES;
    private final Future<Vector>[] WORKING_FUTURE_FORCES_ARRAY;
    private final Quadtree QUADTREE;
    private double lastDeltaTime;

    public Simulation(ExecutorService executorService, int count) {
        EXECUTOR_SERVICE = executorService;
        BODIES = new Body[count];
        WORKING_FUTURE_FORCES_ARRAY = new Future[count];
        QUADTREE = new Quadtree(-2000, 2000, -2000, 2000);
        initializeBodies();
    }

    private void initializeBodies() {
        for (int i = 0; i < BODIES.length; i++) {
            BODIES[i] = generateRandomBody();
            QUADTREE.add(new Attractor(BODIES[i].POSITION.getX(), BODIES[i].POSITION.getY(), BODIES[i].MASS));
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
            Attractor attractor = new Attractor(body.POSITION.getX(), body.POSITION.getY(), body.MASS);
            QUADTREE.move(attractor, body.VELOCITY.getX() * deltaTime, body.VELOCITY.getY() * deltaTime);
            body.update(deltaTime);
            Statistics.addToSum(body);
        }
        Statistics.calcAvg(BODIES.length);
    }

    private Future<Vector> calculateDeltaGravityForce(int i) {
        return EXECUTOR_SERVICE.submit(() -> {
            Vector accumulatedForce = Vector.zero();
            Body targetBody = BODIES[i];
            Attractor targetAttractor = new Attractor(targetBody.POSITION.getX(), targetBody.POSITION.getY(), targetBody.MASS);
            List<Attractor> attractors = QUADTREE.attractors(targetAttractor);
            for (Attractor attractor : attractors) {
                Vector otherPosition = new Vector(attractor.getX(), attractor.getY());
                Vector direction = targetBody.POSITION.direction(otherPosition);
                double gravitationalForce = Physics.gravityForceBetweenBodies(targetBody.POSITION, otherPosition, targetBody.MASS, attractor.getMass());
                accumulatedForce.add(direction.product(gravitationalForce));
            }
            return accumulatedForce;

        });
    }

    @Override
    public void render(Graphics graphics) {
        for (Body body : BODIES) {
            body.render(graphics);
        }
    }

    public double getLastDeltaTime() {
        return lastDeltaTime;
    }
}
