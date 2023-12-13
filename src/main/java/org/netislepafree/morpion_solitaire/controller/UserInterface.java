package org.netislepafree.morpion_solitaire.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import org.netislepafree.morpion_solitaire.model.algorithms.Algorithm;
import org.netislepafree.morpion_solitaire.model.algorithms.NMCS;
import org.netislepafree.morpion_solitaire.model.algorithms.RandomSearchAlgorithm;
import org.netislepafree.morpion_solitaire.model.grid.Mode;
import javafx.scene.input.MouseEvent;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.view.GridView;

import java.awt.Point;
import java.util.List;

public class UserInterface {
    @FXML
    private ComboBox<String> gameModeOptions;
    @FXML
    public Canvas canvas;
    @FXML
    private ComboBox<String> algorithmOptions;
    private Game game;
    private GridView gridView;
    
    public void start(){
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
        algorithmOptions.getItems().removeAll(algorithmOptions.getItems());
        algorithmOptions.getItems().addAll(List.of("Random","NMCS"));
        algorithmOptions.getSelectionModel().select(0);
        this.game=new Game();
        this.gridView= new GridView(game.grid, canvas,25);
        gridView.init();
    }
    
    public void mousePressed(MouseEvent me) {
        logMousePosition(me);
        if (isInvalidClick(me)) {
            System.out.println("INVALID");
            return;
        }

        Point gridPoint = convertToGridCoordinates(me);
        game.playerMove(gridPoint.x, gridPoint.y);
        gridView.updateView();
    }

    private void logMousePosition(MouseEvent me) {
        System.out.printf("(%f,%f)\n", me.getX(), me.getY());
    }

    private boolean isInvalidClick(MouseEvent me) {
        double tolerance = 0.2;
        double frX = calculateFractionalPart((me.getX() - gridView.getOffX()) / gridView.getCellSize());
        double frY = calculateFractionalPart((me.getY() - gridView.getOffY()) / gridView.getCellSize());
        return (frX > tolerance && frX < 1 - tolerance) || (frY > tolerance && frY < 1 - tolerance);
    }

    private double calculateFractionalPart(double value) {
        return value - (int) value;
    }

    private Point convertToGridCoordinates(MouseEvent me) {
        int gridX = (int) Math.round((me.getX() - gridView.getOffX()) / gridView.getCellSize());
        int gridY = (int) Math.round((me.getY() - gridView.getOffY()) / gridView.getCellSize());
        return new Point(gridX, gridY);
    }

    
    public void gameModeChanged() {
        Mode mode = determineSelectedGameMode();
        resetGameWithNewMode(mode);
    }

    private Mode determineSelectedGameMode() {
        String selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();
        return "5T".equalsIgnoreCase(selectedMode) ? Mode._5T : Mode._5D;
    }

    private void resetGameWithNewMode(Mode mode) {
        reset();
        game.setGameMode(mode);
    }
    
    public void reset() {
        recreateGame();
        updateGameMode();
        gridView.init();
    }

    private void recreateGame() {
        game = new Game();
        gridView = new GridView(game.grid, canvas, 25);
    }

    private void updateGameMode() {
        String selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();
        Mode mode = "5T".equalsIgnoreCase(selectedMode) ? Mode._5T : Mode._5D;
        game.setGameMode(mode);
    }

    
    public void simulate() throws Exception {
        this.game.computerMode=true;
        String algorithmName = algorithmOptions.getSelectionModel().getSelectedItem().trim();
        Algorithm algo = switch (algorithmName) {
                                case "Random" -> {
                                    yield new RandomSearchAlgorithm();
                                }
                                case "NMCS" -> {
                                    yield new NMCS();
                                }
                                default -> {
                                    yield null;
                                }};
        Thread simulationThread = new Thread(() -> {
            while (true) {
                try {
                    game.computerMove(algo);
                    gridView.updateView();
                } catch (Exception e) {
                    game.gameOver();
                    break;
                }
            }
        });
        simulationThread.start();
        simulationThread.interrupt();

    }
    
    public void undo(){
        this.game.resetMove();
        gridView.init();
        gridView.updateView();
    }
}
