package org.redshiftrobotics.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.redshiftrobotics.lib.encoder.EncoderPositioner;
import org.redshiftrobotics.lib.pid.FakePIDManager;
import org.redshiftrobotics.lib.pid.PIDManager;

import java.util.Date;

abstract public class AutoOpMode extends OpMode {
    static double SPEED = 50;
    static Position TOLERANCE = new Position(DistanceUnit.CM, 0.5, 0.5, 0.5, 0);
    static double TICKS_PER_CM = 1500;

    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;

    public PIDManager pid;
    public Positioner positioner;

    @Override
    public void init() {
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");

        pid = new FakePIDManager(m0, m1, m2, m3);
        positioner = new EncoderPositioner(new DoubleDcMotorEncoder(m0, m1), new DoubleDcMotorEncoder(m2, m3), TICKS_PER_CM);

    }

    private Position diff(Position a, Position b) {
        b = b.toUnit(a.unit);
        return new Position(a.unit, a.x - b.x, a.y - b.y, a.z - b.z, (new Date()).getTime());
    }

    private void driveToPosition(Position pos) {
        pid.move(diff(positioner.getPosition(), pos), SPEED, TOLERANCE);
    }

}
