package org.netislepafree.morpion_solitaire.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.algorithms.Algorithm;
import org.netislepafree.morpion_solitaire.model.algorithms.NMCS;
import org.netislepafree.morpion_solitaire.model.algorithms.RandomSearchAlgorithm;
import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Mode;
import org.netislepafree.morpion_solitaire.model.util.Score;
import org.netislepafree.morpion_solitaire.model.util.ScoreEntry;
import org.netislepafree.morpion_solitaire.view.GridView;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * The type User interface.
 */
public class UserInterface {
    @FXML
    private ComboBox<String> gameModeOptions;
    /**
     * The Canvas.
     */
    @FXML
    public Canvas canvas;
    @FXML
    private ComboBox<String> algorithmOptions;
    private Game game;
    private GridView gridView;

    /**
     * Start.
     *
     * @param playerName the player name
     */
    public void start(String playerName){
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
        algorithmOptions.getItems().removeAll(algorithmOptions.getItems());
        algorithmOptions.getItems().addAll(List.of("Random","NMCS"));
        algorithmOptions.getSelectionModel().select(0);
        this.game=new Game(playerName);
        this.gridView= new GridView(game.grid, canvas,25);
        gridView.init();
    }

    /**
     * Mouse pressed.
     *
     * @param me the mouseevent
     */
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


    /**
     * Game mode changed.
     */
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

    /**
     * Reset.
     */
    public void reset() {
        recreateGame(game.getPlayerName());
        updateGameMode();
        gridView.init();
    }

    private void recreateGame(String playerName) {
        game = new Game(playerName);
        gridView = new GridView(game.grid, canvas, 25);
    }

    private void updateGameMode() {
        String selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();
        Mode mode = "5T".equalsIgnoreCase(selectedMode) ? Mode._5T : Mode._5D;
        game.setGameMode(mode);
    }


    /**
     * Simulate.
     *
     */
    public void simulate() {
        this.game.computerMode=true;
        String algorithmName = algorithmOptions.getSelectionModel().getSelectedItem().trim();
        Algorithm algo = switch (algorithmName) {
                                case "Random" -> new RandomSearchAlgorithm();
                                case "NMCS" -> new NMCS();
                                default -> null;
        };
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

    /**
     * Undo.
     */
    public void undo(){
        this.game.resetMove();
        gridView.init();
        gridView.updateView();
    }
    @FXML
    private void displayHistory() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Score History");

        try {
            List<ScoreEntry> history = Score.loadScores();
            if (history.isEmpty()) {
                dialog.setContentText("No score history available.");
            } else {
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(5);
                gridPane.setPadding(new Insets(10, 10, 10, 10));

                gridPane.add(new Label("Name"), 0, 0);
                gridPane.add(new Label("Score"), 1, 0);

                int row = 1;
                for (ScoreEntry scoreEntry : history) {
                    gridPane.add(new Label(scoreEntry.getUsername()), 0, row);
                    gridPane.add(new Label(String.valueOf(scoreEntry.getScore())), 1, row);
                    row++;
                }

                dialog.getDialogPane().setContent(gridPane);
            }
        } catch (IOException e) {
            dialog.setContentText("Error loading score history.");
            e.printStackTrace(); // Handle the exception appropriately
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        dialog.showAndWait();
    }
    @FXML
    private void displayBestGrid() {
        System.out.println("Best Grid");

        Grid bestGrid = Game.loadBestGrid();
        if (bestGrid == null) {
            System.out.println("Failed to load the best grid!");
            return;
        }

        Dialog dialog = new Dialog();
        Canvas canvas = new Canvas();
        GridView gv = new GridView(bestGrid, canvas, 25);
        gv.init();
        dialog.getDialogPane().setContent(canvas);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("ok",ButtonBar.ButtonData.OK_DONE ));
        dialog.showAndWait();
    }
}
