package org.netislepafree.morpion_solitaire.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Point;

import java.util.List;

/**
 * The type Grid view.
 */
public class GridView {
    private final Grid grid;
    private final Canvas canvas;
    private final GraphicsContext g;
    private int cellSize;
    /**
     * The Width.
     */
    public int width;
    /**
     * The Height.
     */
    public final int height;
    private final int offX = cellSize;
    private final int offY = cellSize;

    /**
     * Instantiates a new Grid view.
     *
     * @param grid     the grid
     * @param canvas   the canvas
     * @param cellSize the cell size
     */
    public GridView(Grid grid, Canvas canvas, int cellSize) {
        this.grid=grid;
        this.canvas = canvas;
        this.g=canvas.getGraphicsContext2D();
        this.cellSize=cellSize;
        this.height=cellSize*grid.getWidth();
        this.width=cellSize*grid.getHeight();
        canvas.setWidth(height);
        canvas.setHeight(width);
        canvas.setFocusTraversable(true);
    }

    /**
     * Init.
     */
    public void init() {
        clearCanvas();
        drawGridRectangle();
        drawGridLines();
        // draw init points
        updateView();
    }

    private void clearCanvas() {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGridRectangle() {
        g.setLineWidth(2.0);
        g.setStroke(Color.valueOf("#CCCCCC"));
        g.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGridLines() {
        g.setLineWidth(1);
        g.setStroke(Color.valueOf("#CCCCCC"));
        drawVerticalGridLines();
        drawHorizontalGridLines();
    }

    private void drawVerticalGridLines() {
        for (double gridX = 0; gridX < grid.getWidth(); gridX++) {
            double x = (offX + gridX * cellSize) + 0.5;
            g.strokeLine(x, offY + 0.5, x, (offY + (grid.getHeight() - 1) * cellSize) + 0.5);
        }
    }

    private void drawHorizontalGridLines() {
        for (double gridY = 0; gridY < grid.getHeight(); gridY++) {
            double y = (offY + gridY * cellSize) + 0.5;
            g.strokeLine(offX + 0.5, y, (offX + (grid.getWidth() - 1) * cellSize) + 0.5, y);
        }
    }

    /**
     * Update view.
     */
    public void updateView() {
        drawPoints();
        drawNumberedPoints();
        drawLines();
        highlightPoints();
    }

    private void drawPoints() {
        g.setFill(Color.valueOf("#000000"));
        grid.getPoints().forEach(point -> drawPoint(point, 5));
    }

    private void drawNumberedPoints() {
        g.setStroke(Color.valueOf("#0F7129"));
        grid.getLines().forEach(line -> drawNumberedPoint(line.getNewPoint(), line.getNumber()));
    }

    private void drawLines() {
        g.setStroke(Color.valueOf("#B91010"));
        grid.getLines().forEach(this::drawLine);
    }

    private void highlightPoints() {
        highlightPoints(grid.getHighlightedPoints());
    }

    private void highlightPoints(List<Point> points){
        if (points.size()>0) {
            g.setFill(Color.valueOf("#0F7129"));
            points.forEach(point -> drawPoint(point, 5));
        }
    }

    /**
     * Draw point.
     *
     * @param point  the point
     * @param radius the radius
     */
    public void drawPoint(Point point, int radius) {
        double centerX = calculateCoordinate(point.x, offX, cellSize);
        double centerY = calculateCoordinate(point.y, offY, cellSize);
        g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    private double calculateCoordinate(double pointCoord, double offset, double cellDimension) {
        return offset + cellDimension * pointCoord;
    }

    private void drawLine(Line line) {
        g.setLineWidth(3);
        Point p1 = line.getPoints().get(0);
        Point p2 = line.getPoints().get(line.getPoints().size() - 1);

        double x1 = calculatePosition(p1.x, offX, cellSize);
        double y1 = calculatePosition(p1.y, offY, cellSize);
        double x2 = calculatePosition(p2.x, offX, cellSize);
        double y2 = calculatePosition(p2.y, offY, cellSize);

        g.strokeLine(x1, y1, x2, y2);
    }

    private double calculatePosition(double pointCoordinate, double offset, double cellSize) {
        return offset + cellSize * pointCoordinate;
    }

    private void drawNumberedPoint(Point p, int num) {
        double radius = 10;
        double centerX = calculatePosition(p.x, offX, cellSize) - radius;
        double centerY = calculatePosition(p.y, offY, cellSize) - radius;
        drawCircle(centerX, centerY, radius);
        drawNumberAtCenter(centerX, centerY, radius, num);
    }

    private void drawCircle(double centerX, double centerY, double radius) {
        g.setFill(Color.valueOf("#0F7129"));
        g.fillOval(centerX, centerY, radius * 2, radius * 2);
    }

    private void drawNumberAtCenter(double centerX, double centerY, double radius, int num) {
        g.setLineWidth(1);
        g.setStroke(Color.valueOf("#FFFFFF"));
        String numberStr = Integer.toString(num);
        double numX = centerX + radius * 0.65 / numberStr.length();
        double numY = centerY + radius * 1.3;
        g.strokeText(numberStr, numX, numY);
    }

    /**
     * Gets cell size.
     *
     * @return the cell size
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Gets off x.
     *
     * @return the off x
     */
    public int getOffX() {
        return offX;
    }

    /**
     * Gets off y.
     *
     * @return the off y
     */
    public int getOffY() {
        return offY;
    }
}

