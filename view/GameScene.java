package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.text.Font;
import application.model.GameSession;
import application.model.GameBoard;
import application.model.Card;
import application.util.Utils;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.*;
import application.util.MusicManager;

public class GameScene {

    private final Scene scene;
    private final BorderPane root;
    private final GridPane boardGrid;
    private final SceneManager sceneManager;
    private final GameSession gameSession;

    private Label levelLabel, errorLabel, timerLabel;
    private Button restartButton;
    private GameBoard gameBoard;
    private ArrayList<Button> boardButtons;
    private Button card1Selected, card2Selected;
    private boolean gameReady = false;
    private boolean flippingAnimationActive = false;

    private int timeLimit = 60;
    private int rows, columns, totalCards;
    private double initialShowTime;
    private Timeline timerAnim;

    private Image cardBackImage;

    public GameScene(SceneManager sceneManager, GameSession gameSession) {
        this.sceneManager = sceneManager;
        this.gameSession = gameSession;
        root = new BorderPane();
        
        // --- MUSIC: Play appropriate background music based on game level ---
        int level = gameSession.getLevel();
        // DSA (HashMap) and OOP (MusicManager) in use:
        if (level == 1 || level == 2 || level == 3) {
            MusicManager.play("match13", true, 0.7); // "Match.wav"
        } else if (level == 4 || level == 5) {
            MusicManager.play("match45", true, 0.7); // "Match4&5.wav"
        }

        setLevelBackground(gameSession.getLevel());

        HBox topBar = new HBox(30);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(15);
        topBar.setBackground(new Background(new BackgroundFill(Color.web("#9d6b40"), CornerRadii.EMPTY, Insets.EMPTY)));
        topBar.setBorder(new Border(new BorderStroke(Color.web("#6d411b"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 5, 0))));

        levelLabel = new Label("Level: " + gameSession.getLevel());
        Utils.setLabelStyle(levelLabel, "#ffffff");
        errorLabel = new Label("Errors: 0");
        Utils.setLabelStyle(errorLabel, "#ffffff");
        timerLabel = new Label("Time: " + timeLimit);
        Utils.setLabelStyle(timerLabel, "#ffffff");
        topBar.getChildren().addAll(levelLabel, errorLabel, timerLabel);
        root.setTop(topBar);

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(15);
        boardGrid.setVgap(15);
        boardGrid.setPadding(new Insets(20));
        boardGrid.setBackground(Background.EMPTY);
        root.setCenter(boardGrid);

        HBox bottomBar = new HBox(20);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setSpacing(15);
        bottomBar.setBackground(new Background(new BackgroundFill(Color.web("#9d6b40"), CornerRadii.EMPTY, Insets.EMPTY)));
        bottomBar.setBorder(new Border(new BorderStroke(Color.web("#6d411b"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5, 0, 0, 0))));

        Button homeButton = Utils.makeStyledButton("HOME", "#fcd701", "#5d2f23", "#b59f00");
        homeButton.setOnAction(e -> {
            gameSession.reset();
            MusicManager.stop(); // Ensure music stops when going back to the main menu
            sceneManager.showOutsideCafeScene();
        });
        restartButton = Utils.makeStyledButton("RESTART", "#fcd701", "#5d2f23", "#b59f00");
        restartButton.setOnAction(e -> {
            gameSession.reset();
            MusicManager.stop(); // Ensure music stops before restarting and being immediately re-played
            sceneManager.showGameSceneForLevel(gameSession.getLevel());
        });

        bottomBar.getChildren().addAll(homeButton, restartButton);
        root.setBottom(bottomBar);

        scene = new Scene(root, 900, 650);

        startLevel();
    }

    private void setLevelBackground(int level) {
        String bgPath = switch (level) {
            case 1 -> "/resources/Background1.png"; 
            case 2 -> "/resources/Background2.png";
            case 3 -> "/resources/Background3.png";
            case 4 -> "/resources/Background4.png";
            case 5 -> "/resources/Background5.png";
            default -> "/resources/SuperDario.png";
        };

        URL url = getClass().getResource(bgPath);
        if (url == null) {
            System.err.println("❌ Background image not found. Falling back to dark blue.");
            root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            return;
        }
        Image bgImg = new Image(url.toExternalForm());
        BackgroundImage bImg = new BackgroundImage(
                bgImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 0, false),
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        root.setBackground(new Background(bImg));
        System.err.println("✅ Successfully loaded background via classpath.");
    }

    private void startLevel() {
        setLevelBackground(gameSession.getLevel());
        setupLevelSettings();
        int level = gameSession.getLevel();
        gameBoard = new GameBoard(level);
        cardBackImage = Utils.loadImage("backcover.png", getClass());
        if (cardBackImage == null) cardBackImage = new Image("file:ingredients/backcover.png");
        gameBoard.setupCards(getClass());
        card1Selected = null;
        card2Selected = null;
        gameReady = false;
        errorLabel.setText("Errors: 0");
        levelLabel.setText("Level: " + gameSession.getLevel());
        boardGrid.setDisable(false);
        createBoard(cardBackImage);
        startTimer();
        hideAllCardsAfterDelay(cardBackImage);
    }

    private void setupLevelSettings() {
        int level = gameSession.getLevel();
        switch (level) {
            case 1 -> { rows = 3; columns = 2; totalCards = 6;  initialShowTime = 1.5; }
            case 2 -> { rows = 4; columns = 2; totalCards = 8;  initialShowTime = 1.5; }
            case 3 -> { rows = 4; columns = 3; totalCards = 12; initialShowTime = 1; }
            case 4 -> { rows = 4; columns = 4; totalCards = 16; initialShowTime = 1; }
            case 5 -> { rows = 4; columns = 5; totalCards = 20; initialShowTime = 0.7; }
            default -> { rows = 4; columns = 5; totalCards = 20; initialShowTime = 0.7; }
        }
    }

    private void createBoard(Image cardBackImage) {
        boardGrid.getChildren().clear();
        boardButtons = new ArrayList<>();
        List<Card> cards = gameBoard.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Button btn = makeCardButton(i, cards.get(i), cardBackImage);
            boardButtons.add(btn);
            boardGrid.add(btn, i % columns, i / columns);
        }
    }

    private Button makeCardButton(int index, Card card, Image cardBackImage) {
        Button button = new Button();
        int cardWidth = 110, cardHeight = 150;
        button.setPrefSize(cardWidth, cardHeight);
        button.setFont(Font.font("Press Start 2P", 12));
        button.setPadding(Insets.EMPTY);
        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        button.setBorder(null);
        ImageView view = new ImageView(card.getImage());
        view.setFitWidth(cardWidth);
        view.setFitHeight(cardHeight);
        view.setPreserveRatio(false);
        view.setOpacity(1.0);
        button.setGraphic(view);
        button.setDisable(true);
        button.setStyle("-fx-opacity: 1.0;");

        // --- MUSIC: Play tap sound on flip (OOP: calls MusicManager) ---
        button.setOnAction(e -> {
            // This will now play simultaneously with BGM due to MusicManager update
            MusicManager.play("tap", false, 0.7); 
            if (!gameReady || button == card1Selected || button == card2Selected || flippingAnimationActive) return;
            ImageView currentView = (ImageView) button.getGraphic();
            if (currentView.getImage().equals(cardBackImage)) {
                if (card1Selected == null) {
                    card1Selected = button;
                    flipCardAnimation(button, true, cardBackImage, () -> {});
                } else {
                    boardGrid.setDisable(true);
                    card2Selected = button;
                    flipCardAnimation(button, true, cardBackImage, this::checkCards);
                }
            }
        });
        return button;
    }

    private void flipCardAnimation(Button button, boolean showFront, Image cardBackImage, Runnable onFinished) {
        flippingAnimationActive = true;
        int cardWidth = 110, cardHeight = 150;
        ScaleTransition shrink = new ScaleTransition(Duration.millis(175), button);
        shrink.setFromX(1.0);
        shrink.setToX(0.0);
        ScaleTransition expand = new ScaleTransition(Duration.millis(175), button);
        expand.setFromX(0.0);
        expand.setToX(1.0);

        shrink.setOnFinished(ev -> {
            int idx = boardButtons.indexOf(button);
            Card card = gameBoard.getCards().get(idx);
            Image newImage = showFront ? card.getImage() : cardBackImage;
            ImageView newView = new ImageView(newImage);
            newView.setFitWidth(cardWidth);
            newView.setFitHeight(cardHeight);
            newView.setPreserveRatio(false);
            newView.setOpacity(1.0);
            button.setGraphic(newView);
            button.setStyle("-fx-opacity: 1.0;");
        });

        expand.setOnFinished(ev -> {
            flippingAnimationActive = false;
            if (onFinished != null) onFinished.run();
        });

        SequentialTransition flipSeq = new SequentialTransition(shrink, expand);
        flipSeq.play();
    }

    private void hideAllCardsAfterDelay(Image cardBackImage) {
        gameReady = false;
        PauseTransition delay = new PauseTransition(Duration.seconds(initialShowTime));
        delay.setOnFinished(e -> {
            int cardWidth = 110, cardHeight = 150;
            for (Button b : boardButtons) {
                ImageView view = new ImageView(cardBackImage);
                view.setFitWidth(cardWidth);
                view.setFitHeight(cardHeight);
                view.setPreserveRatio(false);
                view.setOpacity(1.0);
                b.setGraphic(view);
                b.setDisable(false);
                b.setStyle("-fx-opacity: 1.0;");
            }
            PauseTransition reEnableDelay = new PauseTransition(Duration.millis(300));
            reEnableDelay.setOnFinished(ev -> gameReady = true);
            reEnableDelay.play();
        });
        delay.play();
    }

    private void checkCards() {
        int index1 = boardButtons.indexOf(card1Selected);
        int index2 = boardButtons.indexOf(card2Selected);

        boolean isMatch = gameBoard.checkMatch(index1, index2);

        if (isMatch) {
            card1Selected.setDisable(true);
            card2Selected.setDisable(true);
            card1Selected.setStyle("-fx-opacity: 1.0;");
            card2Selected.setStyle("-fx-opacity: 1.0;");
            card1Selected = null;
            card2Selected = null;
            boardGrid.setDisable(false);
            if (gameBoard.getMatchesFound() == gameBoard.getTotalMatches()) {
                gameReady = false;
                if (timerAnim != null) timerAnim.stop();
                
                // MODIFICATION: Stop music on win
                MusicManager.stop(); 
                
                sceneManager.showWinScene(gameSession.getLevel());
                return;
            }
        } else {
            gameBoard.incrementErrorCount(); 
            errorLabel.setText("Errors: " + gameBoard.getErrorCount());
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> {
                flippingAnimationActive = true;
                final boolean[] finishedArr = {false, false};
                Runnable checkFinish = () -> {
                    if (finishedArr[0] && finishedArr[1]) {
                        card1Selected.setStyle("-fx-opacity: 1.0;");
                        card2Selected.setStyle("-fx-opacity: 1.0;");
                        card1Selected.setDisable(false);
                        card2Selected.setDisable(false);
                        card1Selected = null;
                        card2Selected = null;
                        flippingAnimationActive = false;
                        gameReady = true;
                        boardGrid.setDisable(false);
                    }
                };
                flipCardAnimation(card1Selected, false, cardBackImage, () -> { finishedArr[0] = true; checkFinish.run(); });
                flipCardAnimation(card2Selected, false, cardBackImage, () -> { finishedArr[1] = true; checkFinish.run(); });
            });
            delay.play();
        }
    }

    private void setBoardOpacityInactive() {
        if (boardButtons != null) {
            for (Button b : boardButtons) {
                b.setStyle("-fx-opacity: 0.3;");
                b.setDisable(true);
            }
        }
    }

    private void setTimerRed(boolean on) {
        if (on) {
            timerLabel.setTextFill(Color.web("#ff3333"));
        } else {
            timerLabel.setTextFill(Color.web("#ffffff"));
        }
    }

    private void startTimer() {
        if (timerAnim != null) timerAnim.stop();
        timerLabel.setText("Time: " + timeLimit);
        setTimerRed(false);

        final int[] timeLeftArr = { timeLimit };
        timerAnim = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeftArr[0]--;
            timerLabel.setText("Time: " + timeLeftArr[0]);
            if (timeLeftArr[0] <= 0) {
                timerAnim.stop();
                timerLabel.setText("Time: 0");
                setBoardOpacityInactive();
                
                // MODIFICATION: Stop music on game over
                MusicManager.stop();
                
                sceneManager.showGameOverScene(gameSession.getLevel());
            }
            setTimerRed(timeLeftArr[0] <= 10);
        }));
        timerAnim.setCycleCount(Animation.INDEFINITE);
        timerAnim.play();
    }

    public Scene getScene() { return scene; }
}