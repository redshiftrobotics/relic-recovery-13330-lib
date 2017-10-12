package org.redshiftrobotics.lib;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

public interface Positioner {
    Position getPosition();
    void reset();
}
