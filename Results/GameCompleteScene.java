package application.Results;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import application.view.SceneManager;
import application.util.MusicManager;

public class GameCompleteScene {
    private final Scene scene;

    public GameCompleteScene(SceneManager sceneManager) {
        MusicManager.play("/music/FinalWin.wav", false, 0.8); // customize as needed!
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // --- Responsive GIF Background ---
        ImageView gifView;
        try {
            // Change to your completion GIF path!
            Image gifImage = new Image(getClass().getResource("/resources/complete.gif").toExternalForm());
            gifView = new ImageView(gifImage);
        } catch (Exception ex) {
            gifView = new ImageView();
        }
        gifView.setPreserveRatio(false);
        gifView.fitWidthProperty().bind(root.widthProperty());
        gifView.fitHeightProperty().bind(root.heightProperty());

        
        // --- Styled button (like GameOverScene/CartScene) ---
        String DEFAULT_BUTTON_STYLE =
            "-fx-background-color: #f7efd5;" +
            "-fx-border-color: #8e7a5a;" +
            "-fx-border-width: 3px;" +
            "-fx-font-family: 'Press Start 2P';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #5d2f23;" +
            "-fx-background-radius: 10;";

        String HOVER_BUTTON_STYLE =
            "-fx-background-color: #8e7a5a;" +
            "-fx-border-color: #f7efd5;" +
            "-fx-border-width: 3px;" +
            "-fx-font-family: 'Press Start 2P';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #f7efd5;" +
            "-fx-background-radius: 10;";

        Button homeBtn = new Button("Back to Home");
        homeBtn.setStyle(DEFAULT_BUTTON_STYLE);
        homeBtn.setPrefWidth(170);
        homeBtn.setPrefHeight(44);
        homeBtn.setFont(Font.font("Press Start 2P", 12));
        homeBtn.setOnMouseEntered(e -> homeBtn.setStyle(HOVER_BUTTON_STYLE));
        homeBtn.setOnMouseExited(e -> homeBtn.setStyle(DEFAULT_BUTTON_STYLE));
        homeBtn.setOnAction(e -> sceneManager.showHomePageScene());

        // --- Center the button below GIF/message ---
        VBox vbox = new VBox(18); // Space between GIF/message and button
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(0, 0, 0, 0));

        
        vbox.getChildren().add(homeBtn);

       
        vbox.setTranslateY(170);

        root.getChildren().addAll(gifView, vbox);

        scene = new Scene(root, 900, 650);
    }

    public Scene getScene() { return scene; }
}