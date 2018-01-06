package org.redshiftrobotics.lib.pid;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.competition.PulsarAuto;
import org.firstinspires.ftc.teamcode.lib.MecanumRobot;
import org.firstinspires.ftc.teamcode.lib.PulsarRobotHardware;

/**
 * Created by adam on 10/25/17.
 */
public class TestEncoderForward extends LinearOpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;
    MecanumRobot robot;
    BNO055IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new MecanumRobot(new PulsarRobotHardware(hardwareMap, PulsarAuto.Alliance.BLUE), this, telemetry);
        robot.PIDCalculator.setTuning(1, 1, 1);

        waitForStart();

        // Forward Ten CM
        robot.moveStraight(1, Math.PI/2, 10000, 10);
        robot.STOP();

        Thread.sleep(1000);

        robot.moveStraight(1, -Math.PI/2, 10000, -10);

     /*   telemetry.addData("Turning... ", "");
        telemetry.update();
        Thread.sleep(5000);
        robot.turn(45, 5000);*/
    }
}
