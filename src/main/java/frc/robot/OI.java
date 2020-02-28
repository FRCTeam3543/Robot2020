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
        omniControl();
        //wheelFlip();
        intakeControl();
        shootControl();
        reverseShootControl();
	}

	void checkReset() {
        if (xbox.getYButtonPressed()) {
            Robot.driveSystem.reset();
        }
    }

    //Omni Controls//
    void omniControl() {
        if (Robot.m_oi.xbox.getAButtonPressed()) {
            Robot.omniSwitch.toggleOmni();
        }
    }

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
            Timer.delay(0.25);
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
