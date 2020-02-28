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

    ///// Drivetrain Motor Ports /////////
    public static final int DRIVELINE_LEFT_FRONT_MOTOR_PORT 		= 1;
    public static final int DRIVELINE_LEFT_REAR_MOTOR_PORT 		    = 2;
    public static final int DRIVELINE_RIGHT_FRONT_MOTOR_PORT 		= 6;
    public static final int DRIVELINE_RIGHT_REAR_MOTOR_PORT 		= 7;

    public static final double MOTOR_TRIM = 0.75;

    // Shooter Motor Ports//
    public static final int SHOOTER_TOP_MOTOR_PORT = 9; 
    public static final int SHOOTER_BOTTOM_MOTOR_PORT = 4; 
    public static final double SHOOTER_MOTOR_SPEED = 0.5;

    // Reverse Speed //
    public static final double SHOOTER_MOTOR_SPEED_REV = -0.4;
    public static final double INTAKE_MOTOR_SPEED_REV = -0.5;

    //Ball Intake Ports //
    public static final int INTAKE_TOP_MOTOR_PORT = 10;
    public static final int INTAKE_BOTTOM_MOTOR_PORT = 5;
    public static final double INTAKE_MOTOR_SPEED_UP = 0.7;

    // Omni Wheel Pneumatics //
    public static final int OMNI_PORT_1 = 0;
    public static final int OMNI_PORT_2 = 1;

    // PCM Channels
    public static final int COMPRESSOR_PORT 						= 5;
	public static final int DRIVELINE_SOLENOID_PORT_1 				= 0;
    public static final int DRIVELINE_SOLENOID_PORT_2				= 1;
    public static final int DRIVELINE_SOLENOID_PORT_3               = 2;
    public static final int DRIVELINE_SOLENOID_PORT_4               = 3;
    
    
    //Gyro
    public static final double GYRO_SENSITIVITY = 0.007;
    public static final int GYRO_PORT = 0;

    // Analog Ports
    public static final int ULTRASOUND_LEFT = 1;
    public static final int ULTRASOUND_RIGHT = 2;

    //PID's
    public static final double THROTTLE_MIN = 0.7;


}
