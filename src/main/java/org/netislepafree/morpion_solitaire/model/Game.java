package org.netislepafree.morpion_solitaire.model;

import org.netislepafree.morpion_solitaire.model.algorithms.Algorithm;
import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Mode;
import org.netislepafree.morpion_solitaire.model.grid.Point;
import org.netislepafree.morpion_solitaire.model.util.Score;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private String playerName;
    public Grid grid;
    public boolean computerMode=false;
    private double score=0;
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
    public void computerMove(Algorithm algorithm) throws Exception {
        if (!computerMode) throw new Exception();
        Line line = algorithm.chooseMove(grid);
        System.out.println("Got line");
        if (line == null) {
            throw new Exception();
        }
        makeMove(line);
    }
    private void makeMove(Line line) {
        grid.addLine(line);
        this.score=grid.getLines().size();
    }

    public void resetMove(){
        if (grid.getLines().size() < 1) return;
        grid.resetLine();
    }

    public void setGameMode(Mode mode) {
        this.grid.setMode(mode);
    }
    
    public int getScore() {
        return grid.getLines().size();
    }
    
    private static Grid loadBestGrid(){
        try {
            FileInputStream file = new FileInputStream("best.grid");
            ObjectInputStream in = new ObjectInputStream(file);

            Grid grid = (Grid) in.readObject();
            in.close();
            file.close();
            return grid;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Unable to load the best grid...");
            return null;
        }
    }
    private static void saveGrid(Grid grid) {
        Grid best=loadBestGrid();

        if (best == null || best.getLines().size() <= grid.getLines().size()) {
            try {
                System.out.println("Saving best grid...");
                FileOutputStream file = new FileOutputStream("best.grid");
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(grid);
                out.close();
                file.close();
                System.out.println("Grid saved!");
            } catch (IOException e) {
                System.out.println("Error saving the best grid");
            }
    }
}
    private void saveScore() {
            try {
                Score.saveScore(playerName, (double) getScore());
            } catch (IOException e){
                System.out.println("Failed to save score");
            }
    }
    public void gameOver() {
        System.out.println("game over !");
        saveScore();
        saveGrid(this.grid);
    }

}
