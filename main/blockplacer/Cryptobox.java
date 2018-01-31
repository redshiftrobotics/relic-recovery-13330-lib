package org.redshiftrobotics.lib.blockplacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.redshiftrobotics.lib.blockplacer.Col.*;
import static org.redshiftrobotics.lib.blockplacer.Cypher.*;

public class Cryptobox {
    /**
     * The list of possible cyphers. Technically, the system will prefer cyphers lower in the list.
     */
    private final Cypher[] cyphers = new Cypher[]{FROG, INV_FROG, BIRD, INV_BIRD, SNAKE, INV_SNAKE};
    private int currentCypher = 0; // The current cypher. We increment this when a cypher becomes impossible

    public Cypher getCurrentCypher() {
        return cyphers[currentCypher];
    }

    /**
     * Col placement preference with index 0 being highest preference.
     */
    private Col[] colPreferences = new Col[]{CENTER, RIGHT, LEFT};

    public void setColPreferences(Col[] newColPreference) {
        colPreferences = newColPreference;
    }

    /**
     * Data about blocks we have already placed.
     */
    Set<Glyph> glyphs = new HashSet<>(12);

    /**
     * The "thinking" function of the AI. Call this when a block has been scanned to determine drive position.
     *
     * @param nextGlyphColor the color of the next block to be placed.
     * @return the position in which to place the block.
     */
    public Col getNextBlock(Glyph.GlyphColor nextGlyphColor, boolean dryRun) {
        if (glyphs.size() >= 12) throw new IllegalStateException("Cannot have more than 12 Glyphs in a Cryptobox!");

        ArrayList<Col> viableCols = new ArrayList<>(getViableColumns(nextGlyphColor));
        final int[] colHeights = {0, 0, 0};

        for (Glyph glyph : glyphs) {
            colHeights[glyph.col.index]++;
        }

        Collections.sort(viableCols, new Comparator<Col>() {
            @Override
            public int compare(Col lhs, Col rhs) {
                return rankColumn(lhs, colHeights[lhs.index]) - rankColumn(rhs, colHeights[rhs.index]);
            }
        });


        if (viableCols.isEmpty()) {
            currentCypher++;
            Col ret = NONE;
            if (currentCypher != cyphers.length) { // If there are more cyphers to try
                ret = getNextBlock(nextGlyphColor, dryRun);
            }
            if (dryRun) currentCypher--;
            return ret;
        }

        Col col = viableCols.get(0);

        if (!dryRun) {
            // We get the row in a somewhat clever fashion: the index of the next glyph is equal to
            // the current height of the target column
            glyphs.add(new Glyph(nextGlyphColor, Row.fromIndex(colHeights[col.index]), col));
        }

        return col;

    }

    /**
     * @return the colPreferences in which a block can be placed that lead to a valid CryptoKey.
     */
    private Collection<Col> getViableColumns(Glyph.GlyphColor blockColor) {
        Glyph[] topmostBlocks = new Glyph[]{null, null, null}; // indexed by column

        // Loop through placed blocks to find topmost blocks.
        for (Glyph glyph : glyphs) {
            if (topmostBlocks[glyph.col.index] == null || topmostBlocks[glyph.col.index].row.index < glyph.row.index) {
                topmostBlocks[glyph.col.index] = glyph;
            }
        }

        Set<Col> viableColumns = new HashSet<>();

        for (int i = 0; i < topmostBlocks.length; i++) {
            Glyph topmostBlock = topmostBlocks[i];

            Row nextRow = topmostBlock == null ? Row.BOTTOM : topmostBlock.row.nextRow();

            // If placing the glyph in this column would lead to a cypher
            if (getCurrentCypher().pattern[nextRow.index][i] == blockColor) {
                viableColumns.add(Col.fromIndex(i));
            }
        }

        return viableColumns;
    }

    private int rankColumn(Col col, int height) {
        int rank = 10 - indexOf(colPreferences, col); // lower indexes are ranked higher

        switch (height) {
            case 2:
                rank += 50;
                break;
            case 0:
                rank += 40;
                break;
            case 1:
                rank += 30;
                break;
            case 3:
                rank += 20;
                break;
            default:
                rank += 0;
                break;
        }

        return rank;
    }


    /**
     * Optionally, one can create a Cryptobox without a (pre-loaded) glyph already in it.
     */
    public Cryptobox() {}

    /**
     * Public Constructor
     */
    public Cryptobox(Glyph.GlyphColor firstGlyph, Col cryptoKey) {
        glyphs.add(new Glyph(firstGlyph, Row.BOTTOM, cryptoKey));
    }

    /**
     * Utilities
     */
    private <T> int indexOf(T[] arr, T item) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == item) return i;
        }
        return -1;
    }
}
