package org.redshiftrobotics.lib.pid.imu;

import com.qualcomm.hardware.bosch.BNO055IMU;

public class IMUWrapper implements IMU {
    private static double IMU_ADDER = 180;

    public BNO055IMU imu;
    public BNO055IMU.Parameters imuParameters;

    public IMUWrapper(BNO055IMU imu) {
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

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
