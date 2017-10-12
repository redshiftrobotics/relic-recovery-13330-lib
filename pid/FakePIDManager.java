package org.redshiftrobotics.lib.pid;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

/**
 * Created by ariporad on 2017-10-11.
 */

public class FakePIDManager implements PIDManager {
    public FakePIDManager(DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3) {

    }

    @Override
    public void move(Position position, double speed, Position tolerance) {

    }

    @Override
    public void turn(double degrees) {

    }
}
