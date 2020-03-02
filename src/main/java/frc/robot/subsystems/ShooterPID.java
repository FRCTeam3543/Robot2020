package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

class ShooterPID extends PIDSubsystem {

    final private InputProvider input;
    final private OutputTarget output;

    ShooterPID(String name, PIDF tunings, InputProvider input, OutputTarget output) {
        super(name, tunings.kP, tunings.kI, tunings.kD, tunings.kF);
        this.input = input;
        this.output = output;
    }

    @Override
    protected double returnPIDInput() {
        return input.get();
    }

    @Override
    protected void usePIDOutput(double outputValue) {
        output.set(outputValue);
    }

    @Override
    protected void initDefaultCommand() {
        // empty on purpose
    }

    @FunctionalInterface
    interface InputProvider {
        double get();
    }

    @FunctionalInterface
    interface OutputTarget {
        void set(double val);
    }

    static class PIDF {
        final double kP;
        final double kI;
        final double kD;
        final double kF;
        PIDF(double kP, double kI, double kD, double kF) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kF = kF;
        }
    }
}