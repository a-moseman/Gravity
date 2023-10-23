package gravity.core.math;

public class Vector {
    public static Vector UP = new Vector(0, 1);
    public static Vector DOWN = new Vector(0, -1);
    public static Vector RIGHT = new Vector(1, 0);
    public static Vector LEFT = new Vector(-1, 0);

    public static Vector zero() {
        return new Vector(0, 0);
    }

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector sum(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public void add(Vector other) {
        x += other.x;
        y += other.y;
    }

    public Vector difference(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public void subtract(Vector other) {
        x -= other.x;
        y -= other.y;
    }

    public Vector product(double m) {
        return new Vector(x * m, y * m);
    }

    public void multiply(double m) {
        x *= m;
        y *= m;
    }

    public double distance(Vector other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector normalized() {
        return this.product(1.0 / magnitude());
    }

    public void normalize() {
        multiply(1.0 / magnitude());
    }

    public Vector direction(Vector other) {
        return other.difference(this).normalized();
    }

    public static Vector fromAngle(double radian) {
        return new Vector(Math.cos(radian), Math.sin(radian));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
