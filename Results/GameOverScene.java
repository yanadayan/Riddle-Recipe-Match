package application.Results;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import application.view.SceneManager;
import application.util.MusicManager;

public class GameOverScene {

    // Define the default style as a constant (from CartScene)
       private static final String DEFAULT_BUTTON_STYLE = 
           "-fx-background-color: #f7efd5;" + 
           "-fx-border-color: #8e7a5a;" +
           "-fx-border-width: 3px;" +
           "-fx-font-family: 'Press Start 2P';" +
           "-fx-font-size: 12px;" +
           "-fx-text-fill: #5d2f23;" +
           "-fx-background-radius: 10;";

       private static final String HOVER_BUTTON_STYLE = 
           "-fx-background-color: #8e7a5a;" +
           "-fx-border-color: #f7efd5;" +
           "-fx-border-width: 3px;" +
           "-fx-font-family: 'Press Start 2P';" +
           "-fx-font-size: 12px;" +
           "-fx-text-fill: #f7efd5;" +
           "-fx-background-radius: 10;";
	
    private final Scene scene;

    public GameOverScene(SceneManager sceneManager, int level) {
    	MusicManager.play("gameover", false, 0.6);
        Pane root = new Pane();
        MusicManager.play("/music/Fail.wav", false, 0.6);
        String failGifPath = "/resources/Loss/L-L" + level + ".gif";
        ImageView gameOverImage;
        try {
            Image img = new Image(getClass().getResource(failGifPath).toExternalForm());
            gameOverImage = new ImageView(img);
            gameOverImage.setPreserveRatio(false);
        } catch (Exception ex) {
            System.err.println("Failed to load game over GIF: " + failGifPath);
            gameOverImage = new ImageView();
        }

        scene = new Scene(root, 900, 650);

        gameOverImage.fitWidthProperty().bind(scene.widthProperty());
        gameOverImage.fitHeightProperty().bind(scene.heightProperty());

        root.getChildren().add(gameOverImage);


        // Try Again Button styled like CartScene
        Button tryAgainBtn = new Button("Try Again");
        tryAgainBtn.setVisible(false); // Hide initially
        tryAgainBtn.setStyle(DEFAULT_BUTTON_STYLE);
        tryAgainBtn.setPrefWidth(139);
        tryAgainBtn.setPrefHeight(44);
        tryAgainBtn.setOnMouseEntered(e -> tryAgainBtn.setStyle(HOVER_BUTTON_STYLE));
        tryAgainBtn.setOnMouseExited(e -> tryAgainBtn.setStyle(DEFAULT_BUTTON_STYLE));
        tryAgainBtn.setOnAction(e -> sceneManager.showGameSceneForLevel(level));

        // Home Button styled like CartScene
        Button homeBtn = new Button("Home");
        homeBtn.setVisible(false); // Hide initially
        homeBtn.setStyle(DEFAULT_BUTTON_STYLE);
        homeBtn.setPrefWidth(139);
        homeBtn.setPrefHeight(44);
        homeBtn.setOnMouseEntered(e -> homeBtn.setStyle(HOVER_BUTTON_STYLE));
        homeBtn.setOnMouseExited(e -> homeBtn.setStyle(DEFAULT_BUTTON_STYLE));
        homeBtn.setOnAction(e -> sceneManager.showOutsideCafeScene());

        // Position them as before, or update coordinates as needed
        tryAgainBtn.layoutXProperty().bind(scene.widthProperty().multiply(0.40));
        tryAgainBtn.layoutYProperty().bind(scene.heightProperty().multiply(0.45));
        homeBtn.layoutXProperty().bind(scene.widthProperty().multiply(0.51));
        homeBtn.layoutYProperty().bind(scene.heightProperty().multiply(0.45));

        root.getChildren().addAll(tryAgainBtn, homeBtn);

        // Show buttons after 1 second
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            tryAgainBtn.setVisible(true);
            homeBtn.setVisible(true);
        });
        delay.play();
    }

    public Scene getScene() {
        return scene;
    }
}