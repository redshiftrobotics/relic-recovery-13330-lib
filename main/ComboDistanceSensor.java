package org.redshiftrobotics.lib;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class ComboDistanceSensor implements DistanceSensor {
    DistanceSensor a;
    DistanceSensor b;

    public ComboDistanceSensor(DistanceSensor a, DistanceSensor b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        a.resetDeviceConfigurationForOpMode();
        b.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        a.close();
        b.close();
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        double aDistance = a.getDistance(unit);

        return aDistance == DistanceUnit.infinity || Double.isInfinite(aDistance)  || Double.isNaN(aDistance) || aDistance == 0 ? b.getDistance(unit) : aDistance;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String getDeviceName() {
        return "Combo Distance Sensor";
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }
}
