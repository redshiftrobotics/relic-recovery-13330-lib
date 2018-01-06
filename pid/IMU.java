package org.redshiftrobotics.lib.pid;

/**
 * Created by adam on 9/16/17.
 */
public interface IMU {
    double getAngularRotationX();
    double getAngularRotationY();
    double getAngularRotationZ();
}
