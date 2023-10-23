package gravity.core;

import javax.swing.*;

public class UpdateLoop implements Runnable {
    private static final long NANOS_PER_SECOND = 1_000_000_000;
    private final double TARGET_TPS;
    private final long TARGET_TICK_TIME;
    private final JFrame FRAME;
    private final Simulation SIMULATION;
    private boolean running;

    public UpdateLoop(double targetTPS, JFrame frame, Simulation simulation) {
        TARGET_TPS = targetTPS;
        TARGET_TICK_TIME = (long) (NANOS_PER_SECOND / TARGET_TPS);
        FRAME = frame;
        SIMULATION = simulation;
    }

    @Override
    public void run() {
        running = true;
        long tickStartTime;
        long tickComputeTime;
        double deltaTime = 1.0 / TARGET_TPS;
        while (running) {
            tickStartTime = System.nanoTime();
            SIMULATION.update(deltaTime);
            FRAME.repaint();
            tickComputeTime = System.nanoTime() - tickStartTime;
            while ((System.nanoTime() - tickStartTime) < TARGET_TICK_TIME) {

            }
            deltaTime = (double) Math.max(tickComputeTime, TARGET_TICK_TIME) / NANOS_PER_SECOND;
        }
    }

    public void stop() {
        running = false;
    }
}
