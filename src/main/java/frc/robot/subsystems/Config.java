/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

public class Config {
    ////// OPERATOR INTERFACE //////
    public static final int XBOX_PORT  = 0;
    public static final int JOYSTICK_PORT = 1;

    ////// VISION //////
    // should be http://10.35.43.11/axis-cgi/mjpg/video.cgi
    public static final String CAMERA_ADDRESS = "10.35.43.11";
    public static final int CAMERA_WIDTH = 320;
    public static final int CAMERA_HEIGHT = 240;

    //Digital Ports//
    public static final int ENCODER1_CHANNEL_A = 1;
    public static final int ENCODER1_CHANNEL_B = 2;
    public static final int ENCODER2_CHANNEL_A = 4;
    public static final int ENCODER2_CHANNEL_B = 5;
    public static final int LINE_SENSOR_LEFT = 8;
    public static final int LINE_SENSOR_RIGHT = 9;

    ///Elevator (Temp)///
    public static final int ELEVATOR_MOTOR_PORT = 8;
    public static final double ELEVATOR_MOTOR_SPEED_UP = 1;
    public static final double ELEVATOR_MOTOR_STAY = 0.1;
    public static final double ELEVATOR_MOTOR_SPEED_DOWN = -0.5;

    ///// Drivetrain Motor Ports /////////
    public static final int DRIVELINE_LEFT_FRONT_MOTOR_PORT 		= 1;
    public static final int DRIVELINE_LEFT_REAR_MOTOR_PORT 		    = 2;
    public static final int DRIVELINE_RIGHT_FRONT_MOTOR_PORT 		= 6;
    public static final int DRIVELINE_RIGHT_REAR_MOTOR_PORT 		= 7;

    public static double MOTOR_TRIM = 0.75;

    // Shooter Motor Ports//
    public static final int SHOOTER_TOP_MOTOR_PORT = 9;
    public static final int SHOOTER_BOTTOM_MOTOR_PORT = 4;
    public static final double SHOOTER_MOTOR_SPEED = 0.5;
    public static final double SHOOTER_INTAKE_SPEED = 0.5;

    // Shooter encoder ports //
    public static final int SHOOTER_TOP_ENCODER_PORT_A = ENCODER2_CHANNEL_A; //4
    public static final int SHOOTER_TOP_ENCODER_PORT_B = ENCODER2_CHANNEL_B; //5
    public static final int SHOOTER_BOTTOM_ENCODER_PORT_A = ENCODER1_CHANNEL_A; //1
    public static final int SHOOTER_BOTTOM_ENCODER_PORT_B = ENCODER1_CHANNEL_B; //2

    // Shooter PID //
    // args are kP, kI, kD, kF
    public static final ShooterPID.PIDF SHOOTER_TOP_PIDF = new ShooterPID.PIDF(0.03, 0.00001, 0, 0);
    public static final ShooterPID.PIDF SHOOTER_BOTTOM_PIDF = SHOOTER_TOP_PIDF; // assume these are the same motor & encoder
    public static final double SHOOTER_PID_TOLERANCE_PERCENT = 2; // this is the percent under which the PID is "on target"
    // Shooter spin
    // set this to bump the output of the bottom shooter motor
    // should be greater than 1 for backspin, less than one for topspin
    public static final double SHOOTER_BACKSPIN_FACTOR = 1.0;
    public static final double SHOOTER_ANGLE_RADIANS = Math.PI / 4; // 45 degrees
    // this is the distance from the vertical centerpoint between the top and bottom shooter motors
    // and the center of the target
    public static final double SHOOTER_TARGET_HEIGHT_ABOVE_MUZZLE = 2.03; //
    // this the velocity we shoot at if we don't know the target distance
    public static final double SHOOTER_NOMINAL_VELOCITY = 0.6; //
    // this is the radius of the shooter wheel, in m
    public static final double SHOOTER_WHEEL_RADIUS = 0.076; // FIXME!
    // this is the max RPM of the encoders - https://www.revrobotics.com/rev-11-1271/
    // Quadrature Resolution: 2048 Cycles per Revolution (8192 Counts per Revolution)
    public static final double SHOOTER_ENCODER_PULSES_PER_REV = 8192; // set from docs
    public static final double SHOOTER_MOTOR_MAX_RPM = 10000; // encoder max
    public static final double SHOOTER_MOTOR_MAX_RADIANS_PER_SEC = SHOOTER_MOTOR_MAX_RPM / (60 * Math.PI * 2);
    public static final double SHOOTER_ENCODER_RADIANS_PER_PULSE = (Math.PI * 2) / SHOOTER_ENCODER_PULSES_PER_REV;

    // Reverse Speed //
    public static final double SHOOTER_MOTOR_SPEED_REV = -0.4;
    public static final double INTAKE_MOTOR_SPEED_REV = -0.5;

    //Ball Intake Ports //
    public static final int INTAKE_TOP_MOTOR_PORT = 10;
    public static final int INTAKE_BOTTOM_MOTOR_PORT = 5;
    public static final double INTAKE_MOTOR_SPEED_UP = 0.8;

    ///////////////////////////////////
    ///////// Omni Wheel Pneumatics /////////
    ///////////////////////////////////
    public static final int OMNI_PORT_1 = 0;
    public static final int OMNI_PORT_2 = 1;

    public static final double DRIVE_LEFT_QUAD_DPP = 0.00003844376;    // meters per pulse, measured 2/9/19
    public static final double DRIVE_RIGHT_QUAD_DPP = -DRIVE_LEFT_QUAD_DPP;

    // PCM Channels
    public static final int COMPRESSOR_PORT 						= 5;
	public static final int DRIVELINE_SOLENOID_PORT_1 				= 0;
    public static final int DRIVELINE_SOLENOID_PORT_2				= 1;
    public static final int DRIVELINE_SOLENOID_PORT_3               = 2;
    public static final int DRIVELINE_SOLENOID_PORT_4               = 3;


    //Gyro
    public static final double GYRO_SENSITIVITY = 0.007;
    public static final int GYRO_PORT = 0;
    public static final double GYRO_GAIN = 1 / 20; // this is the gain applied to the error in angle, for driving straight


    // Analog Ports
    public static final int ULTRASOUND_LEFT = 1;
    public static final int ULTRASOUND_RIGHT = 2;

    //PID's
    public static final double THROTTLE_MIN = 0.7;
    public static final double RUMBLE_VALUE = 0.7;
}
