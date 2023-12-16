package org.netislepafree.morpion_solitaire.model.algorithms;

import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

/**
 * The interface Algorithm.
 */
public interface Algorithm  {
    /**
     * Choose move line.
     *
     * @param grid the grid
     * @return the line
     */
    public Line chooseMove(Grid grid);
}
