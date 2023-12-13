package org.netislepafree.morpion_solitaire.model.algorithms;


import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

import java.util.List;

public class RandomSearchAlgorithm implements Algorithm {
    @Override
    public Line chooseMove(Grid grid) {
        List<Line> possibleLines = grid.possibleLines();
        return selectRandomLine(possibleLines);
    }

    private Line selectRandomLine(List<Line> lines) {
        if (lines.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * lines.size());
        return lines.get(randomIndex);
    }

}