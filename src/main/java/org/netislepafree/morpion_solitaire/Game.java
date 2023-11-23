package org.netislepafree.morpion_solitaire;

import org.netislepafree.morpion_solitaire.grid.Grid;
import org.netislepafree.morpion_solitaire.grid.Line;
import org.netislepafree.morpion_solitaire.grid.Mode;
import org.netislepafree.morpion_solitaire.grid.Point;
import org.netislepafree.morpion_solitaire.view.GridView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String playerName;
    public Grid grid;
    private GridView gridView;
    private boolean computerMode=false;
    private boolean isChosingLine;
    private List<Line> chooseLines;

    public Game() {
        grid = new Grid(50, 50, Mode._5D);
        grid.init();
    }

    public void playerMove(int x, int y){
        if (computerMode) return;
        if (this.isChosingLine==true){
            for (Line line : this.chooseLines){
                for (Point point : line.getPoints()) {
                    if (point.x == x && point.y == y) {
                        makeMove(line);
                        isChosingLine = false;
                        grid.setHighlightedPoints(new ArrayList<>());
                        return;
                    }
                }
            }
            System.out.println("Invalid Move");
            return;
        }
        List<Line> possibleLines = grid.findLines(x, y);
        if (possibleLines.size() == 0) {
            System.out.println("Invalid Move");
        } else if (possibleLines.size() == 1) {
            makeMove(possibleLines.get(0));
        } else if (possibleLines.size() > 1) {
            System.out.println("More than one possible");
            List<Point> points = new ArrayList<>();
            for (Line line : possibleLines) {
                List<Point> otherPoints = new ArrayList<>();
                possibleLines.stream().filter(l -> l != line).toList()
                        .forEach(l -> otherPoints.addAll(l.getPoints()));
                System.out.println(otherPoints);
                for (Point point : line.getPoints()) {
                    if (!otherPoints.contains(point)) {
                        points.add(point);
                        break;
                    }
                }
            }
            grid.setHighlightedPoints(points);
            this.isChosingLine=true;
            this.chooseLines=possibleLines;
        }
    }
    private void makeMove(Line line) {
        grid.addLine(line);
    }

    public void setGameMode(Mode mode) {
        this.grid.setMode(mode);
    }

}
