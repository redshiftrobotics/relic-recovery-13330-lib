package org.redshiftrobotics.lib.blockplacer;

public class Glyph {
    public enum GlyphColor {
        NONE, GRAY, BROWN;

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
