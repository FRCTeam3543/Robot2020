package frc.robot.subsystems;

import org.opencv.core.Mat;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public class LineSensor extends Subsystem implements HUDProvider {
    DigitalInput leftSensor = new DigitalInput(Config.LINE_SENSOR_LEFT);
    DigitalInput rightSensor = new DigitalInput(Config.LINE_SENSOR_RIGHT);

    @Override
    protected void initDefaultCommand() {

    }

    public void initOperatorInterface() {
        setName("Line Sensor");
        SendableRegistry.setName(leftSensor, "Left Line Sensor");
        SendableRegistry.setName(rightSensor, "Right Line Sensor");

        addChild(leftSensor);
        addChild(rightSensor);
        //LiveWindow.add(this);
    }

    public boolean isEitherEnabled() {
        return isLeftEnabled() || isRightEnabled();
    }

    public boolean isLeftEnabled() {
        return !leftSensor.get();
    }

    public boolean isRightEnabled() {
        return !rightSensor.get();
    }

    public boolean isBothEnabled() {
        return isLeftEnabled() && isRightEnabled();
    }

    @Override
    public void updateHUD(Mat mat) {
        HUD.with(mat)
            .color(isLeftEnabled() ? HUD.GREEN : HUD.RED)
            .thickness(4)
            .circle(HUD.point(25, 20), 10)
            .color(isRightEnabled() ? HUD.GREEN : HUD.RED)
            .circle(HUD.point(50, 20), 10);
	}
}
