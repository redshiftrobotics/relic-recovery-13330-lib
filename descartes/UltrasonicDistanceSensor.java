package org.redshiftrobotics.lib.descartes;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import static com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode.ONLY_ONCE;


import java.util.ArrayList;

/**
 * Created by adam on 10/18/17.
 */

class Timer {
    private long start = 0;

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public long end() {
        if (start == 0) return 0;
        else
            return System.currentTimeMillis() - this.start;
    }
}

@I2cSensor(name = "Maxbotix Ultrasonic Sensor", description = "A Maxbotix Ultrasonic Sensor", xmlTag = "MAXBOTICS_ULTRASONIC")
public class UltrasonicDistanceSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> implements DistanceSensor {

    private Timer timer;
    private int lastReading = 0;

    private int currentAverage = 0;

    private ArrayList<Integer> previousReadings;

    public static int SENSOR_REGISTER = 0x0;
    public static int DATA_LENGTH = 2;
    public static int WAIT_TIME = 100; //time to wait between measurements in milliseconds
//    public static int DEFAULT_ADDRESS = 0x70;
    public static int DEFAULT_ADDRESS = 112;
    public static int RANGE_CMD = 0x51;
    public static int CHANGE_ADDR_FIRST_CMD = 170;
    public static int CHANGE_ADDR_SECOND_CMD = 160;

    public static int MAX_DATA_SIZE = 25;

    public static DistanceUnit SENSOR_DISTANCE_UNIT = DistanceUnit.CM;

    private static I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(0x0, DATA_LENGTH,  ONLY_ONCE);
    private static I2cAddr distanceSensorAddress = I2cAddr.create7bit(DEFAULT_ADDRESS);

    public UltrasonicDistanceSensor(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);
        super.registerArmingStateCallback(false);
        this.deviceClient.setI2cAddress(distanceSensorAddress);
        this.deviceClient.setReadWindow(readWindow);
        this.deviceClient.engage();
        timer = new Timer();
        previousReadings = new ArrayList<>();
    }

    public void startReading() {
        if (timer.end() == 0 || timer.end() >= WAIT_TIME) {
            this.deviceClient.write8(RANGE_CMD, 0x0);
            timer.start();
        }
    }

    public int getNextReading() {
        if (timer.end() != 0 && timer.end() < WAIT_TIME) {
            //return currentAverage;
            return currentAverage;
        }

        this.deviceClient.setReadWindow(readWindow);
        byte[] readings = this.deviceClient.read(SENSOR_REGISTER, DATA_LENGTH);

        if (previousReadings.size() > MAX_DATA_SIZE) {
            previousReadings.remove(0);
        }

        int distance = (readings[0] << 8) | readings[1];
        lastReading = distance;

        previousReadings.add(distance);

        sanitizeData();

        return currentAverage;
    }

    public int getLastRawReading() {
        return this.lastReading;
    }

    public double getDistance(DistanceUnit unit, Telemetry telemetry) {
        // TODO: return DistanceUnit.infinity for out of range and below range values.
        int reading = getNextReading();
        startReading();
        if (telemetry != null) { telemetry.addData("unsanitized reading", reading); }
        return unit.fromUnit(SENSOR_DISTANCE_UNIT, reading == 0 || reading < 25 ? DistanceUnit.infinity : reading);
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        return getDistance(unit, null);
    }


    public int getUnsanitizedReading() {
        return getUnsanitizedReading(null);
    }

    public int getUnsanitizedReading(Telemetry t) {
        if (timer.end() != 0 && timer.end() < WAIT_TIME) {
            t.addData("lastReading", this.lastReading);
            t.addData("End", '-');
            t.addData("readings", '-');
            t.addData("distance", '-');
            return this.lastReading;
        }

        t.addData("lastReading", '-');

        if (t != null) {
            t.addData("End", timer.end());
        }

        this.deviceClient.setReadWindow(readWindow);
        byte[] readings = this.deviceClient.read(SENSOR_REGISTER, DATA_LENGTH);
        t.addData("readings", readings);
        t.addData("readings 1", readings[0]);
        t.addData("readings 2", readings[1]);
        t.addData("readings len", readings.length);
        int distance = (((int) readings[0]) << 8) | ((int)readings[1]);
        t.addData("distance", distance);

        if (distance < 20) distance = 0;

        lastReading = distance;

        timer.start();

        return distance;
    }


    private void sanitizeData() {
        int adjusted = 0;
        int max = previousReadings.get(0);
        int min = previousReadings.get(0);
        //boolean ignore70 = false;
        int num_ignored = 0;

        for (int reading : previousReadings) {
            if (reading > max) {
                max = reading;
            } else if (reading < min) {
                min = reading;
            }
        }

        /*if(max > 84 || min < 69){
            ignore70 = true;
        }*/


        for (int reading  : previousReadings) {
           /* if ((ignore70 && reading < 84 && reading > 69) || reading <= 0) {
                num_ignored++;
            } else {*/
                adjusted += reading;
            //}
        }
        if(num_ignored>=MAX_DATA_SIZE){
            adjusted = 0;
        }else {
            adjusted /= (MAX_DATA_SIZE - num_ignored);
        }

        currentAverage = adjusted;
    }

    // DistanceSensor Impl


    @Override
    protected boolean doInitialize() {
        return true;
    }

    @Override
    public String getConnectionInfo() { return this.deviceClient.getConnectionInfo(); }

    @Override
    public Manufacturer getManufacturer() { return Manufacturer.Other; }

    @Override
    public String getDeviceName() { return "Maxbotix Ultrasonic Sensor"; }

    @Override
    public int getVersion() { return this.deviceClient.getVersion(); }

    @Override
    public void close() { this.deviceClient.close(); }

    @Override
    public void resetDeviceConfigurationForOpMode() { this.deviceClient.resetDeviceConfigurationForOpMode(); }
}