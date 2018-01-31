package org.redshiftrobotics.lib.pid;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.lib.PulsarRobotHardware;

@TeleOp(name="PID Tuner")
public class PIDTuner extends LinearOpMode {

    private PulsarRobotHardware hw;
    private TurningPIDController turningPIDController;
    private StraightPIDController straightPIDController;
    private PIDController pidController;

    private static final double TRIGGER_THRESHOLD = 0.75;

    private boolean lastB = false, lastX = false, lastY = false, lastA = false, lastLT = false, lastRT = false;

    private static final double P_STEP = 5;
    private static final double I_STEP = 1e-4;
    private static final double D_STEP = 1e-3;

    @Override
    public void runOpMode() {
        final PulsarRobotHardware hw = new PulsarRobotHardware(this, null);
        final StraightPIDController straightPIDController = new ForwardPIDController(hw);
        final TurningPIDController turningPIDController = new TurningPIDController(hw);

        PIDController pidController = straightPIDController;

        // If we don't do this, then .getOverrideTuning() returns null. This allows us to be dumb about it later
        straightPIDController.setTuningOverride(hw.getStraightTurning());
        turningPIDController.setTuningOverride(hw.getTurningTuning());

        telemetry.addLine("Welcome to the PID Tuner!");
        telemetry.addLine("Press START to Begin!");
        waitForStart();

        while (opModeIsActive()) {

            final boolean LT = gamepad1.left_trigger > TRIGGER_THRESHOLD;
            final boolean RT = gamepad1.right_trigger > TRIGGER_THRESHOLD;

            if (gamepad1.a && !lastA) {
                pidController = (pidController == turningPIDController) ? straightPIDController : turningPIDController;
            }

            PIDCalculator.PIDTuning tuning = pidController.getTuningOverride();
            double P = tuning.P;
            double I = tuning.I;
            double D = tuning.D;
            double increment = gamepad1.dpad_down ? -1 : 1;

            telemetry.addData("P", tuning.P);
            telemetry.addData("I", tuning.I);
            telemetry.addData("D", tuning.D);
            telemetry.addData("angle", pidController.pidCalculator.angle);
            telemetry.addData("target", pidController.pidCalculator.target);


            if (gamepad1.b && !lastB) P += increment * P_STEP;
            if (gamepad1.x && !lastX) I += increment * I_STEP;
            if (gamepad1.y && !lastY) D += increment * D_STEP;

            pidController.setTuningOverride(new PIDCalculator.PIDTuning(P, I, D));

            if (gamepad1.dpad_up) {
                telemetry.addLine("Clearing Data and Resetting Target");
                pidController.pidCalculator.clearData();
                pidController.pidCalculator.resetTarget();
            }

            if (LT && !lastLT) {
                telemetry.addLine("Turning 90deg, with a 5s timeout");
                turningPIDController.turn(90, 5000);
            }
            if (RT && !lastRT) {
                telemetry.addLine("Moving Forward for 5s at Full Speed");
                straightPIDController.move(1, 5000);
            }

            lastA = gamepad1.a;
            lastB = gamepad1.b;
            lastX = gamepad1.x;
            lastY = gamepad1.y;
            lastLT = LT;
            lastRT = RT;

            sendInstructions();
            telemetry.update();
            idle();
        }
    }

    private void sendInstructions() {
        telemetry.addLine("Welcome to the PID Tuner! Instructions:");
        telemetry.addLine("[A] Toggle between tuning Straight and Turning");
        telemetry.addLine("[B] Adjust P");
        telemetry.addLine("[X] Adjust I");
        telemetry.addLine("[Y] Adjust D");
        telemetry.addLine("[D-UP] Clear Data & Reset Target");
        telemetry.addLine("[D-DOWN] Decrement Instead of Incrementing");
        telemetry.addLine("[LT] Turn 90deg");
        telemetry.addLine("[RT] Move Straight");
        telemetry.update();
    }
}
