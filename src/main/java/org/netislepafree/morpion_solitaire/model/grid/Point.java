package org.netislepafree.morpion_solitaire.model.grid;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Point.
 */
public class Point implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The X.
     */
    public final int x, /**
     * The Y.
     */
    y;
    private final Set<Direction> lockedDirections;

    /**
     * Instantiates a new Point.
     *
     * @param x the x
     * @param y the y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.lockedDirections = new HashSet<>();
    }

    /**
     * Is locked boolean.
     *
     * @param direction the direction
     * @return the boolean
     */
    public boolean isLocked(Direction direction) {
        return lockedDirections.contains(direction);
    }

    /**
     * Lock.
     *
     * @param direction the direction
     */
    public void lock(Direction direction) {
        lockedDirections.add(direction);
    }

    /**
     * Unlock.
     *
     * @param direction the direction
     */
    public void unlock(Direction direction) {
        lockedDirections.remove(direction);
    }

    /**
     * Copy point.
     *
     * @return the point
     */
    public Point copy() {
        Point copy = new Point(x, y);
        copy.lockedDirections.addAll(lockedDirections);
        return copy;
    }

    public boolean equals(Object other) {
        if (other.getClass() != getClass())
            return false;
        Point p = (Point) other;
        return x == p.x && y == p.y;
    }

}
