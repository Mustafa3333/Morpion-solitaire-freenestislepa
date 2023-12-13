package org.netislepafree.morpion_solitaire.model.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Line implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Point> points;
    private Point newPoint;
    private int number;   
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
    public Line copy() {
        return new Line(points, newPoint, direction, number);
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

    public Point getNewPoint() {
        return newPoint;
    }

    public int getNumber() {
        return number;
    }
}
