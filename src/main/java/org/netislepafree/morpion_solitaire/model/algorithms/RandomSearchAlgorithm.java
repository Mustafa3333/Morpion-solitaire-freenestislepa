package org.netislepafree.morpion_solitaire.model.algorithms;


import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

import java.util.List;

public class RandomSearchAlgorithm implements Algorithm {
    @Override
    public Line chooseMove(Grid grid) {
        List<Line> possibleLines = grid.possibleLines();
        if (possibleLines.isEmpty()) return null;
        else return possibleLines.get((int) (Math.random() * possibleLines.size()));
    }
}