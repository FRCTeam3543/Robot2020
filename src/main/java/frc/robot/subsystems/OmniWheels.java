package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    public void defaultOmniStatus() {
        SmartDashboard.setDefaultBoolean("Omni Wheels Up", false);
        SmartDashboard.setDefaultBoolean("Omni Wheels Down", false);
    }

    public void open() {
        doubleSolenoid.set(Value.kForward);
        SmartDashboard.putBoolean("Omni Wheels Up", true);
        SmartDashboard.putBoolean("Omni Wheels Down", false);
        Timer.delay(0.05);
    }

    public void close() {
        doubleSolenoid.set(Value.kReverse);
        SmartDashboard.putBoolean("Omni Wheels Up", false);
        SmartDashboard.putBoolean("Omni Wheels Down", true);
        Timer.delay(0.05);
    }

    @Override
    protected void initDefaultCommand() {}
}