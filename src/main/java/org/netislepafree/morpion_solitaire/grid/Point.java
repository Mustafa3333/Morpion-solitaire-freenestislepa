package org.netislepafree.morpion_solitaire.grid;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Point implements Serializable {
    public final int x, y;
    private final Set<Direction> lockedDirections;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.lockedDirections = new HashSet<>();
    }
    public boolean isLocked(Direction direction) {
        return lockedDirections.contains(direction);
    }

    public void lock(Direction direction) {
        lockedDirections.add(direction);
    }

    public void unlock(Direction direction) {
        lockedDirections.remove(direction);
    }

    public boolean equals(Object other) {
        if (other.getClass() != getClass())
            return false;
        Point p = (Point) other;
        return x == p.x && y == p.y;
    }

}
