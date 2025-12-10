package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import application.util.MusicManager;

public class outsideCafe {
    private final Scene scene;

    public outsideCafe(SceneManager sceneManager) {
    	MusicManager.play("cafe", true, 0.8);
        StackPane root = new StackPane();

        // --- Responsive background (2ndscene.gif) ---
        URL url = getClass().getResource("/resources/2ndscene.gif");
        if (url == null) throw new RuntimeException("2ndscene.gif not found! Check path and spelling.");
        Image bgImg = new Image(url.toExternalForm());
        ImageView bgView = new ImageView(bgImg);
        bgView.setPreserveRatio(false);

        // Bind imageview to scene resizing
        root.widthProperty().addListener((obs, oldV, newV) -> bgView.setFitWidth(newV.doubleValue()));
        root.heightProperty().addListener((obs, oldV, newV) -> bgView.setFitHeight(newV.doubleValue()));

        root.getChildren().add(bgView);
        scene = new Scene(root, 900, 650);

        // Initial fit
        bgView.setFitWidth(scene.getWidth());
        bgView.setFitHeight(scene.getHeight());

        // After a delay, move to CartScene
        PauseTransition delay = new PauseTransition(Duration.seconds(3.0));
        delay.setOnFinished(e -> sceneManager.showCartScene());
        delay.play();
    }

    public Scene getScene() { return scene; }
}