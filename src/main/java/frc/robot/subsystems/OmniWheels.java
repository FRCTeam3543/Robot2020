package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class OmniWheels extends Subsystem {
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(Config.OMNI_PORT_1, Config.OMNI_PORT_2);
    
    boolean isUp = false;
    
    @Override
    public void periodic() {
        super.periodic();
        if (isUp) {
            open();
        }

        else {
            close();
        }
    }
    
    

    public void toggleOmni() {
        isUp = !isUp;
    }

    public void open() {
        doubleSolenoid.set(Value.kForward);
        
    }

    public void close() {
        doubleSolenoid.set(Value.kReverse);
        
    }

    @Override
    protected void initDefaultCommand() {}
}