package org.redshiftrobotics.lib.vuforia;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.redshiftrobotics.lib.RobotHardware;
import org.redshiftrobotics.lib.blockplacer.Col;

public class VuforiaController {
    private final VuforiaLocalizer.Parameters parameters;
    private final VuforiaLocalizer vuforia;
    private final VuforiaTrackable relicTemplate;
    private final VuforiaTrackables relicTrackables;

    public VuforiaController(RobotHardware hw) { this(hw, VuforiaLocalizer.CameraDirection.BACK); }

    public VuforiaController(RobotHardware hw, VuforiaLocalizer.CameraDirection cameraDirection) {
        int cameraMonitorViewId = hw.getAppContext().getResources().getIdentifier("cameraMonitorViewId", "id", hw.getAppContext().getPackageName());

        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        //This is our key for vuforia
        parameters.vuforiaLicenseKey = "AZSn6x3/////AAAAGQUiAVV7BUM5p1/oUpgt2zd2gpH6mH3RDbbzWwc6oPE80fZ61JSft68k7bnar35QeFYAffqqC4lASNO+ufDo3YkAAmrqm7xttuFSQCwStUUwxj6smqRehkzjIG9Ud/qMUKwtZ477dal9IayK0S/meM6t8xQpLOfGpFesBjXBxqaO092Uz3ab+O+Y3px+tSwo+w7NTqDKy6QhJnju6vyqLN10tXhzAYCdsl0tPmNoYfieelsQNAfQrTO0onkzGrvJXsSF+J+eVbwVUtdn1+SK2MWyVQHks/aXvin929RYaMTgxiAz6GwmKOHR5/S4XarDBz48mKGSnxB00OOg8QxFSWkKPsHen5b9ZQpVFwcqdzz0";

        parameters.cameraDirection = cameraDirection;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /*
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    }

    public Col detectColumn() {
        relicTrackables.activate();
        Log.d("RedshiftVuforiaSaw", RelicRecoveryVuMark.from(relicTemplate).toString());
        return Col.fromVuMark(RelicRecoveryVuMark.from(relicTemplate));
    }
}
