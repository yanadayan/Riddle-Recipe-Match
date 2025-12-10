package application.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.Font;

public class MatchCardsDialog extends Stage {
    public interface NextAction {
        void onNext();
    }

    public MatchCardsDialog(Stage owner, NextAction nextAction) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
        setResizable(false);
        setTitle("Level Complete!");

        ImageView bgImage = new ImageView(new Image(getClass().getResource("/resources/matchcards.png").toExternalForm()));
        bgImage.setFitWidth(520);
        bgImage.setFitHeight(400);

        Button nextBtn = new Button("NEXT");
        nextBtn.setFont(Font.font("Press Start 2P", 24));
        nextBtn.setTextFill(javafx.scene.paint.Color.web("#7c4f29"));
        nextBtn.setPrefWidth(136);
        nextBtn.setPrefHeight(48);
        nextBtn.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.web("#e57836"), new CornerRadii(8), Insets.EMPTY)));
        nextBtn.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.TRANSPARENT,
                BorderStrokeStyle.NONE, new CornerRadii(8), BorderWidths.DEFAULT)));
        nextBtn.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.28), 4, 0.0, 2, 2);");

        nextBtn.setOnAction(e -> {
            close();
            if (nextAction != null) nextAction.onNext();
        });
        StackPane.setAlignment(nextBtn, Pos.BOTTOM_CENTER);
        StackPane.setMargin(nextBtn, new Insets(0, 0, 38, 0));

        Button xBtn = new Button("✖");
        xBtn.setFont(Font.font("Press Start 2P", 20));
        xBtn.setBackground(Background.EMPTY);
        xBtn.setTextFill(javafx.scene.paint.Color.web("#7c4f29"));
        xBtn.setOnAction(e -> close());
        StackPane.setAlignment(xBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(xBtn, new Insets(20, 28, 0, 0));

        StackPane dialogPane = new StackPane(bgImage, nextBtn, xBtn);
        dialogPane.setPrefSize(520, 400);

        Scene scene = new Scene(dialogPane);
        setScene(scene);
    }
}