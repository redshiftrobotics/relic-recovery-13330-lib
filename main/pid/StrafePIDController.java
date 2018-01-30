package org.redshiftrobotics.lib.pid;


import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.Vector2D;

public class StrafePIDController extends StraightPIDController {
    public StrafePIDController(RobotHardware hw) {
        super(hw);
        if (!hw.getCanStrafe()) throw new IllegalArgumentException("RobotHardware can't Strafe!");
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        PIDCalculator.PIDTuning strafeTuning = hw.getStrafeTuning();
        if (strafeTuning != null) return strafeTuning;
        return super.getTuning();
    }

    public void move(double speed, long time, long tweenTime) {
        move(time, tweenTime, new Vector2D(speed, 0));
    }
}
