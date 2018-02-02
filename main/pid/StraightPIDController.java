package org.redshiftrobotics.lib.pid;


import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.Vector2D;
import org.redshiftrobotics.lib.asam.ASAMController;

public class StraightPIDController extends PIDController {
    protected Vector2D velocity;
    protected long time;
    protected long tweenTime;

    public StraightPIDController(RobotHardware hw) {
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

    public void move(double speed, long time) {
        move(speed, time, time / 2);
    }
    public void move(double speed, long time, long tweenTime) {
        move(time, tweenTime, new Vector2D(0, speed));
    }

    public void strafe(double speed, long time) {
        strafe(speed, time, time / 2);
    }
    public void strafe(double speed, long time, long tweenTime) {
        move(time, tweenTime, new Vector2D(speed, 0));
    }

    public void move(long time, long tweenTime, Vector2D velocity) {
        this.time = time;
        this.tweenTime = tweenTime;
        this.velocity = velocity;

        move(time);
    }
}
