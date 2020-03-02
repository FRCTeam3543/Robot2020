/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Config;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public XboxController xbox = new XboxController(Config.XBOX_PORT);
//	public Joystick joystick = new Joystick(Config.JOYSTICK_PORT);

	// Instantiaion of Xbox
	public XboxController getJoystick() {
		return xbox;
	  }

	void loop() {
		checkReset();
        elevatorControl();
        wheelSpeed();
        omniControl();
        //wheelFlip();
        intakeControl();
        shootControl();
        reverseShootControl();
	}

	void checkReset() {
        if (xbox.getStartButtonPressed()) {
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
        // IF the Y button is pressed, we want to drive straight.  Otherwise we want
        // to turn.
        if (xbox.getYButton()) {
            Robot.driveStraight(mag * throttle);
        }
        else {
            Robot.arcadeDrive(mag * throttle,  turn * throttle * throttleMult + throttleOffset, false);
        }
	}

    /**
     * Controls the elevator lift with the xbox bumpers OR Joystick up/down
     */
	void elevatorControl()
	{
        if(xbox.getBumper(GenericHID.Hand.kRight)){
            Robot.elevator.goUp();
        }
        else if (Robot.m_oi.xbox.getBumper(GenericHID.Hand.kLeft)){
            Robot.elevator.goDown();
        }
        else {
            Robot.elevator.stay();
        }
	}

    //Change Wheel Speed//
    void wheelSpeed() {
        if (Robot.m_oi.xbox.getStickButtonPressed(GenericHID.Hand.kRight)) {
            Robot.driveSystem.toggleSpeed();
        }
    }

    //Omni Controls//
    void omniControl() {
        if (Robot.m_oi.xbox.getAButtonPressed()) {
            Robot.omniSwitch.toggleOmni();
        }
    }

/* Temporarily disabled
    void wheelFlip() {
        if (Robot.m_oi.xbox.getBButtonPressed()) {
            Robot.driveSystem.toggleDriveFlip();
        }
    }*/

    //Shooting Controls//

    void intakeControl() {
        if (Robot.m_oi.xbox.getXButton()) {
            Robot.shooterSystem.intake();
        }

        else if (Robot.m_oi.xbox.getXButtonReleased()) {
            Robot.shooterSystem.stopIntake();
        }
    }

    void shootControl() {
        if (Robot.m_oi.xbox.getTriggerAxis(GenericHID.Hand.kRight) > 0.5) {
            Robot.shooterSystem.shoot();
            Timer.delay(0.5);
            Robot.shooterSystem.intakeShoot();
        }

        else if (Robot.m_oi.xbox.getTriggerAxis(GenericHID.Hand.kRight) < 0.5) {
            Robot.shooterSystem.stopShoot();
        }
    }

    void reverseShootControl() {
        if (Robot.m_oi.xbox.getBButton()) {
            Robot.shooterSystem.reverse();
        }

        else if (Robot.m_oi.xbox.getBButtonReleased()) {
            Robot.shooterSystem.stopIntake();
        }
    }

}
