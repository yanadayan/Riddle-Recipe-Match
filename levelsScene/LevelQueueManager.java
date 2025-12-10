package application.levelsScene;

import java.util.LinkedList;
import java.util.Queue;

/** Queue for level scene management */
public class LevelQueueManager {
    private Queue<LevelScene> levelQueue;

    public LevelQueueManager() {
        levelQueue = new LinkedList<>();
    }

    public void addLevelScene(LevelScene scene) {
        levelQueue.offer(scene);
    }

    public LevelScene getNextLevelScene() {
        return levelQueue.poll();
    }

    public boolean hasMoreLevels() {
        return !levelQueue.isEmpty();
    }
}