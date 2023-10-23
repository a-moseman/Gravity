package gravity.core.ui;

import gravity.core.Simulation;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private final Simulation SIMULATION;

    public Renderer(Simulation simulation) {
        SIMULATION = simulation;
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        SIMULATION.render(g);
    }
}
