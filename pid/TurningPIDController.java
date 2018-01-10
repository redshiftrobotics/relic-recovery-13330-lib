package org.redshiftrobotics.lib.pid;

import org.redshiftrobotics.lib.RobotHardware;

public class TurningPIDController extends PIDController {
    private double powerConstant;

    public TurningPIDController(RobotHardware hw) {
        super(hw);
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        return hw.getTurningTuning();
    }

    public void turn(double angle, long time){
        turn(angle, time, 0.5);
    }
    public void turn(double angle, long time, double powerConstant) {
        this.powerConstant = powerConstant;
        pidCalculator.addTarget(angle);

        move(time);
    }

    @Override
    void applyMotorPower(double correction, long elapsedTime) {
        hw.applyMotorPower(0, 0, powerConstant + correction);
    }

    @Override
    boolean predicate() {
        return (
                withinThreshold(pidCalculator.P, hw.getTurningAngleThreshold()) &&
                withinThreshold(pidCalculator.lastError, hw.getTurningAngleThreshold())
        );
    }

    // Helper Method
    protected boolean withinThreshold(double value, double tolerance) {
        return Math.abs(value) <= tolerance;
    }
}
