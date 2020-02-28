/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveWithXbox;
import frc.robot.subsystems.*;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // public static ExampleSubsystem m_subsystem = new ExampleSubsystem();

  /**
  * Instantiation of subsystems
  */
  public static final DriveWithXbox contorl = new DriveWithXbox(); 
  public static final DriveSubsystem driveSystem = new DriveSubsystem(); // manages driveline sensors and acutators
  public static final OmniStuff omniSwitch = new OmniStuff();
  public static final Shooter shooterSystem = new Shooter();
  public final Encoder m_encoder = new Encoder(1, 2, true, CounterBase.EncodingType.k4X);
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
    driveSystem.calibrate();
    driveSystem.reset();
    m_encoder.setSamplesToAverage(5);
    m_encoder.setDistancePerPulse(1.0 / 360.0 * 2.0 * Math.PI * 1.5);
    m_encoder.setMinRate(1.0);
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = (Command) m_autoChooser.getSelected();
    m_autonomousCommand.start();
  }

  @Override
  protected void finalize() {
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

    Robot.shooterSystem.shuffleBoard();
  }

  // This function is called periodically during operator control
  @Override
  public void teleopPeriodic() {
    updateState();
    m_oi.loop();
    omniSwitch.periodic();
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
  }
  /**
   * Add stuff to the OI
   */
  void initOperatorInterface() {
    driveSystem.initOperatorInterface();
  }

  void updateOperatorInterface() {
    SmartDashboard.putNumber("Encoder Distance", m_encoder.getDistance());
    SmartDashboard.putNumber("Encoder Rate", m_encoder.getRate());
  }
}