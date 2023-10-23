package gravity.core.ui;

import gravity.core.Simulation;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private final Simulation SIMULATION;

    public Renderer(Simulation simulation) {
        SIMULATION = simulation;
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        SIMULATION.render(g);
        g.setColor(Color.BLUE);
        g.drawString("COMMANDS: ", 1600, 32);
        g.drawString("ESCAPE - Stop the simulation", 1600, 64);
        g.drawString("SPACE - Pause the simulation", 1600, 96);
    }
}
