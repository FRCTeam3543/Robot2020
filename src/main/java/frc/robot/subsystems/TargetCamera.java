package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TargetCamera
{
    // static final double SLOPE = -1.55; //-9/8;
    // static final double INTERCEPT = 6.16; //91/16;

    static final double SLOPE = -1.25; //-9/8;
    // static final double INTERCEPT = 6.19; //91/16;
    static final double INTERCEPT = 5.5; //91/16;

    final NetworkTable networkTable;

    public static final int SMOOTHING = 5;
    // we track these as a moving average to smooth it
    double rawDist = 0.0;
    double ta = 0.0;
    double dist = 0.0;

    public TargetCamera()
    {
        networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public void update() {

        double slope = SmartDashboard.getNumber("Target slope", SLOPE);
        double intercept = SmartDashboard.getNumber("Target intercept", INTERCEPT);

        ta = getTa();
        rawDist = slope * ta + intercept;
        // moving average
        dist = (dist * SMOOTHING + rawDist) / (SMOOTHING + 1);
    }

    double getTa()
    {
        return networkTable.getEntry("ta").getDouble(0.0);
    }

    public double getTargetDistance()
    {
        update();
        return dist;
    }

    public void shuffleBoard()
    {
        SmartDashboard.putNumber("Target ta", ta);
        SmartDashboard.putNumber("Target Distance (Raw)", rawDist);
        SmartDashboard.putNumber("Target Distance (Smoothed)", dist);
    }
}