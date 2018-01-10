package org.redshiftrobotics.lib.pid;

import org.redshiftrobotics.lib.debug.DebugHelper;
import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.pid.imu.IMU;
import org.redshiftrobotics.lib.pid.imu.IMUWrapper;

public abstract class PIDController {
    abstract PIDCalculator.PIDTuning getTuning();
    abstract void applyMotorPower(double correction, long elapsedTime);
    boolean predicate() { return true; }

    private final IMU imu;
    protected final RobotHardware hw;
    protected final PIDCalculator pidCalculator;

    public PIDController(RobotHardware hw) {
        this.hw = hw;

        imu = new IMUWrapper(hw.getIMU());
        pidCalculator = new PIDCalculator(imu);
    }

    protected void move(long time) {
        pidCalculator.setTuning(getTuning());
        pidCalculator.clearData();

        long elapsedTime = 0;
        long startTime = System.currentTimeMillis();
        long loopTime = System.currentTimeMillis();

        do {
            double correction = pidCalculator.calculatePID(loopTime/1000);

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
