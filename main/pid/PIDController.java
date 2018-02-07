package org.redshiftrobotics.lib.pid;

import org.redshiftrobotics.lib.debug.DebugHelper;
import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.pid.imu.IMU;
import org.redshiftrobotics.lib.pid.imu.IMUWrapper;

public abstract class PIDController {
    public static PIDCalculator pidCalculator;

    abstract PIDCalculator.PIDTuning getTuning();
    abstract void applyMotorPower(double correction, long elapsedTime);
    boolean predicate() { return true; }

    protected final RobotHardware hw;

    private PIDCalculator.PIDTuning tuningOverride;

    public void setTuningOverride(PIDCalculator.PIDTuning tuning) {
        tuningOverride = tuning;
    }

    public PIDCalculator.PIDTuning getTuningOverride() {
        return tuningOverride;
    }

    public PIDController(RobotHardware hw) {
        this.hw = hw;

        if (pidCalculator == null) pidCalculator = new PIDCalculator(hw.getIMU());
    }

    protected void move(long time) {
        DebugHelper.sleep(2000);

        pidCalculator.setTuning(tuningOverride != null ? tuningOverride : getTuning());
        pidCalculator.clearData();

        long elapsedTime = 0;
        long startTime = System.currentTimeMillis();
        long loopTime = System.currentTimeMillis();

        do {
            double correction = pidCalculator.calculatePID((double)loopTime/1000);

            DebugHelper.addData("Correction", correction);
            DebugHelper.addData("P", pidCalculator.P);
            DebugHelper.addData("I", pidCalculator.I);
            DebugHelper.addData("D", pidCalculator.D);

            applyMotorPower(correction, elapsedTime);

            long currSysTime = System.currentTimeMillis();
            elapsedTime = currSysTime - startTime;
            loopTime = currSysTime - loopTime;

            DebugHelper.update();
        } while (elapsedTime <= time && hw.getOpMode().opModeIsActive() && predicate());

        hw.stop();
    }
}
