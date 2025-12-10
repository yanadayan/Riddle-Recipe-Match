package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import application.util.MusicManager;

public class CartScene {
    private final Scene scene;

    // Define the default style as a constant
    private static final String DEFAULT_BUTTON_STYLE = 
        "-fx-background-color: #f7efd5; " + // Light Cream background
        "-fx-border-color: #8e7a5a;" +       // Brown border
        "-fx-border-width: 3px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-font-size: 12px;" +
        "-fx-text-fill: #5d2f23;" +         // Dark brown text
        "-fx-background-radius: 10;";
        
    // Define the hover style
    private static final String HOVER_BUTTON_STYLE = 
        "-fx-background-color: #8e7a5a; " + // Change to Brown background on hover
        "-fx-border-color: #f7efd5;" +       // Change border to Light Cream
        "-fx-border-width: 3px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-font-size: 12px;" +
        "-fx-text-fill: #f7efd5;" +         // Change text to Light Cream
        "-fx-background-radius: 10;";

    public CartScene(SceneManager sceneManager) {
        Pane root = new Pane();
    	MusicManager.play("cafe", true, 0.8);

        // Responsive background (3rdscene.gif)
        URL url = getClass().getResource("/resources/3rdscene.gif");
        if (url == null) throw new RuntimeException("3rdscene.gif not found! Check path and spelling.");
        Image bgImg = new Image(url.toExternalForm());
        ImageView bgView = new ImageView(bgImg);
        bgView.setPreserveRatio(false);

        // Bind imageview to root size
        root.widthProperty().addListener((obs, oldV, newV) -> bgView.setFitWidth(newV.doubleValue()));
        root.heightProperty().addListener((obs, oldV, newV) -> bgView.setFitHeight(newV.doubleValue()));

        // Initial fit
        scene = new Scene(root, 900, 650);
        bgView.setFitWidth(scene.getWidth());
        bgView.setFitHeight(scene.getHeight());
        root.getChildren().add(bgView);

        // "Buy Here!" Button
        Button cartButton = new Button("Buy Here!");
        
        // --- APPLY DEFAULT STYLE ---
        cartButton.setStyle(DEFAULT_BUTTON_STYLE);
        
        cartButton.setPrefWidth(139);
        cartButton.setPrefHeight(44);

        // Responsive placement of the button (ADJUSTED to be centered between the cart's tires)
        double cartXRatio = 0.20; 
        double cartYRatio = 0.68; 
        
        root.widthProperty().addListener((obs, oldV, newV) -> cartButton.setLayoutX(newV.doubleValue() * cartXRatio));
        root.heightProperty().addListener((obs, oldV, newV) -> cartButton.setLayoutY(newV.doubleValue() * cartYRatio));
        cartButton.setLayoutX(scene.getWidth() * cartXRatio);
        cartButton.setLayoutY(scene.getHeight() * cartYRatio);
        
        // --- ADD HOVER EFFECTS ---
        
        // Mouse Entered: Apply the HOVER_BUTTON_STYLE
        cartButton.setOnMouseEntered(e -> cartButton.setStyle(HOVER_BUTTON_STYLE));
        
        // Mouse Exited: Revert to the DEFAULT_BUTTON_STYLE
        cartButton.setOnMouseExited(e -> cartButton.setStyle(DEFAULT_BUTTON_STYLE));

        cartButton.setOnAction(e -> sceneManager.showOrderSceneForLevel(1));
        root.getChildren().add(cartButton);
    }

    public Scene getScene() { return scene; }
}