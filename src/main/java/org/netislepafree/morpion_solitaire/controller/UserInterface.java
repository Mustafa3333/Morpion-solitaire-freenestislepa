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
        System.out.printf("(%f,%f)", me.getX(), me.getY());
        double tolerance = 0.2;
        double tempX = (me.getX() - gridView.getOffX()) / gridView.getCellSize();
        double tempY = (me.getY() - gridView.getOffY()) / gridView.getCellSize();
        double frX = tempX - (int) tempX;
        double frY = tempY - (int) tempY;
        if ((frX > tolerance && frX < 1 - tolerance) || (frY > tolerance && frY < 1 - tolerance)) {
            System.out.println("INVALID");
            return;
        }
        int gridX = (int) Math.round(tempX);
        int gridY = (int) Math.round(tempY);
        game.playerMove(gridX, gridY);
        gridView.updateView();
    }
    public void gameModeChanged(){
        reset();
        Mode mode = gameModeOptions.getSelectionModel().getSelectedItem().equalsIgnoreCase("5T") ? Mode._5T : Mode._5D;
        game.setGameMode(mode);
    }
    public void reset(){
        this.game=new Game();
        this.gridView= new GridView(game.grid, canvas,25);
        gridView.init();
        Mode mode = gameModeOptions.getSelectionModel().getSelectedItem().equalsIgnoreCase("5T") ? Mode._5T : Mode._5D;
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
