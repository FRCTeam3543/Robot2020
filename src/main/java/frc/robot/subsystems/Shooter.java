package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class Shooter extends Subsystem {
    //SHOOTER//
    WPI_TalonSRX topShooter = new WPI_TalonSRX(Config.SHOOTER_TOP_MOTOR_PORT);
    WPI_TalonSRX bottomShooter = new WPI_TalonSRX(Config.SHOOTER_BOTTOM_MOTOR_PORT);

    //BALL INTAKE//
    WPI_TalonSRX topIntake = new WPI_TalonSRX(Config.INTAKE_TOP_MOTOR_PORT);
    WPI_TalonSRX bottomIntake = new WPI_TalonSRX(Config.INTAKE_BOTTOM_MOTOR_PORT);


   @Override
   public void periodic() {

   }

   public void shuffleBoard() {
       SmartDashboard.setDefaultNumber("Top Motor", 0.6); //Update once calibrated
       SmartDashboard.setDefaultNumber("Bottom Motor", 0.7); //Update once calibrated
   }

   public void shoot() {
        topShooter.set(SmartDashboard.getNumber("Top Motor", topShooter.get()));
        bottomShooter.set(SmartDashboard.getNumber("Bottom Motor", bottomShooter.get()));
   }

   public void intakeShoot() {
        shootIntake(Config.INTAKE_MOTOR_SPEED_UP);  
   }

   public void intake() {
        go(Config.INTAKE_MOTOR_SPEED_UP);
    }

   public void stopShoot() {
       topShooter.stopMotor();
       bottomShooter.stopMotor();
       topIntake.stopMotor();
       topShooter.set(0);
       bottomShooter.set(0);

       Robot.m_encoder.reset();
       Robot.m_encoder2.reset();
   }

   public void stopIntake() {
       bottomIntake.stopMotor();
   }

   public void reverse() {
       shootRev(Config.SHOOTER_MOTOR_SPEED_REV);
       go(Config.INTAKE_MOTOR_SPEED_REV);
   }

   public void shootRev(double speed) {
       topShooter.set(speed);
       bottomShooter.set(speed);
   }

   public void go(double speed) {
       bottomIntake.set(speed);
   }

   public void shootIntake(double speed) {
       topIntake.set(speed * -1);
   }
   @Override
   public void initDefaultCommand() {}
}