package org.redshiftrobotics.lib;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class EncoderPositioner {
    private class EncoderTracker {
        private Encoder encoder;

        private double startPos;

        double ticksPerCM;

        EncoderTracker(Encoder encoder, double ticksPerCM) {
            this.encoder = encoder;
            this.ticksPerCM = ticksPerCM;
            reset();
        }

        void reset() {
            startPos = encoder.getValue();
        }

        double getValue() {
            return encoder.getValue() - startPos;
        }

        double getPos() {
            return getValue() * ticksPerCM;
        }
    }

    EncoderTracker leftRight;
    EncoderTracker frontBack;

    public EncoderPositioner(Encoder leftRight, Encoder frontBack, double ticksPerCM) {
        this.leftRight = new EncoderTracker(leftRight, ticksPerCM);
        this.frontBack = new EncoderTracker(frontBack, ticksPerCM);
    }

    public void reset() {
        leftRight.reset();
        frontBack.reset();
    }

    public Position getPosition() {
        return new Position(DistanceUnit.CM, this.leftRight.getPos(), this.frontBack.getPos(), 0, 0);
    }

    public void setTicksPerCM(double ticksPerCM) {
        this.leftRight.ticksPerCM = ticksPerCM;
        this.frontBack.ticksPerCM = ticksPerCM;
    }
}
