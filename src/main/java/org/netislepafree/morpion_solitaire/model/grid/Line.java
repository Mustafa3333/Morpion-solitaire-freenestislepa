package org.netislepafree.morpion_solitaire.model.grid;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Line.
 */
public class Line implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Point> points;
    private Point newPoint;
    private int number;
    private Direction direction;

    /**
     * Instantiates a new Line.
     */
    public Line() {
        points = new ArrayList<>();
    }

    /**
     * Instantiates a new Line.
     *
     * @param points    the points
     * @param newPoint  the new point
     * @param direction the direction
     * @param number    the number
     */
    public Line(Collection<Point> points, Point newPoint, Direction direction, int number) {
        this.points = new ArrayList<>();
        this.points.addAll(points);
        this.newPoint = newPoint;
        this.direction = direction;
        this.number = number;
    }

    /**
     * Add point.
     *
     * @param point the point
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Copy line.
     *
     * @return the line
     */
    public Line copy() {
        return new Line(points, newPoint, direction, number);
    }

    /**
     * Sets new point.
     *
     * @param newPoint the new point
     */
    public void setNewPoint(Point newPoint) {
        this.newPoint = newPoint;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets new point.
     *
     * @return the new point
     */
    public Point getNewPoint() {
        return newPoint;
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }
}
