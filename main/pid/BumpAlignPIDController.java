package org.redshiftrobotics.lib.pid;


import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.competition.auto.PulsarAuto;
import org.firstinspires.ftc.teamcode.lib.PulsarRobotHardware;

// Friendly reminder that the collector is the front of the robot, so the left cryptobox
// column is on our right side, the right cryptobox column is on our left side, and vis versa.
public class BumpAlignPIDController extends PIDController {
    private double forwardSpeed;
    private double strafeSpeed;
    private final PulsarRobotHardware phw;
    private final ColorSensor firstSide;
    private final ColorSensor secondSide;

    public BumpAlignPIDController(PulsarRobotHardware hw) {
        super(hw);
        phw = hw;

        // We always want to assume that we're closer to the center line, and strafe towards the
        // edges. Everything gets easier if we just figure out which sensor we're checking for first
        // beforehand.
        firstSide  = phw.alliance == PulsarAuto.Alliance.BLUE ? phw.colorSensors.rightTape : phw.colorSensors.leftTape;
        secondSide = phw.alliance == PulsarAuto.Alliance.BLUE ? phw.colorSensors.leftTape  : phw.colorSensors.rightTape;
    }

    @Override
    PIDCalculator.PIDTuning getTuning() {
        return hw.getStraightTurning();
    }

    @Override
    void applyMotorPower(double correction, long elapsedTime) {
        double powerX = phw.alliance.detectTape(secondSide) ? strafeSpeed : 0;
        double powerY = phw.alliance.detectTape(firstSide) ? forwardSpeed : 0;

        hw.applyMotorPower(powerX, powerY, correction);
    }

    @Override
    boolean predicate() {
        return super.predicate() && !(phw.alliance.detectTape(firstSide) && phw.alliance.detectTape(secondSide));
    }

    public void align(double forwardSpeed, double strafeSpeed, long timeout) {
        this.strafeSpeed = strafeSpeed;
        this.forwardSpeed = forwardSpeed;
        move(timeout);
    }
}
