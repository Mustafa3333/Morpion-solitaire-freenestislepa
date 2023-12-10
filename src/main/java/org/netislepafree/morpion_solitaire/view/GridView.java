package org.netislepafree.morpion_solitaire.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Point;

import java.util.List;

public class GridView {
    private Grid grid;
    private Canvas canvas;
    private GraphicsContext g;
    private int cellSize;
    public int width;
    public int height;
    private final int offX = cellSize;
    private final int offY = cellSize;
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

    public void init(){
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Rectangle of grid
        g.setLineWidth(2.0);
        g.setStroke(Color.valueOf("#CCCCCC"));
        g.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Grid
        g.setLineWidth(1);
        g.setStroke(Color.valueOf("#CCCCCC"));
        for (double gridX = 0; gridX < grid.getWidth(); gridX++) {
            double x1 = (offX + gridX * cellSize)+0.5;
            double y1 = (offY)+0.5;
            double x2 = (offX + gridX * cellSize)+0.5;
            double y2 = (offY + (grid.getHeight() - 1) * cellSize)+0.5;

            g.strokeLine(x1, y1, x2, y2);
        }
        for (double gridY = 0; gridY < grid.getHeight(); gridY++) {
            double x1 = (offX)+0.5;
            double y1 = (offY + gridY * cellSize)+0.5;
            double x2 = (offX + (grid.getWidth() - 1) * cellSize)+0.5;
            double y2 = (offY + gridY * cellSize)+0.5;
            g.strokeLine(x1, y1, x2, y2);
        }

        // draw init points
        updateView();
    }

    public void updateView(){
        // Draw points
        g.setFill(Color.valueOf("#000000"));
        grid.getPoints().forEach(point -> {
            drawPoint(point, 5);
        });

        // Draw numbered points
        g.setStroke(Color.valueOf("#8B0000"));
        grid.getLines().forEach(line -> drawNumberedPoint(line.getNewPoint(), line.getNumber()));

        // Draw lines
        g.setStroke(Color.valueOf("#8B0000"));
        grid.getLines().forEach(line ->{
            drawLine(line);
        });

        // Draw highlihgted Points
        highlightPoints(grid.getHighlightedPoints());
    }

    private void highlightPoints(List<Point> points){
        if (points.size()>0) {
            g.setFill(Color.valueOf("#FF5733"));
            points.forEach(point -> {
                drawPoint(point, 5);
            });
        }
    }


    public void drawPoint(Point point, int radius){
        g.fillOval(offX + cellSize * point.x - radius, offY + cellSize * point.y - radius, 2 * radius, 2 * radius);
    }

    private void drawLine(Line line) {
        g.setLineWidth(2);
        Point p1 = line.getPoints().get(0);
        Point p2 = line.getPoints().get(line.getPoints().size() - 1);

        g.strokeLine(offX + cellSize * p1.x, offY + cellSize * p1.y, offX + cellSize * p2.x, offY + cellSize * p2.y);
    }

    private void drawNumberedPoint(Point p, int num) {
        double radius = 10;
        double centerX = offX + cellSize * p.x - radius;
        double centerY = offY + cellSize * p.y - radius;
        double numX = centerX + radius * .65 * 1.0 / (num + "").length();
        double numY = centerY + radius * 1.3;
        g.setFill(Color.valueOf("#8BE92F"));
        g.fillOval(centerX, centerY, radius * 2, radius * 2);
        g.setLineWidth(1);
        g.setStroke(Color.valueOf("#0C3547"));
        g.strokeText(num + "", numX, numY);
    }
    public int getCellSize() {
        return cellSize;
    }

    public int getOffX() {
        return offX;
    }

    public int getOffY() {
        return offY;
    }
}

