package org.redshiftrobotics.lib.pid;

import com.qualcomm.hardware.bosch.BNO055IMU;

/**
 * Created by adam on 9/16/17.
 */
public class IMUImpl implements IMU {
    public static double IMU_ADDER = 180;

    BNO055IMU imu;
    BNO055IMU.Parameters imuParameters;

    public IMUImpl(BNO055IMU imu) {
        imuInit(imu);
    }

    private void imuInit(BNO055IMU imu) {
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

      //  BNO055IMU  imu = new AdafruitBNO055IMU(device);
        imu.initialize(imuParameters);
        this.imu = imu;
    }

    @Override
    public double getAngularRotationX() {
        return imu.getAngularOrientation().firstAngle + IMU_ADDER;
    }

    @Override
    public double getAngularRotationY() {
        return imu.getAngularOrientation().secondAngle + IMU_ADDER;
    }

    @Override
    public double getAngularRotationZ() {
        return imu.getAngularOrientation().thirdAngle + IMU_ADDER;
    }
}
