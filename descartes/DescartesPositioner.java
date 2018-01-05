package org.redshiftrobotics.lib.descartes;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.redshiftrobotics.lib.Positioner;

public class DescartesPositioner implements Positioner {
    public class SputnikSensor {
        private I2cDeviceSynch ultrasonic_i2c;

        private DistanceSensor ultrasonic;
        private DistanceSensor ods;

        SputnikSensor( I2cDeviceSynch ultrasonic,  DistanceSensor ods) {
            this.ultrasonic_i2c = ultrasonic;
            this.ods = ods;

            this.ultrasonic = new UltrasonicDistanceSensor(ultrasonic);
        }

        public double getPosition() {
            if (ultrasonic == null || ultrasonic.getDistance(DistanceUnit.CM) == DistanceUnit.infinity) {
                return ods.getDistance(DistanceUnit.CM);
            } else if (ods == null || ods.getDistance(DistanceUnit.CM) == DistanceUnit.infinity) {
                return ods.getDistance(DistanceUnit.CM);
            } else {
                return (ods.getDistance(DistanceUnit.CM) + ultrasonic.getDistance(DistanceUnit.CM)) / 2;
            }
        }
    }

    private SputnikSensor x;
    private SputnikSensor y;

    public DescartesPositioner(SputnikSensor x, SputnikSensor y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void reset() {} // XXX: Remove this from Postioner interface

    public Position getPosition() {
        return new Position(DistanceUnit.CM, x.getPosition(), y.getPosition(), 0, 0);
    }
}
