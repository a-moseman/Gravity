package gravity.core.quadtree;

public class VirtualBody {
    protected double sumX;
    protected double sumY;
    protected double mass;
    protected double avgX;
    protected double avgY;
    protected int size;

    public VirtualBody() {
        sumX = 0;
        sumY = 0;
        mass = 0;
        avgX = 0;
        avgY = 0;
        size = 0;
    }
}
