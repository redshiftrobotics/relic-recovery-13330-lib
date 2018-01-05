package org.redshiftrobotics.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.redshiftrobotics.lib.encoder.Encoder;

/**
 * Created by ariporad on 2017-10-06.
 */
public class DoubleDcMotorEncoder implements Encoder {
    private DcMotor m1;
    private DcMotor m2;

    public DoubleDcMotorEncoder(DcMotor m1, DcMotor m2) {
        this.m1 = m1;
        this.m2 = m2;
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m1.setPower(0);
        m2.setPower(0);
    }

    // Because Java is a shitty language
    private double average(double a, double b) {
        return (a + b) / 2;
    }

    @Override
    public double getValue() { return average(m1.getCurrentPosition(), m2.getCurrentPosition()); }
}
