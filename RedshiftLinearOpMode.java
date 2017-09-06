package org.redshiftrobotics.lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class RedshiftLinearOpMode extends LinearOpMode {
    // At least if no one presses a button, we'll be right 50% of the time
    private Alliance selectedAlliance = Alliance.BLUE;

    abstract void initialization() throws InterruptedException;
    abstract void run(Alliance alliance) throws InterruptedException;

    @Override
    final public void runOpMode() throws InterruptedException {
        initialization();

        if (gamepad1.a) { // Blue
            selectedAlliance = Alliance.BLUE;
        } else if (gamepad1.b) { // Red
            selectedAlliance = Alliance.RED;
        }

        waitForStart();

        run(selectedAlliance);
    }
}
