package gravity;

import gravity.core.Simulation;
import gravity.core.UpdateLoop;
import gravity.core.ui.Input;
import gravity.core.ui.Renderer;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

public class Main {
    public static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    public static final double TPS = 144;
    public static final int BODY_COUNT = 2_000;


    public static void main(String[] args) {
        Simulation simulation = new Simulation(Executors.newVirtualThreadPerTaskExecutor(), BODY_COUNT);

        JFrame frame = new JFrame();
        frame.setSize(WINDOW_SIZE);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        Renderer renderer = new Renderer(simulation);
        frame.add(renderer);
        UpdateLoop updateLoop = new UpdateLoop(TPS, frame, simulation);
        Input input = new Input(updateLoop);
        frame.addKeyListener(input);
        Thread thread = new Thread(updateLoop);
        thread.start();
        frame.setVisible(true);

    }
}
