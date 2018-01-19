package org.redshiftrobotics.lib;

import android.content.Context;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.redshiftrobotics.lib.pid.PIDCalculator;

public interface RobotHardware {
    BNO055IMU getIMU();
    Context getAppContext();
    LinearOpMode getOpMode();

    void applyMotorPower(double velocityX, double velocityY, double correctionAngular);
    void stop();

    PIDCalculator.PIDTuning getStraightTurning();
    PIDCalculator.PIDTuning getTurningTuning();
}

