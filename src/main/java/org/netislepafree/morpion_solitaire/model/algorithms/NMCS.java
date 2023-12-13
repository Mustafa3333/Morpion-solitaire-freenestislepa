package org.netislepafree.morpion_solitaire.model.algorithms;

import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;

import java.util.*;
import java.util.function.Supplier;

public class NMCS implements Algorithm {
    @Override
    public Line chooseMove(Grid grid) {
        int searchLevel = 2;
        final long maxRunningTimeMs = 1000; 

        List<Line> lines = findLinesWithinTimeLimit(grid, searchLevel, maxRunningTimeMs);
        return !lines.isEmpty() ? lines.get(0) : null;
    }

    private List<Line> findLinesWithinTimeLimit(Grid grid, int level, long maxRunningTimeMs) {
        long endTimeMs = System.currentTimeMillis() + maxRunningTimeMs;
        return search(grid, level, () -> System.currentTimeMillis() > endTimeMs);
    }

    private List<Line> search(Grid grid, int depth, final Supplier<Boolean> isCanceled) {
        if (depth < 1) {
            return grid.rollout();
        }

        List<Line> globalBestLines = new LinkedList<>();
        List<Line> visited = new LinkedList<>();

        while (!grid.possibleLines().isEmpty() && !isCanceled.get()) {
            Line curBestLine = findBestLineForCurrentGrid(grid, depth, isCanceled, globalBestLines, visited);
            if (curBestLine != null) {
                updateVisitedAndGlobalBestLines(visited, globalBestLines, curBestLine);
                grid = grid.child(curBestLine);
            }
        }

        return globalBestLines;
    }

    private Line findBestLineForCurrentGrid(Grid grid, int depth, Supplier<Boolean> isCanceled, 
                                            List<Line> globalBestLines, List<Line> visited) {
        List<Line> curBestLines = new LinkedList<>();
        Line curBestLine = null;

        for (Line line : grid.possibleLines()) {
            List<Line> rolloutLines = search(grid.child(line), depth - 1, isCanceled);
            if (curBestLines.size() < rolloutLines.size()) {
                curBestLine = line;
                curBestLines = rolloutLines;
            }
        }

        if (!curBestLines.isEmpty() && curBestLines.size() >= globalBestLines.size()) {
            return curBestLine;
        } else if (!globalBestLines.isEmpty() && visited.size() < globalBestLines.size()) {
            return globalBestLines.get(visited.size());
        }

        return null;
    }

    private void updateVisitedAndGlobalBestLines(List<Line> visited, List<Line> globalBestLines, Line curBestLine) {
        visited.add(curBestLine);
        if (visited.size() > globalBestLines.size()) {
            globalBestLines.clear();
            globalBestLines.addAll(visited);
        }
    }

}
