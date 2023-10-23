package gravity.core.math;

public class Physics {
    public static double gravityForceBetweenBodies(double gravityConstant, Vector position1, Vector position2, double mass1, double mass2) {
        double actualDistance = position1.distance(position2);
        // minDistance is a fix to an issue where bodies misbehave if too close
        // minDistance is mostly arbitrary, based on the assumption that if you're within the mass of a body, the force of gravity should decrease
        // todo: find a better solution than minDistance, such as more reality-based solution
        double minDistance = mass1 / 2 + mass2 / 2;
        double distance = Math.max(actualDistance, minDistance);
        return gravityConstant * ((mass1 * mass2) / (distance * distance));
    }
}
