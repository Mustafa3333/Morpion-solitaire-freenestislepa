package org.netislepafree.morpion_solitaire.model.algorithms;

import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

import java.util.*;
import java.util.function.Supplier;

public class NMCS implements Algorithm {
    @Override
    public Line chooseMove(Grid grid) throws NullPointerException {
        int level = 2;
        List<Line> lines;
        final long maxRunningTimeMs = 1000; // Temps maximal d'exécution en millisecondes
        final long endTimeMs = System.currentTimeMillis() + maxRunningTimeMs;
        lines = search(grid, level, () -> System.currentTimeMillis() > endTimeMs);

        return lines.size() > 0 ? lines.get(0) : null;
    }

    private List<Line> search(Grid grid, int depth, final Supplier<Boolean> isCanceled) {
        if (depth < 1)
            return grid.rollout();

        List<Line> globalBestLines = new LinkedList<>();

        List<Line> visited = new LinkedList<>();

        while (!grid.possibleLines().isEmpty() && !isCanceled.get()) {

            List<Line> curBestLines = new LinkedList<>();
            Line curBestLine = null;

            List<Line> possibleLines = grid.possibleLines();
            for (Line line : possibleLines) {
                List<Line> rolloutLines = search(grid.child(line), depth - 1, isCanceled);
                if (curBestLines.size() < rolloutLines.size()) {
                    curBestLine = line;
                    curBestLines = rolloutLines;
                }
            }

            if (curBestLines.size() < globalBestLines.size()) {
                curBestLine = globalBestLines.get(visited.size());
                visited.add(curBestLine);
            } else {
                visited.add(curBestLine);
                globalBestLines = curBestLines;
                globalBestLines.addAll(0, visited);
            }


            grid = grid.child(curBestLine);

        }
        return globalBestLines;
    }
}
