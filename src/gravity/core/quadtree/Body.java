package gravity.core.quadtree;

public class Body {
    protected double posX;
    protected double posY;
    protected double mass;
    protected double velX;
    protected double velY;

    public Body(double posX, double posY, double mass) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.velX = 0;
        this.velY = 0;
    }
}
