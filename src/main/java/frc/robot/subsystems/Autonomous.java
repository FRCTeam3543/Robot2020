package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous
{
    final DriveSubsystem drive;
    final Shooter shooter;

    static final int NONE = 0;
    static final int SHOOT = 1;
    static final int BACKUP = 2;
    static final int SPIN = 3;
    static final int DONE = 5;

    static final long SHOOT_TIME = 8000; // ms
    static final long SPIN_UP_TIME = 1000; // ms
    static final long SPIN_INTAKE_TIME = 2000; // ms
    static final long BACKUP_TIME = 3000; // ms
    static final double SPIN_DEGREES = 180; // deg
    static final long SPIN_TIME = 2000; // ms

    static final double BACKUP_SPEED = 0.35;
    static final double TURN_SPEED = 0.25;

    int stage = NONE;
    long timestamp = System.currentTimeMillis();

    public Autonomous(DriveSubsystem drive, Shooter shooter)
    {
        this.drive = drive;
        this.shooter = shooter;
    }

    public void init()
    {
        reset();
        drive.stop();
        drive.speedDown();
        stage = NONE;
    }

    public void periodic()
    {
        String s = "NONE";
        switch (stage) {
            case NONE:
                s = "START";
                start();
                break;
            case SHOOT:
                s = "SHOOT";
                shoot();
                break;
            case BACKUP:
                s = "BACKUP";
                backup();
                break;
            case SPIN:
                s = "SPIN";
                spin();
                break;
            case DONE:
            default:
                s = "DONE";
                done();
                break;
        }
        SmartDashboard.putString("Autonomous stage", s);
        SmartDashboard.putNumber("Autonomous elapsed", elapsed());

        // Robot.LOGGER.info("Autonomous stage "+s);
    }

    void resetTimer() {
        timestamp = System.currentTimeMillis();
    }

    long elapsed() {
        return System.currentTimeMillis() - timestamp;
    }

    // this just starts the shot
    void start() {
        stage = SHOOT;
        resetTimer();
    }

    void shoot() {
        long e = elapsed();
        if (e >= SHOOT_TIME) {
            shooter.stopShoot();
            shooter.stopIntake();
            stage = BACKUP;
            resetTimer();
        }
        else {
            shooter.shoot();
            if (e > SPIN_UP_TIME) {
                shooter.intakeShoot(true);
            }
            else {
                shooter.intakeShoot(false);
            }
            if (e > SPIN_INTAKE_TIME) {
                shooter.intake();
            }
            else {
                shooter.stopIntake();
            }
        }
    }

    double initialHeading = 0;

    void backup()
    {
        if (elapsed() > BACKUP_TIME) {
            drive.stop();
            resetTimer();
            initialHeading = drive.getHeading();
            stage = SPIN;
            // done();
        }
        else {
            drive.driveStraight(BACKUP_SPEED);
        }
     }


    void spin() {
        double diff = drive.getHeading() - initialHeading;
        if (elapsed() > SPIN_TIME) {
            drive.reset();
            stage = DONE;
        }
        else if (Math.abs(diff) < SPIN_DEGREES) {
            drive.turn(TURN_SPEED * diff / 180, -1);
        }
        else {
            drive.reset();
            stage = DONE;
        }
    }

    public void stop() {
        drive.stop();
        shooter.reset();
    }

    public void done() {
        stage = DONE;
        resetTimer();
        drive.reset();
        shooter.reset();
    }

    public void reset() {
        done();
        drive.reset();
        shooter.reset();
    }

}
