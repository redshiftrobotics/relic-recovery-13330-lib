package org.redshiftrobotics.lib.pid;

import android.util.Log;

import org.redshiftrobotics.lib.RobotHardware;

public class TurningPIDController extends PIDController {
    private static double powerConstant = 0.5;
    private double powerScalar = 1;

    public TurningPIDController(RobotHardware hw) {
        super(hw);
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        return hw.getTurningTuning();
    }

    public void turn(double angle, long time){
        turn(angle, time, 1);
    }
    public void turn(double angle, long time, double powerScalar) {
        this.powerScalar = powerScalar;
        pidCalculator.addTarget(angle);

        move(time);
    }

    public void turnToTarget(double target, long time) {
        turnToTarget(target, time, 1);
    }

    public void turnToTarget(double target, long time, double powerScalar) {
        this.powerScalar = powerScalar;
        pidCalculator.setTarget(target);

        move(time);
    }

    @Override
    void applyMotorPower(double correction, long elapsedTime) {
        hw.applyMotorPower(0, 0, (powerConstant + correction) * powerScalar);
    }

    @Override
    boolean predicate() {
        return !(
                withinThreshold(pidCalculator.P, hw.getTurningAngleThreshold()) &&
                withinThreshold(pidCalculator.lastError, hw.getTurningAngleThreshold())
        );
    }

    // Helper Method
    protected boolean withinThreshold(double value, double tolerance) {
        return Math.abs(value) <= tolerance;
    }
}
