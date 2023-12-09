package org.netislepafree.morpion_solitaire.model.algorithms;

import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

public interface Algorithm  {
    public Line chooseMove(Grid grid);
}
