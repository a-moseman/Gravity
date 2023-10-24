package gravity.core.quadtree;

public class Sector {
    protected Range xRange;
    protected Range yRange;

    public Sector(Range xRange, Range yRange) {
        this.xRange = xRange;
        this.yRange = yRange;
    }
}
