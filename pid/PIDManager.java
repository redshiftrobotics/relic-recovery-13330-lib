package org.redshiftrobotics.lib.pid;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

/**
 * Created by ariporad on 2017-10-11.
 */

public interface PIDManager {
    void move(Position position, double speed, Position tolerance);
    void turn(double degrees);
}
