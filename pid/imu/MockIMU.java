package org.redshiftrobotics.lib.pid.imu;

public class MockIMU implements IMU {

    double[] testData;

    int currPoint = 0;

    public MockIMU(double[] mockData) {
        testData = mockData;
    }

    @Override
    public double getAngularRotationX() {
        if (currPoint < testData.length) {
            return testData[currPoint++];
        }
        return Float.POSITIVE_INFINITY;
    }

    @Override
    public double getAngularRotationY(){
        return 0;
    }

    @Override
    public double getAngularRotationZ() {
        return 0;
    }
}
