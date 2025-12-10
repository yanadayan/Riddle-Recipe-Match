package application.story;
import application.util.MusicManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import java.util.Stack;
import java.util.List;
import java.util.Arrays;
import application.view.SceneManager;

public class StoryStackScene {
    private final Scene scene;
    private final Stack<Integer> historyStack;
    private int currentIndex = 0;
    private final List<String> gifPaths;
    private final StackPane root;

    private ImageView gifView;

    public StoryStackScene(SceneManager sceneManager) {
      	MusicManager.play("storyline", false, 0.6);
    	root = new StackPane();
        root.setAlignment(Pos.CENTER);

        gifPaths = Arrays.asList(
            "/resources/story1.gif",
            "/resources/story2.gif",
            "/resources/story3.gif"
        );
        historyStack = new Stack<>();
        historyStack.push(0);

        scene = new Scene(root, 900, 650);
        showFrame(currentIndex);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeGif());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeGif());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT:
                    goForward();
                    break;
                case LEFT:
                    goBack();
                    break;
                case ENTER:
                    sceneManager.showOutsideCafeScene();
                    break;
                default:
                    break;
            }
        });
    }

    private void showFrame(int index) {
        root.getChildren().clear();
        Image gifImage = new Image(getClass().getResource(gifPaths.get(index)).toExternalForm());
        gifView = new ImageView(gifImage);
        gifView.setPreserveRatio(false);

        // Bind to scene width/height (responsive!)
        gifView.fitWidthProperty().bind(scene.widthProperty());
        gifView.fitHeightProperty().bind(scene.heightProperty());

        root.getChildren().add(gifView);
    }

    private void resizeGif() {
        if (gifView != null) {
            gifView.setFitWidth(scene.getWidth());
            gifView.setFitHeight(scene.getHeight());
        }
    }

    private void goForward() {
        if (currentIndex < gifPaths.size() - 1) {
            currentIndex++;
            historyStack.push(currentIndex);
            showFrame(currentIndex);
        }
    }

    private void goBack() {
        if (historyStack.size() > 1) {
            historyStack.pop();
            currentIndex = historyStack.peek();
            showFrame(currentIndex);
        }
    }

    public Scene getScene() { return scene; }
}