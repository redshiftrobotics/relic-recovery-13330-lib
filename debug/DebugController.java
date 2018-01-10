package org.redshiftrobotics.lib.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Debugger", group = "Debug")
public class DebugController extends OpMode {
    @Override
    public void init() {
        telemetry.addLine("Welcome to the Debugger!");
        telemetry.addLine();
        telemetry.addLine("Press START to begin.");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.a) DebugHelper.enable();
        if (gamepad1.b) DebugHelper.disable();

        telemetry.addLine("Welcome to the Debugger!");
        telemetry.addLine();
        telemetry.addLine("Press [A] to enable debugging, or press [B] to disable it.");
        telemetry.addLine("(NOTE: This will be reset if the Robot Controller is restarted.)");
        telemetry.addData("Debugging is currently", DebugHelper.isEnabled() ? "Enabled" : "Disabled");
        telemetry.update();
    }
}
