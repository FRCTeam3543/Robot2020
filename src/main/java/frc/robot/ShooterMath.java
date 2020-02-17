package frc.robot;

import java.util.logging.Logger;

public class ShooterMath
{
    private static final Logger LOGGER = Logger.getLogger("ShooterMath");

      // convenience
    public final double cosAngle;
    public final double cosAngleSquared;
    public final double sinAngle;
    public final double sinOverCos;
    public final double targetHeight;
    public final double k;

    public static final double G = 9.81;  // m/s^2

    public ShooterMath(double angleInRadians, double targetHeightAboveMuzzle, double muzzleVelocityToVolts)
    {
        // these are for speed/convenience
        cosAngle = Math.cos(angleInRadians);
        sinAngle = Math.sin(angleInRadians);
        cosAngleSquared = Math.pow(cosAngle,2);
        sinOverCos = sinAngle/cosAngle;
        targetHeight = targetHeightAboveMuzzle;
        k = muzzleVelocityToVolts;
    }

    /**
     *
     * @param d horizontal distance from the target in metres
     * @return
     */
    public double muzzleVelocityFromDistanceAway(final double d)
    {
        try {
            double ret = Math.sqrt(G * Math.pow(d,2) / (2 * cosAngleSquared * ( d * sinOverCos - targetHeight)));
            if (Double.isNaN(ret)) {
                LOGGER.info("Bad square root, there isn't a shot (NaN)");
                return 0.0;
            }
            return ret;
        }
        catch (Throwable ex) {
            LOGGER.info("Bad square root, there isn't a shot: "+ex.getMessage());
        }
        return 0.0;
    }

    /**
     * Get the motor output needed to hit the target from the given distance
     *
     * @param d horizontal distance from the target in metres
     * @return power to motor - CHECK IF GREATHER THAN 1, IF SO DON'T TAKE THE SHOT
     * @see Constants.MUZZLE_VELOCITY_TO_VOLTS for calibration
     */
    public double muzzleVoltsForDistanceAway(final double d)
    {
        return k * muzzleVelocityFromDistanceAway(d);
    }
}