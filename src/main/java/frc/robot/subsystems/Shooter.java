package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class Shooter extends Subsystem {
    // SHOOTER//
    WPI_TalonSRX topShooter = new WPI_TalonSRX(Config.SHOOTER_TOP_MOTOR_PORT);
    WPI_TalonSRX bottomShooter = new WPI_TalonSRX(Config.SHOOTER_BOTTOM_MOTOR_PORT);

    // BALL INTAKE//
    WPI_TalonSRX topIntake = new WPI_TalonSRX(Config.INTAKE_TOP_MOTOR_PORT);
    WPI_TalonSRX bottomIntake = new WPI_TalonSRX(Config.INTAKE_BOTTOM_MOTOR_PORT);

    Encoder topShooterEncoder = new Encoder(Config.SHOOTER_TOP_ENCODER_PORT_A, Config.SHOOTER_TOP_ENCODER_PORT_B);
    Encoder bottomShooterEncoder = new Encoder(Config.SHOOTER_BOTTOM_ENCODER_PORT_A, Config.SHOOTER_BOTTOM_ENCODER_PORT_B);

    PIDSubsystem topShooterPID = new ShooterPID("Top Shooter PID", Config.SHOOTER_TOP_PIDF,
        () -> topShooterEncoder.getRate() * -1,
        (x) -> topShooter.set(trim(x))
    );
    PIDSubsystem bottomShooterPID = new ShooterPID("Bottom Shooter PID", Config.SHOOTER_BOTTOM_PIDF,
        () -> bottomShooterEncoder.getRate(),
        (x) -> bottomShooter.set(trim(x))
    );

    ShooterMath shooterMath = new ShooterMath(Config.SHOOTER_ANGLE_RADIANS, Config.SHOOTER_TARGET_HEIGHT_ABOVE_MUZZLE);

    TargetCamera targetCamera = new TargetCamera();

    public Shooter() {
        super("Shooter");

        topShooterEncoder.setDistancePerPulse(Config.SHOOTER_ENCODER_RADIANS_PER_PULSE);
        bottomShooterEncoder.setDistancePerPulse(Config.SHOOTER_ENCODER_RADIANS_PER_PULSE);
        topShooterEncoder.setSamplesToAverage(5);
        bottomShooterEncoder.setSamplesToAverage(5);
        topShooterPID.setOutputRange(-1, 1);
        bottomShooterPID.setOutputRange(-1, 1);
        topShooterPID.disable();
        bottomShooterPID.disable();
    }

    double trim(double x) {
        return Math.min(1, Math.max(-1, x));
    }

    @Override
    public void periodic() {
        // updates the target distance on the shuffleboard
        targetCamera.shuffleBoard();
        // checkIfWaiting();
    }

    public void shuffleBoard() {
        SmartDashboard.setDefaultNumber("Top Motor", 0.6); // Update once calibrated
        SmartDashboard.setDefaultNumber("Bottom Motor", 0.7); // Update once calibrated
    }

    double backspinFactor = Config.SHOOTER_BACKSPIN_FACTOR;

    public void shoot() {
        // topShooter.set(SmartDashboard.getNumber("Top Motor", topShooter.get()));
        // bottomShooter.set(SmartDashboard.getNumber("Bottom Motor", bottomShooter.get()));
        backspinFactor = SmartDashboard.getNumber("Backspin factor", Config.SHOOTER_BACKSPIN_FACTOR);
        startShooting(getShooterMotorSpeedNeededToHitTarget());
    }

    public void intakeShoot() {
        shootIntake(SmartDashboard.getNumber("Shooter intake Speed", Config.INTAKE_MOTOR_SPEED_UP));
    }

    public void intake() {
        go(Config.INTAKE_MOTOR_SPEED_UP);
    }

    public void enablePID(final double targetSpeed) {
        topShooterPID.setPercentTolerance(Config.SHOOTER_PID_TOLERANCE_PERCENT);
        bottomShooterPID.setPercentTolerance(Config.SHOOTER_PID_TOLERANCE_PERCENT);
        topShooterPID.setSetpoint(targetSpeed);
        // the bottom shooter target is multiplied by the amount desired for backspin
        bottomShooterPID.setSetpoint(targetSpeed * backspinFactor);
        topShooterPID.enable();
        bottomShooterPID.enable();
    }

    public void disablePID() {
        topShooterPID.disable();
        bottomShooterPID.disable();
    }

    public void stopShoot() {
        disablePID();
        waitStart = -1;
        waitRunnable = null;
        topShooter.stopMotor();
        bottomShooter.stopMotor();
        topIntake.stopMotor();
        topShooter.set(0);
        bottomShooter.set(0);

        topShooterEncoder.reset();
        bottomShooterEncoder.reset();
    }

    public void stopIntake() {
        bottomIntake.stopMotor();
    }

    // enum ShotStage {
    //     NONE, WARMING, SHOOTING
    // };

    // ShotStage shotStage = ShotStage.NONE;
    double desiredShotVelocity = 0.0;
    static final long SHOT_TIME = 1000; // shoot for 1s


    public void startShooting(double targetVelocity) {

        // as discussed this is the take-a-shot routine
        // if (shotStage == ShotStage.NONE) {
            // waits until the shoot motors are at the right speed
            // shotStage = ShotStage.WARMING;

        long wait = (long)SmartDashboard.getNumber("Intake delay", 500);
        // waitThen(wait, () -> enablePID(targetVelocity));
        SmartDashboard.putNumber("Target Velocity", targetVelocity);
        enablePID(targetVelocity);
        intakeShoot();

        // } else {
        //     Robot.LOGGER.info("Already shooting, can't shoot now");
        // }
    }

    Runnable waitRunnable;
    long waitStart = -1; // stores the timestamp when we start the shot
    long waitTimeout = -1;
    // sets the state above to wait for timeout milliseconds
    void waitThen(long timeout, Runnable what) {
        waitStart = System.currentTimeMillis();
        waitRunnable = what;
        waitTimeout = timeout;
    }

    // cancels any waiting runnable
    void cancelWait() {
        waitStart = -1;
        waitRunnable = null;
    }

    // called by periodoc
    void checkIfWaiting() {
        // // if we're waiting, and the timeout has expired and we have a runnable, run it
        // if (waitStart > 0 && (System.currentTimeMillis() - waitStart) > waitTimeout && waitRunnable != null) {
        //     waitRunnable.run();
        //     cancelWait();
        // }
    }

    double getShooterMotorSpeedNeededToHitTarget()
    {
        double distanceToTarget = targetCamera.getTargetDistance();
        if (distanceToTarget < 0) {
            Robot.LOGGER.info("Not on target, can't shoot");
            return Config.SHOOTER_NOMINAL_VELOCITY;
        }
        double requiredMuzzleVelocity = shooterMath.muzzleVelocityFromDistanceAway(distanceToTarget);
        SmartDashboard.putNumber("Muzzle Velocity", requiredMuzzleVelocity);
        // we need to translate the muzzle velocity to an angular velocity
        return muzzleVelocityToMotorSpeed(requiredMuzzleVelocity);
    }

    double muzzleVelocityToMotorSpeed(double muzzleVelocity)
    {
        double angularVelocity = muzzleVelocity / Config.SHOOTER_WHEEL_RADIUS;
        return angularVelocity;
        // return the range, clipped
        // return Math.max(-1.0, Math.max(1.0, angularVelocity / Config.SHOOTER_MOTOR_MAX_RADIANS_PER_SEC));
    }

    public void reset() {
        stopShoot();
    }

    boolean isAtSetpoint() {
        return topShooterPID.onTarget() && bottomShooterPID.onTarget();
    }

    public void controlShot() {
        // switch (shotStage) {
        //     case WARMING:
        //         if (isAtSetpoint()) {
        //             shotStage = ShotStage.SHOOTING;
        //             shotStartTime = System.currentTimeMillis();
        //         }
        //         break;
        //     case SHOOTING:
        //         if (System.currentTimeMillis() - shotStartTime > SHOT_TIME) {
        //             disablePID();
        //             shotStage = ShotStage.NONE;
        //         }
        //         else {
        //             shootIntake(Config.SHOOTER_INTAKE_SPEED);
        //         }
        //         // we want to be shooting, since we should be at the setpoint
        //         // it should shoooooot
        //         break;
        //     default:
        //         // ensure PID is OFF
        //         shootIntake(0);
        //         disablePID();
        //         break;

        // }
    }

    public void reverse() {
        shootRev(Config.SHOOTER_MOTOR_SPEED_REV);
        go(Config.INTAKE_MOTOR_SPEED_REV);
    }

    public void shootRev(final double speed) {
        topShooter.set(speed);
        bottomShooter.set(speed);
    }

    public void go(final double speed) {
        bottomIntake.set(speed);
    }

    public void shootIntake(final double speed) {
        topIntake.set(speed * -1);
    }

    @Override
    public void initDefaultCommand() {
    }

    public void updateOperatorInterface() {
        SmartDashboard.putNumber("Encoder 1 Distance", topShooterEncoder.getDistance());
        SmartDashboard.putNumber("Encoder 1 Rate", topShooterEncoder.getRate());
        SmartDashboard.putNumber("Encoder 2 Distance", bottomShooterEncoder.getDistance());
        SmartDashboard.putNumber("Encoder 2 Rate", bottomShooterEncoder.getRate());
        SmartDashboard.putNumber("Top PID Error", topShooterPID.getPIDController().getAvgError());
        SmartDashboard.putNumber("Bottom PID Error", topShooterPID.getPIDController().getAvgError());
        SmartDashboard.putNumber("Setpoint", topShooterPID.getSetpoint());
    }
}