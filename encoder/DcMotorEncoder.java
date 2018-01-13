package org.redshiftrobotics.lib.encoder;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ariporad on 2017-10-06.
 */
public class DcMotorEncoder implements Encoder {
    private DcMotor motor;

    public DcMotorEncoder(DcMotor motor) {
        this.motor = motor;
    }

    @Override
    public double getValue() { return motor.getCurrentPosition(); }
}
