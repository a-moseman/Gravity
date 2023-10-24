package gravity.core.quadtree;

public class Range {
    protected double min;
    protected double max;
    protected double center;

    protected Range(double min, double max) {
        this.min = min;
        this.max = max;
        this.center = (min + max) / 2;
    }
}
