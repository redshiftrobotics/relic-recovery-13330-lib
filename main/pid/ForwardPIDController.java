package org.redshiftrobotics.lib.pid;


import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.Vector2D;

public class ForwardPIDController extends StraightPIDController {
    public ForwardPIDController(RobotHardware hw) {
        super(hw);
    }

    @Override
    public void move(double speed, long time, long tweenTime) {
        move(time, tweenTime, new Vector2D(0, speed));
    }
}
