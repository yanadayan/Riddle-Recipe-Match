package application.view;
import application.Results.GameCompleteScene;
import javafx.stage.Stage;
import application.Results.GameOverScene;
import application.Results.YouWinScene;
import application.levelsScene.*;
import application.model.GameSession;
import application.story.StoryStackScene;

/** Updated to use LevelQueueManager queue for sequential level progression */
public class SceneManager {
    private final Stage stage;
    private LevelQueueManager levelQueueManager;
    private HomePageScene homePageScene;
    private outsideCafe outsideCafeScene;
    private CartScene cartScene;
    private GameSession gameSession;
    private GameScene gameScene;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.gameSession = new GameSession();
        showHomePageScene();
    }

    public void showHomePageScene() {
        if (homePageScene == null) homePageScene = new HomePageScene(this);
        stage.setScene(homePageScene.getScene());
    }

    public void showOptionScene() {
        OptionScene optionScene = new OptionScene(this);
        stage.setScene(optionScene.getScene());
    }
    public void showStoryScene() {
        StoryStackScene storyScene = new StoryStackScene(this);
        stage.setScene(storyScene.getScene());
    }
    public void showOutsideCafeScene() {
        outsideCafeScene = new outsideCafe(this);
        stage.setScene(outsideCafeScene.getScene());
    }

    public void showCartScene() {
        cartScene = new CartScene(this);
        stage.setScene(cartScene.getScene());
    }

    // --- LEVEL QUEUE SETUP ---
    public void setupLevelQueue() {
        levelQueueManager = new LevelQueueManager();
        // Add all scenes (order: Order, Ending) as needed
        levelQueueManager.addLevelScene(new Level1OrderScene(this));
        levelQueueManager.addLevelScene(new Level1EndingScene(this));
        levelQueueManager.addLevelScene(new Level2OrderScene(this));
        levelQueueManager.addLevelScene(new Level2EndingScene(this));
        levelQueueManager.addLevelScene(new Level3OrderScene(this));
        levelQueueManager.addLevelScene(new Level3EndingScene(this));
        levelQueueManager.addLevelScene(new Level4OrderScene(this));
        levelQueueManager.addLevelScene(new Level4EndingScene(this));
        levelQueueManager.addLevelScene(new Level5OrderScene(this));
        levelQueueManager.addLevelScene(new Level5EndingScene(this));
    }

    /** Show next scene in the queue; on exhaustion, show game completed scene */
    public void showNextLevelScene() {
        if (levelQueueManager == null || !levelQueueManager.hasMoreLevels()) {
            showGameCompletedScene();
            return;
        }
        LevelScene nextScene = levelQueueManager.getNextLevelScene();
        stage.setScene(nextScene.getScene());
    }

    public void showOrderSceneForLevel(int level) {
        switch (level) {
            case 1 -> stage.setScene(new Level1OrderScene(this).getScene());
            case 2 -> stage.setScene(new Level2OrderScene(this).getScene());
            case 3 -> stage.setScene(new Level3OrderScene(this).getScene());
            case 4 -> stage.setScene(new Level4OrderScene(this).getScene());
            case 5 -> stage.setScene(new Level5OrderScene(this).getScene());
            default -> showHomePageScene();
        }
    }

    // MATCHING GAME
    public void showGameSceneForLevel(int level) {
        if (gameSession.getLevel() != level) {
            gameSession.reset();
            for(int i=1; i<level; i++) gameSession.nextLevel();
        }
        gameScene = new GameScene(this, gameSession);
        stage.setScene(gameScene.getScene());
    }

    // YOU WIN
    public void showWinScene(int level) {
        stage.setScene(new YouWinScene(this, gameSession).getScene());
    }

    // ENDING (after win) (can be replaced with queue logic)
    public void showEndingSceneForLevel(int level) {
        switch (level) {
            case 1 -> stage.setScene(new Level1EndingScene(this).getScene());
            case 2 -> stage.setScene(new Level2EndingScene(this).getScene());
            case 3 -> stage.setScene(new Level3EndingScene(this).getScene());
            case 4 -> stage.setScene(new Level4EndingScene(this).getScene());
            case 5 -> stage.setScene(new Level5EndingScene(this).getScene());
            default -> showHomePageScene();
        }
    }
    
    // UNLOCK SCENE
    public void showUnlockSceneForLevel(int unlockedLevel) {
        stage.setScene(new GenericUnlockScene(this, unlockedLevel).getScene());
    }

    public void showGameCompletedScene() {
        GameCompleteScene completedScene = new GameCompleteScene(this);
        stage.setScene(completedScene.getScene());
    }
    // GAME OVER
    public void showGameOverScene(int level) {
        stage.setScene(new GameOverScene(this, level).getScene());
    }

    public void advanceLevel() {
        gameSession.nextLevel();
    }

    public void resetLevel() {
        gameSession.reset();
    }

    public int getCurrentLevel() {
        return gameSession.getLevel();
    }
}