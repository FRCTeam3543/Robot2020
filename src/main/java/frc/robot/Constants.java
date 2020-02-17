/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  // FIXME - Constants are all wrong
  public static final double MAX_VELOCITY = 1.0; // m/s
  public static final int WHEEL_TICKS_PER_REV = 3640; // ticks per rev

  // Field Geometry
  public static final double TARGET_HEIGHT = 2.0;   // FIXME metres, height of target center above ground

  // Robot Geometry
  public static final double MUZZLE_HEIGHT = 0.5;  // FIXME metres, muzzle height above ground
  public static final double WHEEL_DIAMETER = 0.1; // m

  // Shooter Calibration
  public static final double MUZZLE_VELOCITY_TO_VOLTS = 1.0; // FIXME - m/s to volts
  public static final double MUZZLE_ANGLE = Math.PI / 4;  // 45 degrees


}
