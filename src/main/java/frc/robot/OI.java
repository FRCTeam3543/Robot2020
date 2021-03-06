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
    public XboxController xbox2 = new XboxController(Config.XBOX_2_PORT);

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
        spinnerWheel();
        spinnerPneumatics();
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
        throttle -= 0.5;
        throttle *= 2;
		// throttle throttle * (1 - Config.THROTTLE_MIN) + Config.THROTTLE_MIN;
		// adjust magnitude and turn by the throttle value
        double throttleMult = 1; // 1.5
        double throttleOffset = 0; //0.2;
        // IF the Y button is pressed, we want to drive straight.  Otherwise we want
        // to turn.
        // FIXME - this is disabled
        if (false && xbox.getYButton()) {
            // Robot.driveStraight(mag * throttle);
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
        boolean leftBumper = xbox2.getBumper(GenericHID.Hand.kLeft);
        double leftTrigger = xbox2.getTriggerAxis(GenericHID.Hand.kLeft);
        boolean rightBumper = xbox2.getBumper(GenericHID.Hand.kRight);
        double rightTrigger = xbox2.getTriggerAxis(GenericHID.Hand.kRight);

        if (xbox2.getAButton()) {
            Robot.elevator.leftDownSlow();
        }
        else if (leftBumper){
            Robot.elevator.leftUp();
        }
        else if (leftTrigger > 0.5) {
            Robot.elevator.leftDown();
        }
        else {
            Robot.elevator.leftStop();
        }

        if (xbox2.getBButton()) {
            Robot.elevator.rightDownSlow();
        }
        else if (rightBumper){
            Robot.elevator.rightUp();
        }
        else if (rightTrigger > 0.5) {
            Robot.elevator.rightDown();
        }
        else {
            Robot.elevator.rightStop();
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
        if (xbox.getTriggerAxis(GenericHID.Hand.kRight) >= 0.5) {
            Robot.shooterSystem.shoot();
            // Timer.delay(0.5);
            // Robot.shooterSystem.intakeShoot();
        }
        // else if (Robot.m_oi.xbox.getTriggerAxis(GenericHID.Hand.kRight) < 0.5) {
        //     Robot.shooterSystem.stopShoot();
        // }
        else {
            Robot.shooterSystem.stopShoot();
        }
        Robot.shooterSystem.intakeShoot(xbox.getYButton());
    }

    void reverseShootControl() {
        if (Robot.m_oi.xbox.getBButton()) {
            Robot.shooterSystem.reverse();
        }

        else if (Robot.m_oi.xbox.getBButtonReleased()) {
            Robot.shooterSystem.stopIntake();
        }
    }

    void spinnerWheel() {
        if (xbox2.getXButton()) {
            Robot.spinnerSystem.spin();
        }

        else {
            Robot.spinnerSystem.stopSpin();
        }
    }

    void spinnerPneumatics() {
        if (xbox2.getYButtonPressed()) {
            Robot.spinnerSystem.toggleSpinner();
        }
    }
}
