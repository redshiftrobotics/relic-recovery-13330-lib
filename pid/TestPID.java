package org.redshiftrobotics.lib.pid;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.competition.PulsarAuto;
import org.firstinspires.ftc.teamcode.lib.MecanumRobot;
import org.firstinspires.ftc.teamcode.lib.PulsarRobotHardware;

/**
 * Created by adam on 10/17/17.
 */

@Autonomous(name = "Test Pid")
@Disabled
public class TestPID extends LinearOpMode {

    MecanumRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new MecanumRobot(new PulsarRobotHardware(hardwareMap, PulsarAuto.Alliance.BLUE), this, telemetry);
      //  robot.imupidController.setTuning(0, 0, 0);
       // robot.imupidController.setTuning(1, 1, 1);

        waitForStart();

        robot.moveStraight(1,  3*Math.PI/2, 2000, 10);

        telemetry.addData("Turning... ", "");
        telemetry.update();
        robot.turn(45, 5000);
        robot.turn(90, 5000);
        robot.turn(-90, 5000);

    }
}
