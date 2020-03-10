/*
*   This subsystem may end up spinning the victor
*   based off of colour sensor input.
*   For now the motor will spin on button push.
*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.VictorSP;


public class  WheelSpin extends Subsystem {
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(Config.SPINNER_SOLENOID_PORT_1, Config.SPINNER_SOLENOID_PORT_2);

    boolean checkUp = false;

	@Override
    public void periodic() {
        super.periodic();
        if (checkUp) {
            spinOpen();
        }

        else {
            spinClose();
        }
    }
    

    VictorSP spinnerVictor = new VictorSP(Config.SPINNER_WHEEL_PORT);


    public void spin() {
        go(Config.SPINNER_MOTOR_SPEED);
    }

    public void stopSpin() {
        spinnerVictor.stopMotor();
    }

    public void toggleSpinner() {
        checkUp = !checkUp;
    }

    public void spinOpen() {
        doubleSolenoid.set(Value.kForward);

        Timer.delay(0.05);
    }

    public void spinClose() {
        doubleSolenoid.set(Value.kReverse);

        Timer.delay(0.05);
    }

    public void go(double speed) {
        spinnerVictor.set(speed);
    }

    @Override
    protected void initDefaultCommand() {
    }
}