package application.model;

public class GameSession {
    private int level = 1;

    public int getLevel() { return level; }
    public void nextLevel() { level++; }
    public void reset() { level = 1; }
}