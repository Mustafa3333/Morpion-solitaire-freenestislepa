package org.netislepafree.morpion_solitaire.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Line {
    private final List<Point> points;

    /**
     * the newest point among the 5 points that made this point
     */
    private Point newPoint;

    /**
     * line number
     */
    private int number;

    /**
     * grid.Direction of this line
     */
    private Direction direction;
    public Line() {
        points = new ArrayList<>();
    }
    public Line(Collection<Point> points, Point newPoint, Direction direction, int number) {
        this.points = new ArrayList<>();
        this.points.addAll(points);
        this.newPoint = newPoint;
        this.direction = direction;
        this.number = number;
    }

    public void addPoint(Point point) {
        points.add(point);
    }
    public void setNewPoint(Point newPoint) {
        this.newPoint = newPoint;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
