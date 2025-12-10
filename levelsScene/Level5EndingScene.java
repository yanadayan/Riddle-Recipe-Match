package application.levelsScene;

import javafx.geometry.Pos;
import application.util.MusicManager;
import application.view.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class Level5EndingScene implements LevelScene {
    private final Scene scene;

    public Level5EndingScene(SceneManager sceneManager) {
    	MusicManager.stop();
    	MusicManager.play("order", false, 0.6);
        // Main layout: StackPane for gif + button overlay
        StackPane root = new StackPane();

        String gifPath = "/resources/level1EndingScene.gif";
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

        // Responsive binding
        endingGif.fitWidthProperty().bind(scene.widthProperty());
        endingGif.fitHeightProperty().bind(scene.heightProperty());

        root.getChildren().add(endingGif);

        // Bottom right button container
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(0, 30, 30, 0));
        buttonBox.setPrefSize(900, 650);

        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-font-size: 18px; -fx-background-color: #fcd701; -fx-text-fill: #5d2f23; -fx-font-family: 'Press Start 2P';");
        nextButton.setOnAction(e -> sceneManager.showOutsideCafeScene());

        buttonBox.getChildren().add(nextButton);

        root.getChildren().addAll(endingGif, buttonBox);

       
    }
@Override
    public Scene getScene() {
        return scene;
    }
}