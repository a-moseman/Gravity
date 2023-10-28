package gravity.core;

import gravity.core.math.Generator;
import gravity.core.math.Physics;
import gravity.core.math.Vector;
import gravity.core.quadtree.Body;
import gravity.core.quadtree.Quadtree;
import gravity.core.quadtree.VirtualBody;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Simulation implements Updatable, Renderable {
    private final ExecutorService EXECUTOR_SERVICE;
    private final Future<Vector>[] WORKING_FUTURE_FORCES_ARRAY;
    private final Quadtree QUADTREE;
    private double lastDeltaTime;

    public Simulation(ExecutorService executorService, int count) {
        EXECUTOR_SERVICE = executorService;
        WORKING_FUTURE_FORCES_ARRAY = new Future[count];
        QUADTREE = new Quadtree(-2000, 2000, -2000, 2000);
        initializeBodies(count);
    }

    private void initializeBodies(int count) {
        for (int i = 0; i < count; i++) {
            Body body = generateRandomBody();
            QUADTREE.add(body);
        }
    }

    private Body generateRandomBody() {
        Vector initialPosition = Vector.fromAngle(Generator.randomAngle()).product(Generator.randomDouble(0, 320));
        Vector initialForce = Vector.fromAngle(Generator.randomAngle()).product(Generator.randomDouble(0, 10));
        Body body = new Body(initialPosition.getX(), initialPosition.getY(), Generator.randomDouble(25, 100));
        body.applyForce(initialForce, 1);
        return body;
    }

    @Override
    public void update(double deltaTime) {
        lastDeltaTime = deltaTime;
        updateBodyVelocities(deltaTime);
        QUADTREE.update(deltaTime);
    }

    private void updateBodyVelocities(double deltaTime) {
        for (int i = 0; i < QUADTREE.size(); i++) {
            WORKING_FUTURE_FORCES_ARRAY[i] = calculateDeltaGravityForce(i);
        }
        Body a;
        for (int i = 0; i < QUADTREE.size(); i++) {
            a = QUADTREE.get(i);
            try {
                a.applyForce(WORKING_FUTURE_FORCES_ARRAY[i].get(), deltaTime);
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private Future<Vector> calculateDeltaGravityForce(int i) {
        return EXECUTOR_SERVICE.submit(() -> {
            Vector accumulatedForce = Vector.zero();
            Body targetBody = QUADTREE.get(i);
            Vector targetBodyPosition = targetBody.getPosition();
            List<VirtualBody> attractors = QUADTREE.attractors(targetBody);
            for (VirtualBody attractor : attractors) {
                Vector attractorPosition = attractor.getPosition();
                Vector direction = targetBodyPosition.direction(attractorPosition);
                double gravitationalForce = Physics.gravityForceBetweenBodies(targetBodyPosition, attractorPosition, targetBody.getMass(), attractor.getMass());
                accumulatedForce.add(direction.product(gravitationalForce));
            }
            return accumulatedForce;
        });
    }

    @Override
    public void render(Graphics graphics) {
        for (int i = 0; i < QUADTREE.size(); i++) {
            QUADTREE.get(i).render(graphics);
        }
    }

    public double getLastDeltaTime() {
        return lastDeltaTime;
    }
}
