/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
  // public static final Encoder m_encoder = new Encoder(Config.ENCODER1_CHANNEL_A, Config.ENCODER1_CHANNEL_B, false, CounterBase.EncodingType.k4X);
  // public static Encoder m_encoder2 = new Encoder(Config.ENCODER2_CHANNEL_A, Config.ENCODER2_CHANNEL_B, true, CounterBase.EncodingType.k4X);


  public static final ElevatorSubsystem elevator = new ElevatorSubsystem();
  public static final DriveSubsystem driveSystem = new DriveSubsystem(); // manages driveline sensors and acutators
  public static final LineSensor lineSensor = new LineSensor();
  public static final DistanceSensor distanceSensor = new DistanceSensor();
  public static final OmniWheels omniSwitch = new OmniWheels();
  public static final Shooter shooterSystem = new Shooter();
  public static final WheelSpin spinnerSystem = new WheelSpin();

  BallCamera ballCamera;
  TargetCamera targetCamera = new TargetCamera();

  Autonomous autonomousMode;

  public static Config config = new Config();
  public static OI m_oi;

  public static State state = new State();


  // Instantiations of commands used in Robot
  // Command m_autonomousCommand;
  // SendableChooser<Command> m_chooser = new SendableChooser<>();

  // Instantiation of Compressor
  Compressor compressor = new Compressor();

  // public SendableChooser<Command> m_autoChooser;

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
    // m_autoChooser = new SendableChooser<Command>();

    // create and start the ball detect camera
    ballCamera = new BallCamera();

    initOperatorInterface();
    driveSystem.reset();
    driveSystem.calibrate();
    targetCamera.init();
    SmartDashboard.putString("Autonomous stage", "new");
  }


  @Override
  public void autonomousInit() {
    Robot.LOGGER.warning("AUTONOMOUS INIT");
    SmartDashboard.putString("Autonomous stage", "init");
    resetAll();

    autonomousMode = new Autonomous(driveSystem, shooterSystem);
    autonomousMode.init();
  }

  @Override
  protected void finalize() {
    super.finalize();
  }

  // This function is called periodically during autonomous
  @Override
  public void autonomousPeriodic() {
    if (isAutonomous()) {
      autonomousMode.periodic();
    }
  }

  @Override
  public void teleopInit() {
    resetAll();
    if (autonomousMode != null) {
      autonomousMode.reset();
    }

    SmartDashboard.putString("Autonomous stage", "OFF");

    Robot.shooterSystem.shuffleBoard();
    Robot.omniSwitch.defaultOmniStatus();

  }

  void resetAll() {
    driveSystem.reset(); // reset on teleop, to zero the gyro
    shooterSystem.reset();
    elevator.reset();
  }

  // This function is called periodically during operator control
  @Override
  public void teleopPeriodic() {
    updateState();
    targetCamera.update();
    m_oi.loop();
    omniSwitch.periodic();
    elevator.periodic();
    spinnerSystem.periodic();
    m_oi.arcadeDrive();

    Scheduler.getInstance().run();
    updateOperatorInterface();
    LiveWindow.updateValues();
  }

  @Override
  public void testInit()
  {
    teleopInit();
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

  static void driveStraight(double mag)
  {
    driveSystem.driveStraight(mag);
  }

  static void arcadeDrive(double mag, double turn, boolean squareInputs) {
    if ((mag > -0.2) & (mag < 0.2)) {
			mag = 0;
		}

		else {

		}

		if ((turn > -0.2) & (turn < 0.2)) {
			turn = 0;
		}

		else {

    }

    // This is inverted
    driveSystem.arcadeDrive(mag, turn, squareInputs);
  }

  static void arcadeDrive(double mag, double turn) {
    // This is inverted
    arcadeDrive(mag, turn, true);
  }

  void updateOperatorInterface() {
    shooterSystem.updateOperatorInterface();
    targetCamera.shuffleBoard();
    elevator.updateOperatorInterface();
    // ballCamera.checkForBall();
    // SmartDashboard.putNumber("Encoder 1 Distance", m_encoder.getDistance());
    // SmartDashboard.putNumber("Encoder 1 Rate", m_encoder.getRate());
    // SmartDashboard.putNumber("Encoder 2 Distance", m_encoder2.getDistance());
    // SmartDashboard.putNumber("Encoder 2 Rate", m_encoder2.getRate());
    // SmartDashboard.putNumber("Left Quad", driveSystem.getLeftQuadPosition());
    // SmartDashboard.putNumber("Right Quad", driveSystem.getRightQuadPosition());
  }
}