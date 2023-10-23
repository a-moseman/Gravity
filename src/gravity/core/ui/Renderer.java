package gravity.core.ui;

import gravity.core.Simulation;
import gravity.core.Statistics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer extends JPanel {
    private final Simulation SIMULATION;
    private final Camera CAMERA;
    private BufferedImage screenBuffer;
    private Graphics bufferGraphics;

    public Renderer(Simulation simulation, Camera camera) {
        SIMULATION = simulation;
        CAMERA = camera;
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // render bodies
        screenBuffer = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = screenBuffer.getGraphics();
        SIMULATION.render(bufferGraphics);
        int x = (int) -CAMERA.getPosition().getX();
        int y = (int) -CAMERA.getPosition().getY();
        g.drawImage(screenBuffer, x, y, this);
        bufferGraphics.dispose();
        // render statistics
        g.setColor(Color.BLUE);
        g.drawString("STATISTICS:", 32, 32);
        g.drawString(String.format("DT: %.4f", SIMULATION.getLastDeltaTime()), 32, 64);
        g.drawString(String.format("TPS: %.4f", 1.0 / SIMULATION.getLastDeltaTime()), 32, 96);
        g.drawString(String.format("SUM V: %.4f", Statistics.getSumVelocity()), 32, 128);
        g.drawString(String.format("AVG V: %.4f", Statistics.getAvgVelocity()), 32, 160);
        // render command legend
        g.drawString("COMMANDS: ", 1600, 32);
        g.drawString("ESCAPE - Stop the simulation", 1600, 64);
        g.drawString("SPACE - Pause the simulation", 1600, 96);
        g.drawString("ARROW KEYS - Move the camera", 1600, 128);

    }
}
