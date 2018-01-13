package org.redshiftrobotics.lib.pid;


import com.qualcomm.robotcore.util.Range;

import org.redshiftrobotics.lib.asam.ASAMController;
import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.Vector2D;

public class StraightPIDController extends PIDController {
    private Vector2D velocity;
    private long time;
    private long tweenTime;

    public StraightPIDController(RobotHardware hw) {
        super(hw);
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        return hw.getStraightTurning();
    }

    @Override
    void applyMotorPower(double correction, long elapsedTime) {
        double tweenPower = ASAMController.computeMotorPower(time, elapsedTime, 0, (float) velocity.getXComponent(), tweenTime);
        hw.applyMotorPower(velocity.getXComponent(), tweenPower, correction);
    }

    public void moveStraight(double speed, double angle, long time) {
        moveStraight(speed, angle, time, time / 2);
    }
    public void moveStraight(double speed, double angle, long time, long tweenTime) {
        this.time = time;
        this.tweenTime = tweenTime;
        speed = Range.clip(Math.abs(speed), 0, 1);
        velocity = new Vector2D(0, 0);
        velocity.setPolar(speed, angle);

        move(time);
    }
}
