package gravity.core.math;

import java.security.SecureRandom;

public class Generator {
    private static final double TAU = Math.PI * 2;
    public static SecureRandom RANDOM = new SecureRandom();

    public static double randomAngle() {
        return RANDOM.nextDouble() * TAU;
    }

    public static double randomDouble(double min, double max) {
        return RANDOM.nextDouble() * (max - min) + min;
    }
}
