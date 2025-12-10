package application.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimeManager {
    private Timeline timer;
    private int timeLimit;
    private Runnable onExpire;
    private Runnable onUpdate;

    public TimeManager(int timeLimit, Runnable onExpire, Runnable onUpdate) {
        this.timeLimit = timeLimit;
        this.onExpire = onExpire;
        this.onUpdate = onUpdate;
    }

    public void start() {
        final int[] timeLeftArr = { timeLimit };
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeftArr[0]--;
            if (onUpdate != null) onUpdate.run();
            if (timeLeftArr[0] <= 0) {
                if (timer != null) timer.stop();
                if (onExpire != null) onExpire.run();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stop() {
        if (timer != null) timer.stop();
    }
}