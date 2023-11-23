package org.netislepafree.morpion_solitaire;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.netislepafree.morpion_solitaire.grid.Grid;
import org.netislepafree.morpion_solitaire.grid.Mode;

public class App extends Application {
    @FXML
    public Canvas canvas;
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),20,100);
        scene.getRoot().setStyle("-fx-base:black");
        UserInterface controller = fxmlLoader.getController();
        controller.start();

        stage.setTitle("Morpion solitaire");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

}
