package org.redshiftrobotics.lib;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ariporad on 2017-10-06.
 */
public class DcMotorEncoder implements Encoder {
    private DcMotor motor;

    public DcMotorEncoder(DcMotor motor) {
        this.motor = motor;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setPower(0);
    }

    @Override
    public double getValue() { return motor.getCurrentPosition(); }
}
