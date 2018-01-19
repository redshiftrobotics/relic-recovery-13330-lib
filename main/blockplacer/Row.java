package org.redshiftrobotics.lib.blockplacer;

public enum Row {
    TOP(3),
    MIDDLE_TOP(2),
    MIDDLE_BOTTOM(1),
    BOTTOM(0),
    NONE(-1);

    public final int index;

    Row(int index) {
        this.index = index;
    }

    public static Row fromIndex(int index) {
        switch (index) {
            case 0: return BOTTOM;
            case 1: return MIDDLE_BOTTOM;
            case 2: return MIDDLE_TOP;
            case 3: return TOP;

            default: case -1: return NONE;
        }
    }

    public Row nextRow() {
        return Row.fromIndex(this.index + 1);
    }
}
