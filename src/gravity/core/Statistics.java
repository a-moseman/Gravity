package gravity.core;

public class Statistics {
    private static double sumVelocity;
    private static double avgVelocity;

    public static void reset() {
        sumVelocity = 0;
        avgVelocity = 0;
    }

    public static void addToSum(Body body) {
        sumVelocity += body.getSpeed();
    }

    public static void calcAvg(int size) {
        avgVelocity = sumVelocity / size;
    }

    public static double getSumVelocity() {
        return sumVelocity;
    }

    public static double getAvgVelocity() {
        return avgVelocity;
    }
}
