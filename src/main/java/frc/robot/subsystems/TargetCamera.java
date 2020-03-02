package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TargetCamera
{
    static final double SLOPE = -1.55; //-9/8;
    static final double INTERCEPT = 6.16; //91/16;

    final NetworkTable networkTable;

    public TargetCamera()
    {
        networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public double getTargetDistance()
    {
        double ta = networkTable.getEntry("ta").getDouble(0.0);
        SmartDashboard.putNumber("Target ta", ta);
        double distance = SLOPE * ta + INTERCEPT;
        SmartDashboard.putNumber("Target Distance", distance);
        return distance;
    }
}