package org.netislepafree.morpion_solitaire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("../../../../resources/game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),20,100);
        stage.setTitle("Morpion solitaire");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

}
