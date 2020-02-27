/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

/**
 * This is the drive subsystem (Motor Controllers and such)
 */
public class DriveSubsystem extends Subsystem {
	
	boolean directionFlip = false;

	@Override
    public void periodic() {
        super.periodic();
        if (directionFlip) {
			invert();
		}

		else {
			normal();
		}
	}
	
	private AnalogGyro gyro = new AnalogGyro(Config.GYRO_PORT);

	/**
	 * Subsystem Devices
	 */
	////////LEFT////////
	WPI_TalonSRX leftFrontTalonSRX = new WPI_TalonSRX(Config.DRIVELINE_LEFT_FRONT_MOTOR_PORT);
	WPI_TalonSRX leftRearTalonSRX = new WPI_TalonSRX(Config.DRIVELINE_LEFT_REAR_MOTOR_PORT);
	////////Right////////
	WPI_TalonSRX rightFrontTalonSRX = new WPI_TalonSRX(Config.DRIVELINE_RIGHT_FRONT_MOTOR_PORT);
	WPI_TalonSRX rightRearTalonSRX = new WPI_TalonSRX(Config.DRIVELINE_RIGHT_REAR_MOTOR_PORT);

	////////////////////
	//////  Group //////
	////////////////////
	private final SpeedControllerGroup LeftSide = new SpeedControllerGroup(leftFrontTalonSRX, leftRearTalonSRX);
	private final SpeedControllerGroup RightSide = new SpeedControllerGroup(rightFrontTalonSRX, rightRearTalonSRX);

	public DifferentialDrive m_drive;
	/**
	 * DriveSubsytem Constructure
	 */
	public DriveSubsystem(){
		super();
		setName("Drive Subsystem");

		leftFrontTalonSRX.setNeutralMode(NeutralMode.Brake);
		rightFrontTalonSRX.setNeutralMode(NeutralMode.Brake);	

		m_drive = new DifferentialDrive(LeftSide, RightSide);

		m_drive.setSafetyEnabled(false);
		 

		gyro.setSensitivity(Config.GYRO_SENSITIVITY);
		gyro.calibrate();

		SendableRegistry.setName(gyro, "Gyro");
		SendableRegistry.setName(m_drive, "Robot Drive");
		SendableRegistry.setName(LeftSide, "Left Wheels");
		SendableRegistry.setName(RightSide, "Right Wheels");
		reset();
	}
	
	public void toggleDriveFlip() {
		directionFlip = !directionFlip;
	}

	public void invert() {
		LeftSide.setInverted(true);
		RightSide.setInverted(true);
	}

	public void normal() {
		LeftSide.setInverted(false);
		RightSide.setInverted(false);
	}

/**
   * When other commands aren't using the drivetrain, allow tank drive with
   * the joystick.
   */
  @Override
  public void initDefaultCommand() {}

  	/**
	 * Get the value from the left stick
	 * 
	 * @param xSpeed Left side values
	 * @param zRotate
	 */

	public void arcadeDrive(double xSpeed, double zRotate, boolean squareInputs){
		m_drive.arcadeDrive(xSpeed * Config.MOTOR_TRIM, zRotate * Config.MOTOR_TRIM, squareInputs);
	}

  /**
   * Stop the drivetrain from moving.
   */
	public void stop() {
		m_drive.stopMotor();
	}

	public void reset() {
	  	stop();
  		gyro.reset();
  		resetEncoders();
	}

	public void calibrate() {
	  	gyro.calibrate();
	}

	public void resetEncoders() {
		leftFrontTalonSRX.getSensorCollection().setQuadraturePosition(0,0);
		rightFrontTalonSRX.getSensorCollection().setQuadraturePosition(0,0);
	}

	public double getHeading() {
	  	return this.gyro.getAngle();
	}

	public void initOperatorInterface() {
		addChild(gyro);
		addChild(m_drive);
		addChild(LeftSide);
		addChild(RightSide);
		SendableRegistry.addChild(LeftSide, leftFrontTalonSRX.getSensorCollection());
		SendableRegistry.addChild(RightSide, rightFrontTalonSRX.getSensorCollection());
	}

	public double getLeftQuadPosition() {
  		return leftFrontTalonSRX.getSensorCollection().getQuadraturePosition() * Config.DRIVE_LEFT_QUAD_DPP;
	}

	public double getRightQuadPosition() {
  		return rightFrontTalonSRX.getSensorCollection().getQuadraturePosition() * Config.DRIVE_RIGHT_QUAD_DPP;
	}
}
