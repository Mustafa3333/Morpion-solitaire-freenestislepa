package org.netislepafree.morpion_solitaire.model.grid;

import org.netislepafree.morpion_solitaire.model.algorithms.RandomSearchAlgorithm;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Grid implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Point[][] grid;
    private Set<Point> points;
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
    private List<Line> lines;
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
        Point newPoint = grid[x][y];
        if (newPoint == null) {
            newPoint = new Point(x, y);
            grid[x][y] = newPoint;
            points.add(newPoint);
        }
    }
    
    public void addLine(Line line) {
        line.setNumber(lines.size() + 1);

        for (Point point : line.getPoints()) {
            Point currentPoint = grid[point.x][point.y];
            if (currentPoint == null) {
                addPoint(point.x, point.y);
                currentPoint = grid[point.x][point.y];
                line.setNewPoint(currentPoint);
            }
            currentPoint.lock(line.getDirection());
        }

        lines.add(line);
    }

    public void init() {
        int startX = (width - 10) / 2;
        int startY = (height - 10) / 2;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (isValidInitPoint(i, j)) {
                    addPoint(startX + i, startY + j);
                }
            }
        }
    }

    private boolean isValidInitPoint(int i, int j) {
        boolean inCenter = (i > 0 && i < 9 && j > 3 && j < 6) || (j > 0 && j < 9 && i > 3 && i < 6);
        boolean inCorner = (i < 3 || i > 6) && (j < 3 || j > 6);
        return !inCenter && !inCorner;
    }


    public List<Line> findLines(int x, int y) {
        if (!isPointValidForLine(x, y)) {
            return Collections.emptyList();
        }

        return Arrays.stream(Direction.values())
                     .flatMap(direction -> findLines(x, y, direction).stream())
                     .collect(Collectors.toList());
    }

    private boolean isPointValidForLine(int x, int y) {
        return isValidX(x) && isValidY(y) && grid[x][y] == null;
    }

    public Mode getMode() {
		return this.mode;
	}

    public List<Line> findLines(int x, int y, Direction direction) {
        if (grid[x][y] != null) {
            return Collections.emptyList();
        }

        int dirX = direction == Direction.HORIZONTAL || direction == Direction.RISE || direction == Direction.FALL ? 1 : 0;
        int dirY = direction == Direction.VERTICAL ? 1 : (direction == Direction.RISE ? -1 : (direction == Direction.FALL ? 1 : 0));

        List<Line> possibleLines = new ArrayList<>();
        int numLocksAllowed = mode == Mode._5T ? 1 : 0;

        for (int offset = -4; offset <= 0; offset++) {
            boolean isValidLine = true;
            Line line = new Line();
            int locksUsed = 0;

            for (int j = 0; j < 5; j++) {
                int currentX = x + dirX * (offset + j);
                int currentY = y + dirY * (offset + j);

                if (!isValidX(currentX) || !isValidY(currentY)) {
                    isValidLine = false;
                    break;
                }

                Point point = new Point(currentX, currentY);
                boolean isCurrentPoint = currentX == x && currentY == y;
                boolean isOccupied = grid[currentX][currentY] != null;

                if (isOccupied && !grid[currentX][currentY].isLocked(direction)) {
                    line.addPoint(point);
                } else if (isCurrentPoint) {
                    line.addPoint(point);
                    line.setNewPoint(point);
                } else if (isOccupied) {
                    if (locksUsed >= numLocksAllowed) {
                        isValidLine = false;
                        break;
                    }
                    line.addPoint(point);
                    locksUsed++;
                } else {
                    isValidLine = false;
                    break;
                }
            }

            if (isValidLine) {
                line.setDirection(direction);
                possibleLines.add(line);
            }
        }

        return possibleLines;
    }

    public void resetLine() {
        if (lines.isEmpty()) {
            return;
        }

        Line lastLine = lines.remove(lines.size() - 1);
        unlockPoints(lastLine);
        removeNewPoint(lastLine);
    }

    private void unlockPoints(Line line) {
        line.getPoints().stream()
            .filter(point -> grid[point.x][point.y] != null)
            .forEach(point -> grid[point.x][point.y].unlock(line.getDirection()));
    }

    private void removeNewPoint(Line line) {
        Point newPoint = line.getNewPoint();
        if (newPoint != null && grid[newPoint.x][newPoint.y] != null) {
            grid[newPoint.x][newPoint.y] = null;
            points.remove(newPoint);
        }
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

    public List<Line> possibleLines() {
        HashSet<Point> pointsSoFar = new HashSet<>();
        List<Line> possibleLines = new ArrayList<>();

        for (Point gridPoint : points) {
            for (Point neighbor : getNeighbors(gridPoint)) {
                if (!pointsSoFar.contains(neighbor)) {
                    pointsSoFar.add(neighbor);
                    possibleLines.addAll(findLines(neighbor.x, neighbor.y));
                }
            }
        }

        return possibleLines;
    }


    public Collection<Point> getNeighbors(Point point) {
        return IntStream.rangeClosed(-1, 1)
                        .boxed()
                        .flatMap(i -> IntStream.rangeClosed(-1, 1)
                                               .filter(j -> !(i == 0 && j == 0))
                                               .mapToObj(j -> new Point(point.x + i, point.y + j)))
                        .filter(p -> isValidX(p.x) && isValidY(p.y))
                        .collect(Collectors.toList());
    }


    public Grid copy() {
        Grid copy = new Grid(getWidth(), getHeight(), mode);

        IntStream.range(0, getWidth()).forEach(i -> 
            IntStream.range(0, getHeight()).forEach(j -> {
                if (grid[i][j] != null) {
                    copy.grid[i][j] = grid[i][j].copy();
                }
            })
        );
        copy.lines = lines.stream().map(Line::copy).collect(Collectors.toList());
        copy.points = new HashSet<>(points);

        return copy;
    }

    public Grid child(Line line) {
        Grid newGrid = copy();
        newGrid.addLine(line);
        return newGrid;
    }

    public List<Line> rollout() {
        Grid copy = copy();
        List<Line> list = new ArrayList<>();
        RandomSearchAlgorithm r = new RandomSearchAlgorithm();

        Line compLine;
        while ((compLine = r.chooseMove(copy)) != null) {
            list.add(compLine);
            copy.addLine(compLine);
        }

        return list;
    }

}

