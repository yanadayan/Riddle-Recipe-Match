package application.util;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import java.io.InputStream;

public class Utils {
    public static void setLabelStyle(Label label, String color) {
        label.setTextFill(Color.web(color));
        label.setFont(Font.font("Press Start 2P", 12));
        label.setStyle("-fx-font-weight: bold;");
    }

    public static Button makeStyledButton(String text, String bgColor, String txtColor, String borderColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Press Start 2P", 12));
        button.setTextFill(Color.web(txtColor));
        button.setBackground(new javafx.scene.layout.Background(
            new javafx.scene.layout.BackgroundFill(Color.web(bgColor), javafx.scene.layout.CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        button.setPadding(new javafx.geometry.Insets(8, 16, 8, 16));
        button.setBorder(new javafx.scene.layout.Border(
            new javafx.scene.layout.BorderStroke(Color.web(borderColor), javafx.scene.layout.BorderStrokeStyle.SOLID, javafx.scene.layout.CornerRadii.EMPTY, new javafx.scene.layout.BorderWidths(4))));
        button.setPrefHeight(32);
        button.setPrefWidth(130);
        return button;
    }

    public static Image loadImage(String fileName, Class<?> loaderClass) {
        String imagePath = "/ingredients/" + fileName;
        try {
            InputStream stream = loaderClass.getResourceAsStream(imagePath.replace(" ", "%20"));
            if (stream == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            return new Image(stream);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            return null;
        }
    }
}