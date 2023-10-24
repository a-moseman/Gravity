package gravity.core.quadtree;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private static final double MIN_SECTOR_RANGE = 8.0;
    protected Sector sector;
    protected List<Body> bodies;
    protected VirtualBody virtualBody;
    protected Node[] children;

    public Node(Sector sector) {
        this.sector = sector;
        this.bodies = new ArrayList<>();
        this.virtualBody = new VirtualBody();
        this.children = new Node[4];
    }

    protected void add(Body body) {
        addToVirtualBody(virtualBody, body);
        if (virtualBody.size > 1 && canSubSector(sector)) {
            int quadrant = quadrantInSector(sector, body.posX, body.posY);
            if (children[quadrant] == null) {
                children[quadrant] = new Node(subSector(sector, body.posX, body.posY));
            }
            children[quadrant].add(body);
            return;
        }
        bodies.add(body);
    }

    protected void remove(Body body) {
        subFromVirtualBody(virtualBody, body);
        if (virtualBody.size > 0 && canSubSector(sector)) {
            int quadrant = quadrantInSector(sector, body.posX, body.posY);
            children[quadrant].remove(body);
            if (children[quadrant].virtualBody.size == 0) {
                children[quadrant] = null;
            }
            return;
        }
        bodies.remove(body);
    }

    protected void update(Body body, double deltaTime) {
        virtualBody.sumX += body.posX + body.velX * deltaTime;
        virtualBody.sumY += body.posY + body.velY * deltaTime;
        calcVirtualBodyAvgPos(virtualBody);

        int currentQuadrant = quadrantInSector(sector, body.posX, body.posY);
        int newQuadrant = quadrantInSector(sector, body.posX + body.velX * deltaTime, body.posY + body.velY * deltaTime);
        if (currentQuadrant == newQuadrant) { // did not move to a new quadrant
            children[currentQuadrant].update(body, deltaTime);
        }
        else {
            if (children[newQuadrant] == null) {
                children[newQuadrant] = new Node(subSector(sector, body.posX + body.velX * deltaTime, body.posY + body.velY * deltaTime));
            }
            children[currentQuadrant].remove(body);
            children[newQuadrant].add(body);
        }
    }

    protected List<VirtualBody> attractors(Body body) {
        List<VirtualBody> attractors = new ArrayList<>();
        getAttractorsHelper(body, attractors);
        return attractors;
    }

    private void getAttractorsHelper(Body target, List<VirtualBody> attractors) {
        int quadrant = quadrantInSector(sector, target.posX, target.posY);
        for (int q = 0; q < 4; q++) {
            if (quadrant == q || children[q] == null) {
                continue;
            }
            attractors.add(children[q].virtualBody);
        }
        if (bodies.isEmpty()) {
            children[quadrant].getAttractorsHelper(target, attractors);
        }
        else {
            VirtualBody clone = cloneVirtualBody(virtualBody);
            subFromVirtualBody(clone, target);
            attractors.add(clone);
        }
    }

    protected int size() {
        return virtualBody.size;
    }

    //---Body Functions---\\

    private static VirtualBody cloneVirtualBody(VirtualBody virtualBody) {
        VirtualBody clone = new VirtualBody();
        clone.sumX = virtualBody.sumX;
        clone.sumY = virtualBody.sumY;
        clone.avgX = virtualBody.avgX;
        clone.avgY = virtualBody.avgY;
        clone.size = virtualBody.size;;
        return clone;
    }

    private static void addToVirtualBody(VirtualBody virtualBody, Body bodyToAdd) {
        virtualBody.sumX += bodyToAdd.posX;
        virtualBody.sumY += bodyToAdd.posY;;
        virtualBody.mass += bodyToAdd.mass;
        virtualBody.size++;
        calcVirtualBodyAvgPos(virtualBody);
    }

    private static void subFromVirtualBody(VirtualBody virtualBody, Body bodyToSub) {
        if (virtualBody.size == 0) {
            throw new RuntimeException("Cannot subtract body from virtual body with size 0.");
        }
        virtualBody.sumX -= bodyToSub.posX;
        virtualBody.sumY -= bodyToSub.posY;;
        virtualBody.mass -= bodyToSub.mass;
        virtualBody.size--;
        calcVirtualBodyAvgPos(virtualBody);
    }

    private static void calcVirtualBodyAvgPos(VirtualBody virtualBody) {
        virtualBody.avgX = virtualBody.sumX / virtualBody.size;
        virtualBody.avgY = virtualBody.sumY / virtualBody.size;
    }

    //---Sector Functions---\\

    private static int quadrantInSector(Sector sector, double x, double y) {
        return quadrantInRange(sector.xRange, x) * 2 + quadrantInRange(sector.yRange, y);
    }

    private static int quadrantInRange(Range range, double value) {
        exceptIfOutOfRange(range, value);
        return value < range.center ? 0 : 1;
    }

    private static Sector subSector(Sector sector, double x, double y) {
        return new Sector(subRange(sector.xRange, x), subRange(sector.yRange, y));
    }

    private static Range subRange(Range range, double value) {
        exceptIfOutOfRange(range, value);
        if (value < range.center) {
            return new Range(range.min, range.center);
        }
        else {
            return new Range(range.center, range.max);
        }
    }

    private static boolean canSubRange(Range range) {
        return !(range.center - range.min < MIN_SECTOR_RANGE || range.max - range.center < MIN_SECTOR_RANGE);
    }

    private static boolean canSubSector(Sector sector) {
        return canSubRange(sector.xRange) && canSubRange(sector.yRange);
    }

    private static void exceptIfOutOfRange(Range range, double value) {
        if (value < range.min || value >= range.max) {
            throw new RuntimeException(String.format("Value %f out of range (%f, %f)", value, range.min, range.max));
        }
    }
}
