package org.redshiftrobotics.lib.blockplacer;

public enum Col {
    NONE(-1), LEFT(0), CENTER(1), RIGHT(2);

    public final int index;

    Col(int index) {
        this.index = index;
    }

    static Col fromIndex(int index) {
        switch (index) {
            case 0: return LEFT;
            case 1: return CENTER;
            case 2: return RIGHT;
            default: return NONE;
        }
    }
}
