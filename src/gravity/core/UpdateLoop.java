package gravity.core;

import javax.swing.*;

public class UpdateLoop implements Runnable {
    private static final long NANOS_PER_SECOND = 1_000_000_000;
    private final double TARGET_TPS;
    private final JFrame FRAME;
    private final Simulation SIMULATION;
    private boolean running;

    public UpdateLoop(double targetTPS, JFrame frame, Simulation simulation) {
        TARGET_TPS = targetTPS;
        FRAME = frame;
        SIMULATION = simulation;
    }

    @Override
    public void run() {
        running = true;
        long tickStartTime;
        long tickTime;
        double deltaTime = 1.0 / TARGET_TPS;
        while (running) {
            tickStartTime = System.nanoTime();
            SIMULATION.update(deltaTime);
            FRAME.repaint();
            while ((tickTime = System.nanoTime() - tickStartTime) < NANOS_PER_SECOND / TARGET_TPS) {}
            deltaTime = (double) tickTime / NANOS_PER_SECOND;
        }
    }

    private void stop() {
        running = false;
    }
}
