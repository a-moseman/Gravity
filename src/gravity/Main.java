import javax.swing.*;
import java.awt.*;

public class Main {
    public static final double TPS = 60;

    public static void main(String[] args) {
        Simulation simulation = new Simulation(0.001);
        simulation.initialize(10);

        JFrame frame = new JFrame();
        frame.setSize(640, 640);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                simulation.render(g);
            }
        });
        frame.setVisible(true);

        long tickStartTime;
        long tickTime;
        double deltaTime = 1.0 / TPS;
        while (true) {
            tickStartTime = System.nanoTime();
            simulation.update(deltaTime);
            frame.repaint();
            while ((tickTime = System.nanoTime() - tickStartTime) < 1_000_000_000 / TPS) {}
            deltaTime = (double) tickTime / 1_000_000_000;
        }
    }
}
