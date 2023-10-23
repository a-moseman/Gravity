package gravity;

import gravity.core.Simulation;
import gravity.core.ui.Renderer;

import javax.swing.*;
import java.util.concurrent.Executors;

public class Main {
    public static final double TPS = 144;
    public static final double G = 0.1;
    public static final int BODY_COUNT = 2_000;
    public static final int SPEED_UP = 0;


    public static void main(String[] args) {
        Simulation simulation = new Simulation(G, BODY_COUNT, Executors.newVirtualThreadPerTaskExecutor());

        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Renderer renderer = new Renderer(simulation);
        frame.add(renderer);
        frame.setVisible(true);


        long tickStartTime;
        long tickTime;
        double deltaTime = 1.0 / TPS;
        while (true) {
            tickStartTime = System.nanoTime();
            simulation.update(deltaTime);
            frame.repaint();
            while ((tickTime = System.nanoTime() - tickStartTime) < (1_000_000_000 - SPEED_UP) / TPS) {}
            deltaTime = (double) tickTime / (1_000_000_000 - SPEED_UP);
        }
    }
}
