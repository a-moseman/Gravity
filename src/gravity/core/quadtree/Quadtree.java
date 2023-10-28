package gravity.core.quadtree;

import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    private Node root;
    private List<Body> bodies;

    public Quadtree(double minX, double maxX, double minY, double maxY) {
        root = new Node(new Sector(new Range(minX, maxX), new Range(minY, maxY)));
        bodies = new ArrayList<>();
    }

    public void add(Body body) {
        root.add(body);
        bodies.add(body);
    }

    public void remove(Body body) {
        root.remove(body);
        bodies.remove(body);
    }

    public void update(double deltaTime) {
        for (Body body : bodies) {
            root.update(body, deltaTime);
        }
    }

    public List<VirtualBody> attractors(Body body) {
        return root.attractors(body);
    }

    public int size() {
        return bodies.size();
    }

    public Body get(int index) {
        return bodies.get(index);
    }


}
