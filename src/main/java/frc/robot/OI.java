/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Config;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public static final int JOYSTICK_ELEVATOR_UP = 6;
    public static final int JOYSTICK_ELEVATOR_DOWN = 4;
    public static final int JOYSTICK_BALL_INTAKE = 2;
    public static final int JOYSTICK_RAMP_DEPLOY_1 = 11;
    public static final int JOYSTICK_RAMP_DEPLOY_2 = 12;


    public XboxController xbox = new XboxController(Config.XBOX_PORT);
//	public Joystick joystick = new Joystick(Config.JOYSTICK_PORT);

	// Instantiaion of Xbox
	public XboxController getJoystick() {
		return xbox;
	  }

	void loop() {
		checkReset();
		elevatorControl();
        omniControl();
        wheelFlip();
	}

	void checkReset() {
        if (xbox.getYButtonPressed()) {
            Robot.driveSystem.reset();
        }
    }

	/**
	 * Make the controller rumble
	 */
	void rumble() {
		xbox.setRumble(GenericHID.RumbleType.kLeftRumble, Config.RUMBLE_VALUE);
	}

	void arcadeDrive() {
		// tricky
		double mag = xbox.getRawAxis(1);
        double turn =  - xbox.getRawAxis(0); // needs to be backward
        if (Math.abs(turn) < 0.05) { // +/- 5% rounds to zero
            turn = 0;
        }
		double throttle = 1 - Math.abs(xbox.getRawAxis(5)); // 0 to 1
		throttle = throttle * (1 - Config.THROTTLE_MIN) + Config.THROTTLE_MIN;
		// adjust magnitude and turn by the throttle value
        double throttleMult = 1; // 1.5
        double throttleOffset = 0.2;
		Robot.arcadeDrive(mag * throttle,  turn * throttle * throttleMult + throttleOffset, false);
	}

    /**
     * Controls the elevator lift with the xbox bumpers OR Joystick up/down
     */
	void elevatorControl()
	{
        if(xbox.getBumper(GenericHID.Hand.kRight)
//                || joystick.getRawButton(JOYSTICK_ELEVATOR_UP)
        ){
            Robot.elevator.goUp();
        }
        else if (Robot.m_oi.xbox.getBumper(GenericHID.Hand.kLeft)
//                || joystick.getRawButton(JOYSTICK_ELEVATOR_DOWN)
        ){
            Robot.elevator.goDown();
        }
        else {
            Robot.elevator.stay();
        }
	}

    void omniControl() {
        if (Robot.m_oi.xbox.getAButtonPressed()) {
            Robot.omniSwitch.toggleOmni();
        }
    }

    void wheelFlip() {
        if (Robot.m_oi.xbox.getBButtonPressed()){
            Robot.driveSystem.toggleDriveFlip();
        }
    }

    void wheelStopDrift() {
        if (Robot.m_oi.xbox.getXButtonPressed()){
            Robot.driveSystem.toggleOmniDrift();
        }
    }
    /**
     * Ball shooter control
     */
    public boolean getJoystickButton(int num)
    {
        return false;
//        return joystick.getRawButton(num);
    }

    public boolean getTrigger()
    {
        return false;
//        return joystick.getTrigger();
    }
}
