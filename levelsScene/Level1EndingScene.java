package application.levelsScene;

import application.util.MusicManager;
import application.levelsScene.LevelScene;
import application.view.SceneManager;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.net.URL;

public class Level1EndingScene implements LevelScene {
    private final Scene scene;

    // Button Styles (copied from Level1OrderScene)
    private static final String DEFAULT_BUTTON_STYLE =
        "-fx-background-color: #e57836;" +
        "-fx-text-fill: #f2e7cf;" +
        "-fx-font-size: 12px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #7c4f29;" +
        "-fx-border-width: 3px;";
    private static final String HOVER_BUTTON_STYLE =
        "-fx-background-color: #7c4f29;" +
        "-fx-text-fill: #e57836;" +
        "-fx-font-size: 12px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #e57836;" +
        "-fx-border-width: 3px;";

    public Level1EndingScene(SceneManager sceneManager) {
        Pane root = new Pane();
        MusicManager.play("order", false, 0.6);
        // Responsive GIF background
        String gifPath = "/resources/Level1Ending.gif";
        ImageView endingGif;
        try {
            Image img = new Image(getClass().getResource(gifPath).toExternalForm());
            endingGif = new ImageView(img);
            endingGif.setPreserveRatio(false);
        } catch (Exception ex) {
            endingGif = new ImageView();
            System.err.println("Failed to load GIF: " + gifPath);
        }

        scene = new Scene(root, 900, 650);

        endingGif.fitWidthProperty().bind(scene.widthProperty());
        endingGif.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(endingGif);

        // "Next" Button with "MATCH NOW" style and placement
        Button nextButton = new Button("Next");
        nextButton.setStyle(DEFAULT_BUTTON_STYLE);
        nextButton.setPrefWidth(150);
        nextButton.setPrefHeight(44);

        // Placement matches the order scene button
        double btnXRatio = 0.50; // center horizontally
        double btnYRatio = 0.58; // vertical position (experiment to perfectly match)

        root.widthProperty().addListener((obs, oldV, newV) -> nextButton.setLayoutX(newV.doubleValue() * btnXRatio - nextButton.getPrefWidth() / 2));
        root.heightProperty().addListener((obs, oldV, newV) -> nextButton.setLayoutY(newV.doubleValue() * btnYRatio));
        nextButton.setLayoutX(scene.getWidth() * btnXRatio - nextButton.getPrefWidth() / 2);
        nextButton.setLayoutY(scene.getHeight() * btnYRatio);

        nextButton.setOnMouseEntered(e -> nextButton.setStyle(HOVER_BUTTON_STYLE));
        nextButton.setOnMouseExited(e -> nextButton.setStyle(DEFAULT_BUTTON_STYLE));
        nextButton.setOnAction(e -> sceneManager.showUnlockSceneForLevel(2));

        // Make button appear after 2 seconds
        nextButton.setVisible(false);
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> nextButton.setVisible(true));
        delay.play();

        root.getChildren().add(nextButton);
    }
    @Override
    public Scene getScene() {
        return scene;
    }
}