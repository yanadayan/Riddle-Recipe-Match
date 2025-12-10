package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.beans.binding.Bindings;
import java.net.URL;
import application.util.MusicManager;

public class HomePageScene {
    private final Scene scene;

    public HomePageScene(SceneManager sceneManager) {
    	MusicManager.play("homepage", true, 0.8);
        // Create root stack for background and UI overlay
        StackPane root = new StackPane();
        

        // --- Background image ---
        URL url = getClass().getResource("/resources/homepagefinal.gif");
        if (url == null) throw new RuntimeException("homepagefinal.gif not found! Check path and spelling.");
        Image bgImg = new Image(url.toExternalForm());
        ImageView bgView = new ImageView(bgImg);
        bgView.setPreserveRatio(false);
        bgView.setFitWidth(1500);
        bgView.setFitHeight(1000);

        // Make background auto-resize
        bgView.fitWidthProperty().bind(root.widthProperty());
        bgView.fitHeightProperty().bind(root.heightProperty());

        // --- Responsive Button column ---
        VBox buttonColumn = new VBox();
        buttonColumn.setAlignment(Pos.CENTER_RIGHT);

        Button startButton = makePixelButton("START");
        startButton.setOnAction(e -> sceneManager.showStoryScene());

        Button optionsButton = makePixelButton("OPTIONS");
        optionsButton.setOnAction(e -> sceneManager.showOptionScene());
        HBox optionsBox = new HBox(12, optionsButton);
        optionsBox.setAlignment(Pos.CENTER_LEFT);

       

        buttonColumn.getChildren().addAll(startButton, optionsBox);

        // Bind vertical and horizontal spacing to window size
        buttonColumn.spacingProperty().bind(root.heightProperty().divide(18)); // Responsive spacing between buttons

        // Responsive padding: top, right, bottom, left
        buttonColumn.paddingProperty().bind(Bindings.createObjectBinding(() -> {
            double h = root.getHeight();
            double w = root.getWidth();
            return new Insets(h * 0.07, w * 0.14, h * 0.00, w * 0.04);
        }, root.widthProperty(), root.heightProperty()));

        // --- Make buttons responsive in size ---
        for (javafx.scene.Node node : buttonColumn.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                bindButtonSize(btn, root);
            } else if (node instanceof HBox) { // For optionsBox
                for (javafx.scene.Node child : ((HBox) node).getChildren()) {
                    if (child instanceof Button) {
                        bindButtonSize((Button) child, root);
                    }
                }
            }
        }

        // Place the button column on the right of your scene/root node:
        BorderPane overlayLayout = new BorderPane();
        overlayLayout.setPrefSize(1000, 1000);
        overlayLayout.setRight(buttonColumn);
        BorderPane.setAlignment(buttonColumn, Pos.CENTER_RIGHT);

        // Add overlayLayout to your root node
        root.getChildren().addAll(bgView, overlayLayout);

        scene = new Scene(root, 900, 650);
    }

    // Helper to create stylized pixel-like buttons
    private Button makePixelButton(String label) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Press Start 2P", 18));
        btn.setTextFill(Color.web("#6d5831"));
        btn.setPrefWidth(300);
        btn.setPrefHeight(60);
        btn.setBackground(new Background(new BackgroundFill(Color.web("#f7efd5"), new CornerRadii(8), Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(Color.web("#493b30"), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(3))));
        btn.setStyle("-fx-effect: dropshadow(gaussian,#e2d4b8,5,0.7,2,2); -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setBackground(new Background(new BackgroundFill(Color.web("#e8d29e"), new CornerRadii(8), Insets.EMPTY))));
        btn.setOnMouseExited(e -> btn.setBackground(new Background(new BackgroundFill(Color.web("#f7efd5"), new CornerRadii(8), Insets.EMPTY))));
        return btn;
    }

    // Bind button size and font responsively to root window
    private void bindButtonSize(Button btn, Region root) {
        btn.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
        btn.prefHeightProperty().bind(root.heightProperty().multiply(0.08));
        btn.fontProperty().bind(Bindings.createObjectBinding(() -> {
            double size = Math.max(12, root.getHeight() * 0.031);
            return Font.font("Press Start 2P", size);
        }, root.heightProperty()));
    }

    public Scene getScene() { return scene; }
}