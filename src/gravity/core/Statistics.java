package gravity.core;

public class Statistics {
    private double sumVelocity;
    private double avgVelocity;

    public void reset() {
        sumVelocity = 0;
        avgVelocity = 0;
    }

    public void addToSum(Body body) {
        sumVelocity += body.getSpeed();
    }

    public void calcAvg(int size) {
        avgVelocity = sumVelocity / size;
    }

    public double getSumVelocity() {
        return sumVelocity;
    }

    public double getAvgVelocity() {
        return avgVelocity;
    }
}
