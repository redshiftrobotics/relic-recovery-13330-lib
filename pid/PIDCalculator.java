package org.redshiftrobotics.lib.pid;

import com.qualcomm.robotcore.util.Range;

import org.redshiftrobotics.lib.DebugHelper;

/**
 * Created by adam on 9/16/17.
 */

public class PIDCalculator {
    public static class PIDTuning {
        public final double P;
        public final double I;
        public final double D;

        public final double MAX_I;

        public PIDTuning(double P, double I, double D) { this(P, I, D, 5); }

        public PIDTuning(double P, double I, double D, double MAX_I) {
            this.P = P;
            this.I = I;
            this.D = D;
            this.MAX_I = MAX_I;
        }
    }

    private final IMU imu;

    private PIDTuning tuning;

    // Proportional, Integral, and Derivative Terms of the PID formula
    public double P = 0, I = 0, D = 0;
    public double lastError;
    public double target;

    // Our current delta time that holds the time between current and last calculations.
    private double deltaTime;


    /**
     * Primary constructor
     *
     * @param imu Valid implementation of the IMU interface
     */
    public PIDCalculator(IMU imu) {
        this.imu = imu;
        resetTarget();
    }

    public void calculateP() {
        double angle = imu.getAngularRotationX();

        lastError = P;

        DebugHelper.addData("angle", angle);
        DebugHelper.addData("target", target);

        // We have to determine what the most efficient way to turn is (we should never turn
        // more than 180 degrees to hit a target).
        if (angle + 360 - target <= 180) {
            P = (angle -  target + 360);
        } else if (target + 360 - angle <= 180) {
            P = (target - angle + 360) * -1;
        } else if (angle -  target <= 180) {
            P = (angle -  target);
        }
    }

    public void calculateI() {
        I += P * deltaTime / 1000f;
        I = Range.clip(I, -tuning.MAX_I, tuning.MAX_I);
    }

    public void calculateD() {
        D = (P - lastError) / (deltaTime /1000f);
    }

    public double calculatePID(long deltaTime) {
        this.deltaTime = deltaTime;
        calculateP();
        calculateI();
        calculateD();

        return tuning.P * P + tuning.I * I / 2000f + tuning.D * D / 2000f;
    }

    public void setTuning(PIDTuning tuning) { this.tuning = tuning; }

    public void setTarget(double targetAngle) {
        this.target = targetAngle;
    }
    public void addTarget(double angleDelta) {
        this.target += angleDelta;
    }
    public void resetTarget() {
        this.target = this.imu.getAngularRotationX();
    }

    /**
     * Clears out all PID-associated data.
     */
    public void clearData() {
        this.P = 0;
        this.I = 0;
        this.D = 0;
        this.lastError = 0;
        this.deltaTime = 0;
    }
}


