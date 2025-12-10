package application.levelsScene;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.net.URL;

import application.util.MusicManager;
import application.view.SceneManager;

public class Level2OrderScene implements LevelScene {
    private final Scene scene;

    // Button Styles
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

    public Level2OrderScene(SceneManager sceneManager) {
        Pane root = new Pane();
        MusicManager.play("order", false, 0.6);
        // Responsive GIF background
        URL url = getClass().getResource("/resources/level2OrderScene.gif");
        if (url == null) throw new RuntimeException("Level1OrderScene.gif not found! Check path and spelling.");
        Image gifImg = new Image(url.toExternalForm());
        ImageView gifView = new ImageView(gifImg);
        gifView.setPreserveRatio(false);

        // Bind imageview to root pane size
        root.widthProperty().addListener((obs, oldV, newV) -> gifView.setFitWidth(newV.doubleValue()));
        root.heightProperty().addListener((obs, oldV, newV) -> gifView.setFitHeight(newV.doubleValue()));

        scene = new Scene(root, 900, 650);
        gifView.setFitWidth(scene.getWidth());
        gifView.setFitHeight(scene.getHeight());
        root.getChildren().add(gifView);

        // "MATCH NOW" Button
        Button matchNowBtn = new Button("MATCH NOW");
        matchNowBtn.setStyle(DEFAULT_BUTTON_STYLE);

        matchNowBtn.setPrefWidth(150);
        matchNowBtn.setPrefHeight(44);

        // Center the button
        double btnXRatio = 0.50; // Center horizontally
        double btnYRatio = 0.58; // Adjust for your GIF layout (experiment)

        root.widthProperty().addListener((obs, oldV, newV) -> matchNowBtn.setLayoutX(newV.doubleValue() * btnXRatio - matchNowBtn.getPrefWidth() / 2));
        root.heightProperty().addListener((obs, oldV, newV) -> matchNowBtn.setLayoutY(newV.doubleValue() * btnYRatio));
        matchNowBtn.setLayoutX(scene.getWidth() * btnXRatio - matchNowBtn.getPrefWidth() / 2);
        matchNowBtn.setLayoutY(scene.getHeight() * btnYRatio);

        // Button Hover Effect
        matchNowBtn.setOnMouseEntered(e -> matchNowBtn.setStyle(HOVER_BUTTON_STYLE));
        matchNowBtn.setOnMouseExited(e -> matchNowBtn.setStyle(DEFAULT_BUTTON_STYLE));

        matchNowBtn.setOnAction(e -> sceneManager.showGameSceneForLevel(2));

        // DELAY the button appearance by 1.5 seconds
        matchNowBtn.setVisible(false); // Hide initially
        PauseTransition delay = new PauseTransition(Duration.seconds(2.8));
        delay.setOnFinished(e -> matchNowBtn.setVisible(true));
        delay.play();

        root.getChildren().add(matchNowBtn);
    }
@Override
    public Scene getScene() { return scene; }
}