/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static final Logger LOGGER = Logger.getLogger("Robot");

  // public static ExampleSubsystem m_subsystem = new ExampleSubsystem();

  /**
  * Instantiation of subsystems
  */
  public static final ElevatorSubsystem elevator = new ElevatorSubsystem();
  public static final DriveSubsystem driveSystem = new DriveSubsystem(); // manages driveline sensors and acutators
  public static final LineSensor lineSensor = new LineSensor();
  public static final DistanceSensor distanceSensor = new DistanceSensor();
  public static final OmniWheels omniSwitch = new OmniWheels();
  public static final CameraSubsystem cameraVision = new CameraSubsystem();
  

  public static Config config = new Config();
  public static OI m_oi;

  public static State state = new State();


  // Instantiations of commands used in Robot
  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  // Instantiation of Compressor
  Compressor compressor = new Compressor();

  public SendableChooser<Command> m_autoChooser;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    // This MUST be here. If the OI creates Commands (which it very likely
    // will), constructing it during the construction of CommandBase (from
    // which commands extend), subsystems are not guaranteed to be
    // yet. Thus, their requires() statements may grab null pointers. Bad
    // news. Don't move it.
    m_oi = new OI();
    // instantiate the command used for the autonomous period
    m_autoChooser = new SendableChooser<Command>();

    initOperatorInterface();
   // lifty.reset();
    driveSystem.calibrate();
    driveSystem.reset();
    cameraVision.initCameraThread();
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = (Command) m_autoChooser.getSelected();
    m_autonomousCommand.start();
  }

  @Override
  protected void finalize() {
    cameraVision.stopCameraThread();
    super.finalize();
  }

  // This function is called periodically during autonomous
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  boolean lineFollowing = false;
  // This function is called periodically during operator control
  @Override
  public void teleopPeriodic() {
    updateState();
    m_oi.loop();
    omniSwitch.periodic();
    elevator.periodic();

    if (m_oi.xbox.getXButtonPressed()) {
      lineFollowing = !lineFollowing;
      SmartDashboard.putBoolean("Line following", lineFollowing);
    }
    if (lineFollowing) {
      followLine();
    }
    else {
      m_oi.arcadeDrive();
    }

    Scheduler.getInstance().run();
    updateOperatorInterface();
    LiveWindow.updateValues();
  }

  // This function called periodically during test mode
  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  // This function is called periodically while disabled
  @Override
  public void disabledPeriodic() {
  }

  /**
   * Update the robot state
   */
  void updateState() {
    state.heading = driveSystem.getHeading();
  }

  void reset() {
    driveSystem.reset();
   // lifty.reset();
  }
  /**
   * Add stuff to the OI
   */
  void initOperatorInterface() {
    driveSystem.initOperatorInterface();
    lineSensor.initOperatorInterface();
    distanceSensor.initOperatorInterface();
  }

  /**
   * Basic line following.
   *
   * Read the line sensors.  If no sensors active, stop.
   *
   * If at least one active, attempt to follow the line, using the speed from the controller.
   */
  void followLine() {
    if (lineSensor.isEitherEnabled()) {
      // if left is on and right is not, turn left
      double mag = m_oi.xbox.getRawAxis(1);
      mag = Math.max(0.6, mag);
      mag = Math.min(0.4, mag);

      mag = -0.5;
      double turnSpeed = 0.2;

      double turn = 0;
      if (lineSensor.isBothEnabled()) {
        // turn = 0
      }
      if (lineSensor.isLeftEnabled()) { // turn to left
          turn = -turnSpeed;
      }
      else if (lineSensor.isRightEnabled()) { // turn to right
          turn = turnSpeed;
      }
      arcadeDrive(mag, turn);
    }
    else { // just arcade drive
      m_oi.arcadeDrive();
    }
  }

//  void arcadeDrive() {
//      arcadeDrive(m_oi.xbox.getRawAxis(1), -m_oi.xbox.getRawAxis(0));
//  }

  static void arcadeDrive(double mag, double turn, boolean squareInputs) {
    // for some reason this is inverted
    driveSystem.arcadeDrive(mag, turn, squareInputs);
  }

  static void arcadeDrive(double mag, double turn) {
    // for some reason this is inverted
    arcadeDrive(mag, turn, true);
  }

  void updateOperatorInterface() {
    SmartDashboard.putNumber("Left Quad", driveSystem.getLeftQuadPosition());
    SmartDashboard.putNumber("Right Quad", driveSystem.getRightQuadPosition());
  }
}