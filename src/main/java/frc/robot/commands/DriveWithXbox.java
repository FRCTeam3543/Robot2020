/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * This command allows Xbox joystick to drive the robot. It is always running
 * except when interrupted by another command.
 */
public class DriveWithXbox extends Command {

  @Override
  protected void execute() {
      this.arcadeDrive(Robot.m_oi.getJoystick());
  }

  /**
	 * Arcade drive using individual joystick axes.
	 *
	 * @param xbox Xbox style to drive in arcade
	 *    
	 */
	void arcadeDrive(XboxController xbox){
		Robot.driveSystem.arcadeDrive(xbox.getRawAxis(1), xbox.getRawAxis(0), true);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    Robot.driveSystem.stop();
  }
}
