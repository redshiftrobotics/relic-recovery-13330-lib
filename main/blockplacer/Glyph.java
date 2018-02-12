package org.redshiftrobotics.lib.blockplacer;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class Glyph {
    public enum GlyphColor {
        NONE, GRAY, BROWN;

        public static GlyphColor fromSensor(ColorSensor sensor) {
            return fromRGB(sensor.red(), sensor.green(), sensor.blue());
        }

        public static GlyphColor fromRGB(double r, double g, double b) {
            if (r > 150 && b < 100 && g < 100) return BROWN;
            else return GRAY;
        }

        GlyphColor invert() {
            switch (this) {
                case GRAY: return BROWN;
                case BROWN: return GRAY;
                case NONE: // fallthrough
                default: return this;
            }
        }
    }

    public final GlyphColor color;
    public final Row row;
    public final Col col;

    public Glyph(GlyphColor color, Row row, Col col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }
}
