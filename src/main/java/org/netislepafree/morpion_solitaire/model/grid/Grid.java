package org.netislepafree.morpion_solitaire.model.grid;

import org.netislepafree.morpion_solitaire.model.algorithms.RandomSearchAlgorithm;

import java.io.Serializable;
import java.util.*;


public class Grid implements Serializable {
    private final Point[][] grid;
    private final Set<Point> points;
    private List<Point> highlightedPoints=new ArrayList<>();

    public List<Point> getHighlightedPoints() {
        return highlightedPoints;
    }

    public void setHighlightedPoints(List<Point> highlightedPoints) {
        this.highlightedPoints = highlightedPoints;
    }

    public Set<Point> getPoints() {
        return Collections.unmodifiableSet(points);
    }
    private final List<Line> lines;
    private Mode mode;
    private int width;
    private int height;

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public Grid(int width, int height, Mode mode) {
        this.width=width;
        this.height=height;
        this.points= new HashSet<>();
        this.grid = new Point[width][height];
        this.lines = new ArrayList<>();
        this.mode = mode;
    }
    public void addPoint(int x, int y) {
        if (grid[x][y] == null) {
            grid[x][y] = new Point(x, y);
            points.add(grid[x][y]);
        }
    }
    public void addLine(Line line) {
        line.setNumber(lines.size() + 1);
        line.getPoints().forEach(point -> {
            if (grid[point.x][point.y] == null) {
                addPoint(point.x, point.y);
                line.setNewPoint(grid[point.x][point.y]);
            }
            grid[point.x][point.y].lock(line.getDirection());
        });
        lines.add(line);
    }

    public void init() {
        int startX = (width - 10) / 2;
        int startY = (height - 10) / 2;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i < 3 && (j < 3 || j > 6)) continue;
                if (i > 6 && (j < 3 || j > 6)) continue;
                if (i > 0 && i < 9 && j > 3 && j < 6) continue;
                if (j > 0 && j < 9 && i > 3 && i < 6) continue;
                addPoint(startX + i, startY + j);
            }
        }
    }
// Check que quand on add un point ca forme une ligne ou pas (dans toutes les directions)
    public List<Line> findLines(int x, int y) {
        if (!isValidX(x) || !isValidY(y) || grid[x][y] != null) {
            return new ArrayList<>();
        }
        ArrayList<Line> possibleLines = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            possibleLines.addAll(findLines(x, y, direction));
        }
        return possibleLines;
    }
    public Mode getMode() {
		return this.mode;
	}

	// Check que quand on add un point ca forme une ligne ou pas
    public List<Line> findLines(int x, int y, Direction direction) {
        if (grid[x][y] != null) {
            return new ArrayList<>();
        }
        int dirX = 0, dirY = 0;
        switch (direction) {
            case HORIZONTAL -> dirX = 1;
            case VERTICAL -> dirY = 1;
            case RISE -> {
                dirX = 1;
                dirY = -1;
            }
            case FALL -> {
                dirX = 1;
                dirY = 1;
            }
        }
        ArrayList<Line> possibleLines = new ArrayList<>();
        int numLocksAllowed = 0;
        switch (mode) {
            case _5T -> numLocksAllowed = 1;
            case _5D -> numLocksAllowed = 0;
        }
        for (int i = -4; i <= 0; i++) {
            Line line = new Line();
            switch (mode) {
                case _5T -> numLocksAllowed = 1;
                case _5D -> numLocksAllowed = 0;
            }
            for (int j = 0; j < 5; j++) {
                int x2 = x + dirX * (i + j);
                int y2 = y + dirY * (i + j);
                if (!isValidX(x2) || !isValidY(y2)) {
                    line = null;
                    break;
                }
                if (grid[x2][y2] != null && !grid[x2][y2].isLocked(direction)) {
                    line.addPoint(new Point(x2, y2));
                } else if (x2 == x && y2 == y) {
                    Point p = new Point(x2, y2);
                    line.addPoint(p);
                    line.setNewPoint(p);
                } else if (grid[x2][y2] != null) {
                    if (numLocksAllowed == 0) {
                        line = null;
                        break;
                    } else {
                        line.addPoint(new Point(x2, y2));
                        numLocksAllowed--;
                    }
                } else {
                    line = null;
                    break;
                }
            }
            if (line != null) {
                line.setDirection(direction);
                possibleLines.add(line);
            }
        }
        return possibleLines;
    }

    public void resetLine() {
        Line line = lines.get(lines.size() - 1);
        for (Point point : line.getPoints()) {
            if (grid[point.x][point.y] != null) {
                grid[point.x][point.y].unlock(line.getDirection());
            }
        }
        lines.remove(line);

        Point newPoint=line.getNewPoint();
        grid[newPoint.x][newPoint.y] = null;
        points.remove(line.getNewPoint());
    }

    public boolean isValidX(int x) {
        return x >= 0 && x < width;
    }

    public boolean isValidY(int y) {
        return y >= 0 && y < height;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Line> possibleLines(){
        HashSet<Point> pointsSoFar = new HashSet<>();
        List<Line> possibleLines = new ArrayList<>();
        points.forEach(gridPoint -> getNeighbors(gridPoint).stream()
                .filter(point -> !pointsSoFar.contains(gridPoint))
                .forEach(point -> {
                    pointsSoFar.add(point);
                    possibleLines.addAll(findLines(point.x, point.y));
                }));
        return possibleLines;
    }

    public Collection<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int x = point.x + i;
                int y = point.y + j;
                if (isValidX(x) && isValidY(y)) {
                    neighbors.add(new Point(x, y));
                }
            }
        }
        return neighbors;
    }

    public Grid copy() {
        Grid copy = new Grid(getWidth(), getHeight(), mode);
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (grid[i][j] == null) continue;
                copy.grid[i][j] = grid[i][j].copy();
            }
        }
        copy.lines.addAll(lines.stream().map(Line::copy).toList());
        copy.points.addAll(points);
        return copy;
    }
    public Grid child(Line line) {
        Grid newGrid = copy();
        newGrid.addLine(line);
        return newGrid;
    }

    public List<Line>  rollout() {
        Grid copy = copy();
        List<Line> list = new LinkedList<>();
        RandomSearchAlgorithm r = new RandomSearchAlgorithm();
        while (true) {

            Line compLine = r.chooseMove(copy);
            if (compLine == null) {
                break;
            }
            list.add(compLine);
            copy.addLine(compLine);
        }
        return list;
    }
}

