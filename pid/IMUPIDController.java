package org.redshiftrobotics.lib.pid;

import com.qualcomm.robotcore.util.Range;

import org.redshiftrobotics.lib.config.ConfigurationManager;

/**
 * Created by adam on 9/16/17.
 */

public class IMUPIDController {
    static boolean newIMU;
    static boolean DEBUG;

    /**
     * Instance of an IMU interface implementation which
     * will allow us to get imu data.
     */
    IMU imu;

    private ConfigurationManager conf;

    // Proportional, Integral, and Derivative Terms of the PID formula
    public float P = 0f, I = 0f, D = 0f;

    // Proportional, Integral, and Derivative Constants for Tuning
    float pConst, iConst, dConst;

    public float lastError;

    public float target;

    // Our current delta time that holds the time between current and last calculations.
    float dT;

    static float MAX_I; // The I term can never go past this value with current tunings


    /**
     * Primary constructor
     *
     * @param imu Valid implementation of the IMU interface
     */
    public IMUPIDController(IMU imu) {
        this.imu = imu;

        conf = ConfigurationManager.getSharedInstance().getConfig("pid");

        pConst = (float) conf.getDouble("pConst");
        iConst = (float) conf.getDouble("iConst");
        dConst = (float) conf.getDouble("dConst");
        MAX_I = (float) conf.getDouble("maxI");
        newIMU = conf.getBoolean("newIMU");
        DEBUG = conf.getBoolean("debug", false);
    }

    /**
     * Calculates the current imu
     */


    public void calculateP() {

        // The new IMUs return data between -180 and 180, while the old return
        // between 0 and 360.
        float angle = newIMU ? imu.getAngularRotationX() + 180: imu.getAngularRotationX();
        lastError = P;

        if (DEBUG) {
            System.out.println("Angle is " + angle);
            System.out.println("Target is " + target);
        }

        // We have to determine what the most efficient way to turn is (we should never turn
        // more than 180 degrees to hit a target).
        if (angle + 360 - target <= 180) {
            P = (angle -  target + 360);
        } else if (target + 360 - angle <= 180) {
            P = (target - angle + 360) * -1;
        } else if (angle -  target <= 180) {
            P = (angle -  target);
        }
        //System.out.println("P is" + P);
    }

    public void calculateI() {
        I += P * dT / 1000f;
        I = Range.clip(I, -MAX_I, MAX_I);
    }

    // d e(t) / dt
    public void calculateD() {
        D = (P - lastError) / (dT/1000f);
    }

    public double calculatePID(long deltaTime) {
        dT = deltaTime;
        calculateP();
        calculateI();
        calculateD();

        return pConst * P + iConst * I / 2000f + dConst * D / 2000f;
    }

    public void setTuning(float pTuning, float iTuning, float dTuning, float maxI) {
        this.pConst = pTuning;
        this.iConst = iTuning;
        this.dConst = dTuning;
        this.MAX_I = maxI;
    }

    public void setTarget(float targetAngle) {
        this.target = targetAngle;
    }

    public void addTarget(float angleDelta) {
        this.target += angleDelta;
    }

    public void subtractTarget(float angleDelta) {
        this.target -= angleDelta;
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
        this.dT = 0;
    }
}


