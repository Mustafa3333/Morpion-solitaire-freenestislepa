package org.netislepafree.morpion_solitaire;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import org.netislepafree.morpion_solitaire.grid.Grid;
import org.netislepafree.morpion_solitaire.grid.Mode;
import javafx.scene.input.MouseEvent;
import org.netislepafree.morpion_solitaire.view.GridView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInterface {
    @FXML
    private ComboBox<String> gameModeOptions;
    @FXML
    public Canvas canvas;
    private Game game;
    private GridView gridView;
    public void start(){
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
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
        Mode mode = gameModeOptions.getSelectionModel().getSelectedItem().equalsIgnoreCase("5T") ? Mode._5T : Mode._5D;
        game.setGameMode(mode);
    }
}
