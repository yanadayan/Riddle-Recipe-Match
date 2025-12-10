package application.levelsScene;

import application.view.SceneManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import application.util.MusicManager;
public class GenericUnlockScene {
    private final Scene scene;

    public GenericUnlockScene(SceneManager sceneManager, int unlockedLevel) {
        
    	String unlockGifPath = "/resources/l" + unlockedLevel + "Unlock.gif";
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 900, 650);
        MusicManager.play("unlock", false, 0.8);
        
        ImageView bgView;
        try {
            Image bgImg = new Image(getClass().getResource(unlockGifPath).toExternalForm());
            bgView = new ImageView(bgImg);
            bgView.setPreserveRatio(false);
        } catch (Exception ex) {
            System.err.println("Unlock GIF not found: " + unlockGifPath);
            bgView = new ImageView();
        }
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(bgView);

        
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

        Button nextButton = new Button("Next");
        nextButton.setVisible(false);
        nextButton.setPrefWidth(139);
        nextButton.setPrefHeight(44);
        nextButton.setFont(Font.font("Press Start 2P", 12));
        nextButton.setStyle(DEFAULT_BUTTON_STYLE);

        nextButton.setOnMouseEntered(e -> nextButton.setStyle(HOVER_BUTTON_STYLE));
        nextButton.setOnMouseExited(e -> nextButton.setStyle(DEFAULT_BUTTON_STYLE));
        nextButton.setOnAction(e -> sceneManager.showOrderSceneForLevel(unlockedLevel));

        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);
        root.getChildren().add(nextButton);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> nextButton.setVisible(true));
        delay.play();

        this.scene = scene;
        
    }

    public Scene getScene() { return scene; }
}