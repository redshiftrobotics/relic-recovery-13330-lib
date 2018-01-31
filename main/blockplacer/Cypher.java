package org.redshiftrobotics.lib.blockplacer;

import static org.redshiftrobotics.lib.blockplacer.Glyph.GlyphColor.GRAY;
import static org.redshiftrobotics.lib.blockplacer.Glyph.GlyphColor.BROWN;

enum Cypher {

    // NOTE: These are upside-down because the first row will be row 0, which should be the bottom row.

    FROG(new Glyph.GlyphColor[][] {
            { BROWN, GRAY, BROWN },
            { GRAY, BROWN, GRAY },
            { BROWN, GRAY, BROWN },
            { GRAY, BROWN, GRAY }
    }),

    BIRD(new Glyph.GlyphColor[][] {
            { GRAY, BROWN, GRAY },
            { BROWN, GRAY, BROWN },
            { BROWN, GRAY, BROWN },
            { GRAY, BROWN, GRAY },
    }),

    SNAKE(new Glyph.GlyphColor[][] {
            { GRAY, GRAY, BROWN },
            { GRAY, BROWN, BROWN },
            { BROWN, BROWN, GRAY },
            { BROWN, GRAY, GRAY }
    }),

    INV_FROG(invert(Cypher.FROG)),
    INV_BIRD(invert(Cypher.BIRD)),
    INV_SNAKE(invert(Cypher.SNAKE));

    Glyph.GlyphColor[][] pattern;

    static Glyph.GlyphColor[][] invert(Cypher alt) {
        Glyph.GlyphColor[][] us = new Glyph.GlyphColor[4][3];

        for (int i = 0; i < alt.pattern.length; i++) {
            for (int j = 0; j < alt.pattern[i].length; j++) {
                us[i][j] = alt.pattern[i][j].invert();
            }
        }

        return us;
    }

    Cypher(Glyph.GlyphColor[][] pattern) {
        this.pattern = pattern;
    }
}
