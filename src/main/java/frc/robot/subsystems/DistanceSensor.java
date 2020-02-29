package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public class DistanceSensor extends Subsystem {
    final AnalogInput leftSensor;
    final AnalogInput rightSensor;

    public DistanceSensor() {
        super();
        this.setName("Distance sensor");

        leftSensor = new AnalogInput(Config.ULTRASOUND_LEFT);
        SendableRegistry.setName(leftSensor, "Left Distance Sensor");
        rightSensor = new AnalogInput(Config.ULTRASOUND_RIGHT);
        SendableRegistry.setName(rightSensor, "Right Distance Sensor");
        
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void initOperatorInterface() {
        //LiveWindow.add(this);
        addChild(leftSensor);
        addChild(rightSensor);
    }
}
