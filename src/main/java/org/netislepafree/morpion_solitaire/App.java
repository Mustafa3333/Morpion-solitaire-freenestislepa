package org.netislepafree.morpion_solitaire;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.netislepafree.morpion_solitaire.controller.UserInterface;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;


/**
 * The type App.
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15));

        // Champ de texte pour l'identifiant
        TextField usernameField = new TextField();
        usernameField.setPromptText("Identifiant");
        usernameField.getStyleClass().add("text-field");

        // Champ de texte pour le mot de passe
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("text-field");

        // Bouton de connexion
        Button loginButton = new Button("Connexion");
        loginButton.getStyleClass().add("button");
        loginButton.setOnAction(e -> {
            if (checkCredentials(usernameField.getText(), passwordField.getText())) {
                startGame(usernameField.getText());
            } else {
                showAlert(Alert.AlertType.ERROR, stage, "Erreur de Connexion", "Identifiant ou mot de passe incorrect.");
            }
        });

        // Bouton d'inscription
        Button signupButton = new Button("Inscription");
        signupButton.getStyleClass().addAll("button", "red");
        signupButton.setOnAction(e -> showSignupWindow());

     // HBox pour les boutons de connexion et d'inscription
        HBox buttonBox = new HBox(10); // 10 est l'espacement entre les boutons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, signupButton);

        // Bouton "Règles du jeu"
        Button rulesButton = new Button("Règles du jeu");
        rulesButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        rulesButton.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("http://www.morpionsolitaire.com/Francais/Rules.htm"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Ajouter les éléments à la VBox
        root.getChildren().addAll(usernameField, passwordField, buttonBox, rulesButton);

        // Créer la scène avec le thème sombre
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/darkTheme.css")).toExternalForm());

        // Configurer et afficher le stage
        stage.setTitle("Connexion - Morpion solitaire");
        stage.setScene(scene);
        stage.show();
    }

    private void startGame(String playerName) {
        try {
            Stage gameStage = new Stage();
            gameStage.setMaximized(true);
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/game.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 20, 100);
            scene.getRoot().setStyle("-fx-base:#F5F5F5");
            UserInterface controller = fxmlLoader.getController();
            controller.start(playerName);

            gameStage.setTitle("Morpion solitaire");
            gameStage.setScene(scene);
            gameStage.setOnCloseRequest(e -> System.exit(0));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showSignupWindow() {
        Stage signupStage = new Stage();
        VBox signupRoot = new VBox(10);
        signupRoot.setAlignment(Pos.CENTER);
        signupRoot.setPadding(new Insets(15));

        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("Nouvel identifiant");
        newUsernameField.getStyleClass().add("text-field");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nouveau mot de passe");
        newPasswordField.getStyleClass().add("text-field");

        Button signupConfirmButton = new Button("Confirmer l'inscription");
        signupConfirmButton.getStyleClass().add("button");
        signupConfirmButton.setOnAction(e -> registerUser(newUsernameField.getText(), newPasswordField.getText(), signupStage));

        signupRoot.getChildren().addAll(newUsernameField, newPasswordField, signupConfirmButton);

        Scene signupScene = new Scene(signupRoot, 300, 250);
        signupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-theme.css")).toExternalForm());

        signupStage.setScene(signupScene);
        signupStage.setTitle("Inscription");
        signupStage.show();
    }


    private void registerUser(String username, String password, Stage signupStage) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            String userRecord = username + ":" + bytesToHex(hashedPassword) + "\n";

            Path filePath = Paths.get("src/main/resources/users.txt");
            Files.write(filePath, userRecord.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            // Afficher un message de confirmation
            showAlert(Alert.AlertType.INFORMATION, signupStage, "Inscription Réussie", "Utilisateur enregistré avec succès!");

            // Fermer la fenêtre d'inscription
            signupStage.close();

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, Stage owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);

        // Définir un délai pour fermer l'alerte automatiquement
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> alert.close());
        delay.play();

        alert.show();
    }



    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Check credentials boolean.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public boolean checkCredentials(String username, String password) {
        try {
            Path filePath = Paths.get("src/main/resources/users.txt");
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] userDetails = line.split(":");
                if (userDetails[0].equals(username) && userDetails[1].equals(hashPassword(password))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}