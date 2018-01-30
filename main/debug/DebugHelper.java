package org.redshiftrobotics.lib.debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DebugHelper {
    // WARNING: DO NOT change this value! If you want to enable debugging, use the Debugger OpMode!
    private static boolean enabled = false;
    private static Telemetry telemetry;

    public static boolean isEnabled() { return enabled; }
    public static void enable() { enabled = true; }
    public static void disable() { enabled = false; }

    public static void setTelemetry(Telemetry tm) { telemetry = tm; }

    public static void sleep(long time) {
        if (!enabled) return;
        try {
            Thread.sleep(time); // XXX: Linear OpMode Sleep?
        } catch (InterruptedException e) {
            // Ignore It
        }
    }

    public static void addLine() { addLine(""); }
    public static void addLine(String line) {
        if (!enabled || telemetry == null) return;
        telemetry.addLine(line);
    }

    public static void addData(String caption, String data) {
        if (!enabled || telemetry == null) return;
        telemetry.addData(caption, data);
    }

    public static void addData(String caption, int data) {
        if (!enabled || telemetry == null) return;
        telemetry.addData(caption, data);
    }

    public static void addData(String caption, long data) {
        if (!enabled || telemetry == null) return;
        telemetry.addData(caption, data);
    }

    public static void addData(String caption, double data) {
        if (!enabled || telemetry == null) return;
        telemetry.addData(caption, data);
    }

    public static void addData(String caption, Object... args) {
        if (!enabled || telemetry == null) return;
        telemetry.addData(caption, args);
    }

    public static void update() {
        if (!enabled || telemetry == null) return;
        telemetry.update();
    }

    public static boolean getEnabled() {
        return enabled;
    }
}
