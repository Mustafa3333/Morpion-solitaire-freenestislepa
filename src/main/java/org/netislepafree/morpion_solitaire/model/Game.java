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

/**
 * The type Game.
 */
public class Game {
    private final String playerName;
    /**
     * The Grid.
     */
    public Grid grid;
    /**
     * The Computer mode.
     */
    public boolean computerMode=false;
    private int score;
    private boolean isChosingLine;
    private List<Line> chooseLines;

    /**
     * Instantiates a new Game.
     *
     * @param playerName the player name
     */
    public Game(String playerName) {
        this.playerName = playerName;
        grid = new Grid(30, 30, Mode._5D);
        grid.init();
    }

    /**
     * Player move.
     *
     * @param x the x
     * @param y the y
     */
    public void playerMove(int x, int y) {
        if (computerMode) {
            return;
        }

        if (isChosingLine) {
            if (!processLineSelection(x, y)) {
                System.out.println("Invalid Move");
            }
            return;
        }

        List<Line> possibleLines = grid.findLines(x, y);
        int lineCount = possibleLines.size();

        if (lineCount == 0) {
            System.out.println("Impossible Move");
        } else if (lineCount == 1) {
            makeMove(possibleLines.get(0));
        } else {
            handleMultiplePossibleLines(possibleLines);
        }
    }

    private boolean processLineSelection(int x, int y) {
        for (Line line : chooseLines) {
            for (Point point : line.getPoints()) {
                if (point.x == x && point.y == y) {
                    makeMove(line);
                    isChosingLine = false;
                    grid.setHighlightedPoints(new ArrayList<>());
                    return true;
                }
            }
        }
        return false;
    }

    private void handleMultiplePossibleLines(List<Line> possibleLines) {
        System.out.println("Choose a point to draw the line");
        List<Point> points = new ArrayList<>();
        for (Line line : possibleLines) {
            for (Point point : line.getPoints()) {
                if (isUniquePoint(point, possibleLines)) {
                    points.add(point);
                    break;
                }
            }
        }
        grid.setHighlightedPoints(points);
        isChosingLine = true;
        chooseLines = possibleLines;
    }

    private boolean isUniquePoint(Point point, List<Line> lines) {
        long count = lines.stream()
                          .filter(l -> l.getPoints().contains(point))
                          .count();
        return count == 1;
    }

    /**
     * Computer move.
     *
     * @param algorithm the algorithm
     * @throws Exception the exception
     */
    public void computerMove(Algorithm algorithm) throws Exception {
        if (!isComputerModeActive()) {
            return;
        }

        Line line = getComputerMove(algorithm);
        if (line == null) {
            System.out.println("No valid move found by the algorithm.");
            throw new Exception();
        }

        executeComputerMove(line);
    }

    private boolean isComputerModeActive() {
        if (!computerMode) {
            System.out.println("Computer mode is not active.");
            return false;
        }
        return true;
    }

    private Line getComputerMove(Algorithm algorithm) {
        try {
            return algorithm.chooseMove(grid);
        } catch (Exception e) {
            System.out.println("Error during computer move: " + e.getMessage());
            return null;
        }
    }

    private void executeComputerMove(Line line) {
        makeMove(line);
        System.out.println("Computer made a move.");
    }

    
    private void makeMove(Line line) {
        grid.addLine(line);
        this.score=grid.getLines().size();
    }

    /**
     * Reset move.
     */
    public void resetMove() {
        if (canResetMove()) {
            grid.resetLine();
        }
    }

    private boolean canResetMove() {
        if (grid.getLines().isEmpty()) {
            System.out.println("No moves to reset.");
            return false;
        }
        return true;
    }

    /**
     * Sets game mode.
     *
     * @param mode the mode
     */
    public void setGameMode(Mode mode) {
        this.grid.setMode(mode);
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Load best grid grid.
     *
     * @return the grid
     */
    public static Grid loadBestGrid() {
        try (FileInputStream file = new FileInputStream("best.grid");
             ObjectInputStream in = new ObjectInputStream(file)) {
            return (Grid) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Unable to load the best grid: " + e.getMessage());
            return null;
        }
    }
    
    private static void saveGrid(Grid grid) {
        Grid best = loadBestGrid();

        if (shouldSaveGrid(best, grid)) {
            try (FileOutputStream file = new FileOutputStream("best.grid");
                 ObjectOutputStream out = new ObjectOutputStream(file)) {
                out.writeObject(grid);
                System.out.println("Grid saved!");
            } catch (IOException e) {
                System.out.println("Error saving the best grid: " + e.getMessage());
            }
        }
    }

    private static boolean shouldSaveGrid(Grid best, Grid current) {
        if (best == null || best.getLines().size() <= current.getLines().size()) {
            System.out.println("Saving best grid...");
            return true;
        }
        return false;
    }
    
    private void saveScore() {
        try {
            Score.saveScore(playerName, (double) getScore());
            System.out.println("Score saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save score: " + e.getMessage());
        }
    }

    /**
     * Game over.
     */
    public void gameOver() {
        System.out.println("Game over !");
        saveScore();
        saveGrid(this.grid);
    }

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
}
