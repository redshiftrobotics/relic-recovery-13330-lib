package org.redshiftrobotics.lib.pid;


import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.Vector2D;
import org.redshiftrobotics.lib.asam.ASAMController;

abstract class StraightPIDController extends PIDController {
    protected Vector2D velocity;
    protected long time;
    protected long tweenTime;

    StraightPIDController(RobotHardware hw) {
        super(hw);
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        return hw.getStraightTurning();
    }

    @Override
    void applyMotorPower(double correction, long elapsedTime) {
        // To make the ASAM algorithm simpler, we abs the input, then re-add the sign to the output.
        double tweenPowerX = Math.signum(velocity.getXComponent()) * ASAMController.computeMotorPower(time, elapsedTime, 0, (float) Math.abs(velocity.getXComponent()), tweenTime);
        double tweenPowerY = Math.signum(velocity.getYComponent()) * ASAMController.computeMotorPower(time, elapsedTime, 0, (float) Math.abs(velocity.getYComponent()), tweenTime);
        hw.applyMotorPower(tweenPowerX, tweenPowerY, correction);
    }

    // XXX: Strictly speaking, these don't really belong here, but they make everything else much
    //      DRYer
    public void move(double speed, long time) {
        move(speed, time, time / 2);
    }
    abstract public void move(double speed, long time, long tweenTime);

    protected void move(long time, long tweenTime, Vector2D velocity) {
        this.time = time;
        this.tweenTime = tweenTime;
        this.velocity = velocity;

        move(time);
    }
}
