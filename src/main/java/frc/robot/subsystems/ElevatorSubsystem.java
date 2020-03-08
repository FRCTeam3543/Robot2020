package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorSubsystem extends Subsystem{

    final WPI_TalonSRX leftMotor;
    final WPI_TalonSRX rightMotor;

    final DigitalInput leftSwitch;
    final DigitalInput rightSwitch;

    @Override
    protected void initDefaultCommand() {}

    public ElevatorSubsystem()
    {
        super("Elevator");
        leftSwitch = new DigitalInput(Config.ELEVATOR_LEFT_SWITCH_PORT);
        rightSwitch = new DigitalInput(Config.ELEVATOR_RIGHT_SWITCH_PORT);

        leftMotor = new WPI_TalonSRX(Config.ELEVATOR_LEFT_MOTOR_PORT);
        leftMotor.setInverted(true);

        rightMotor = new WPI_TalonSRX(Config.ELEVATOR_RIGHT_MOTOR_PORT);

        leftMotor.setNeutralMode(NeutralMode.Brake);
        rightMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void periodic() {
        super.periodic();
    }

    public void leftUp() {
        leftMotor.set(!leftSwitch.get() ? 0 : Config.ELEVATOR_UP_SPEED);
    }

    public void leftDown() {
        leftMotor.set(Config.ELEVATOR_DOWN_SPEED);
    }

    public void leftDownSlow() {
        leftMotor.set(Config.ELEVATOR_DOWN_SLOW_SPEED);
    }

    public void leftStop() {
        leftMotor.set(0);
    }

    public void rightUp() {
        rightMotor.set(!rightSwitch.get() ? 0 : Config.ELEVATOR_UP_SPEED);
    }

    public void rightDown() {
        rightMotor.set(Config.ELEVATOR_DOWN_SPEED);
    }

    public void rightDownSlow() {
        rightMotor.set(Config.ELEVATOR_DOWN_SLOW_SPEED);
    }

    public void rightStop() {
        rightMotor.set(0);
    }

    public void reset() {
        leftStop();
        rightStop();
    }

    public void updateOperatorInterface()
    {
        SmartDashboard.putBoolean("Left switch", leftSwitch.get());
        SmartDashboard.putBoolean("Right switch", rightSwitch.get());
    }
}