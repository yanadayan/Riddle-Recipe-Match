package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color; 
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
import application.util.MusicManager;

/**
 * OptionScene: Shows a dialog with ON/OFF music buttons and a close ("X") button.
 * - OOP: OptionController encapsulates mute/resume logic and button states.
 * - DSA: ArrayList stores all option buttons for easy group management.
 * - Responsive: Gif background auto-fits window, all UI elements are centered and scale.
 */
public class OptionScene {
    private final Scene scene;
    private final OptionController optionController;

    // --- Styling Constants (Reduced font size for pixel art, as previously requested) ---
    private static final String FONT_SIZE = "12px";
    
    // Style for the unselected button (default state)
    private static final String DEFAULT_STYLE =
        "-fx-background-color: #f7efd5;" +
        "-fx-border-color: #8e7a5a;" +
        "-fx-border-width: 3px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-font-size: " + FONT_SIZE + ";" +
        "-fx-text-fill: #5d2f23;" +
        "-fx-background-radius: 6;";
        
    // Style for the selected button (ON/OFF state)
    private static final String SELECTED_STYLE =
        "-fx-background-color: #8e7a5a;" + 
        "-fx-border-color: #f7efd5;" +     
        "-fx-border-width: 3px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-font-size: " + FONT_SIZE + ";" +
        "-fx-text-fill: #f7efd5;" +       
        "-fx-background-radius: 6;";
    
    // Style for a button when hovered over, but is NOT the currently selected state
    private static final String HOVER_STYLE = SELECTED_STYLE; 
    
    // Base style for buttons like "X" (using the original 22px font size constants for consistency, but will use 16px font in code)
    private static final String BUTTON_STYLE = 
        "-fx-background-color: #f7efd5;" +
        "-fx-border-color: #8e7a5a;" +
        "-fx-border-width: 3px;" +
        "-fx-font-family: 'Press Start 2P';" +
        "-fx-font-size: 16px;" + // Keep 'X' larger for visibility
        "-fx-text-fill: #5d2f23;" +
        "-fx-background-radius: 6;";


    // Encapsulates button logic & state (OOP: Encapsulation/Composition)
    public static class OptionController {
        // Now using MusicManager.isMusicPlaying() since it's defined
        private boolean musicOn = MusicManager.isMusicPlaying(); 
        private final List<Button> buttons = new ArrayList<>(); // DSA: ArrayList
        private final Button btnOn;
        private final Button btnOff;

        public OptionController(Button btnOn, Button btnOff) {
            this.btnOn = btnOn;
            this.btnOff = btnOff;
            buttons.add(btnOn);
            buttons.add(btnOff);
            
            // If music hasn't started, default state to OFF
            if (!MusicManager.isMusicPlaying()) {
                this.musicOn = false;
            } else {
                this.musicOn = true;
            }

            updateButtonStyles(); 
            initListeners();
        }

        private void initListeners() {
            btnOn.setOnAction(e -> {
                if (!musicOn) {
                    musicOn = true;
                    // Note: If music was never played, resume() does nothing, but this is the right command.
                    MusicManager.resume();
                    updateButtonStyles();
                }
            });
            btnOff.setOnAction(e -> {
                if (musicOn) {
                    musicOn = false;
                    MusicManager.pause();
                    updateButtonStyles();
                }
            });
        }

        private void updateButtonStyles() {
            btnOn.setStyle(musicOn ? SELECTED_STYLE : DEFAULT_STYLE);
            btnOff.setStyle(!musicOn ? SELECTED_STYLE : DEFAULT_STYLE);
        }
    }

    public OptionScene(SceneManager sceneManager) {
        StackPane root = new StackPane();

        // --- Responsive GIF background ---
        ImageView bgView;
        try {
            Image bgImg = new Image(getClass().getResource("/resources/Option.png").toExternalForm());
            bgView = new ImageView(bgImg);
            bgView.setPreserveRatio(false); // Stretch, do not keep ratio
            bgView.fitWidthProperty().bind(root.widthProperty());
            bgView.fitHeightProperty().bind(root.heightProperty());
        } catch (Exception ex) {
            bgView = new ImageView();
        }
        root.getChildren().add(bgView);

        // --- Option Dialog Content ---
        VBox dialogBox = new VBox(24); // Spacing between text and buttons
        dialogBox.setAlignment(Pos.TOP_CENTER);
        
        

        // 2. ON/OFF Buttons (Fixed size and font size: 12px)
        Button btnOn = new Button("ON");
        Button btnOff = new Button("OFF");
        btnOn.setFont(Font.font("Press Start 2P", 12)); 
        btnOff.setFont(Font.font("Press Start 2P", 12)); 
        btnOn.setPrefSize(110, 46);
        btnOff.setPrefSize(110, 46);

        optionController = new OptionController(btnOn, btnOff);
        
        // --- Add Hover Effects to ON/OFF buttons ---
        btnOn.setOnMouseEntered(e -> { if (!optionController.musicOn) btnOn.setStyle(HOVER_STYLE); });
        btnOn.setOnMouseExited(e -> optionController.updateButtonStyles()); 
        
        btnOff.setOnMouseEntered(e -> { if (optionController.musicOn) btnOff.setStyle(HOVER_STYLE); });
        btnOff.setOnMouseExited(e -> optionController.updateButtonStyles()); 


        HBox buttonBox = new HBox(28, btnOn, btnOff);
        buttonBox.setAlignment(Pos.CENTER);

        // Add both the label and the button box
        dialogBox.getChildren().addAll( buttonBox);

        // Place the dialog content over the center of the dialog frame in the image
        StackPane.setAlignment(dialogBox, Pos.CENTER);
        // Adjusted vertical margin (120 -> 140) to shift content further down as requested ("lower the buttons")
        StackPane.setMargin(dialogBox, new Insets(410, 0, 0, -20)); 


        // 3. "X" Close button (Fixed size and style)
        Button btnClose = new Button("✖");
        btnClose.setFont(Font.font("Press Start 2P", 16));
        btnClose.setPrefSize(38, 38); 
        btnClose.setStyle(BUTTON_STYLE);
        btnClose.setOnAction(e -> sceneManager.showHomePageScene());

        // Align button to the top right of the screen
        StackPane.setAlignment(btnClose, Pos.TOP_RIGHT);
        // Adjusted margins (170, 202 -> 140, 190) to move the X button closer to the top-right corner of the dialog box
        StackPane.setMargin(btnClose, new Insets(280, 450, 0, 0));

        // Add dialog content & close button on top of background
        root.getChildren().addAll(dialogBox, btnClose);

        // Responsive Scene
        scene = new Scene(root, 900, 650);
    }

    public Scene getScene() { return scene; }
}