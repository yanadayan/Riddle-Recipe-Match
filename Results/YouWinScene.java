package application.Results;

import application.view.SceneManager;
import application.model.GameSession;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import application.util.MusicManager;

public class YouWinScene {
    private final Scene scene;

    public YouWinScene(SceneManager sceneManager, GameSession gameSession) {
    	MusicManager.play("success", false, 0.6);

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 900, 650);

        int currLevel = gameSession.getLevel();
        String winGifPath = "/resources/Win/W-L" + currLevel + ".gif";

        // Fully responsive GIF with no aspect ratio or padding constraints
        ImageView winGifView;
        try {
            Image winGif = new Image(getClass().getResource(winGifPath).toExternalForm());
            winGifView = new ImageView(winGif);
            winGifView.setPreserveRatio(false);
        } catch (Exception ex) {
            System.err.println("Failed to load win GIF: " + winGifPath);
            winGifView = new ImageView();
        }
        winGifView.fitWidthProperty().bind(scene.widthProperty());
        winGifView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(winGifView);

        

        final String DEFAULT_BUTTON_STYLE =
            "-fx-background-color: #f7efd5;" +
            "-fx-border-color: #8e7a5a;" +
            "-fx-border-width: 3px;" +
            "-fx-font-family: 'Press Start 2P';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #5d2f23;" +
            "-fx-background-radius: 10;";
        final String HOVER_BUTTON_STYLE =
            "-fx-background-color: #8e7a5a;" +
            "-fx-border-color: #f7efd5;" +
            "-fx-border-width: 3px;" +
            "-fx-font-family: 'Press Start 2P';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #f7efd5;" +
            "-fx-background-radius: 10;";

        Button btnNext = new Button("Next");
        btnNext.setVisible(false);
        btnNext.setPrefWidth(139);
        btnNext.setPrefHeight(44);
        btnNext.setFont(Font.font("Press Start 2P", 12));
        btnNext.setStyle(DEFAULT_BUTTON_STYLE);

        btnNext.setOnMouseEntered(e -> btnNext.setStyle(HOVER_BUTTON_STYLE));
        btnNext.setOnMouseExited(e -> btnNext.setStyle(DEFAULT_BUTTON_STYLE));
        btnNext.setOnAction(e -> {
            int justCompletedLevel = gameSession.getLevel();
            sceneManager.advanceLevel();
            if (justCompletedLevel < 5) {
                sceneManager.showEndingSceneForLevel(justCompletedLevel);
            } else {
                sceneManager.showGameCompletedScene();
            }
        });

        StackPane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        root.getChildren().add(btnNext);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> btnNext.setVisible(true));
        delay.play();

        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }
}