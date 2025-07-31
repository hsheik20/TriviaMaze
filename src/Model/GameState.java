package Model;

public abstract class GameState {
    protected GameStateManager manager;

    public GameState() {}

    public abstract void enter(GameStateManager manager);
    public abstract void exit();
    public abstract void update();
    public abstract String getStateName();
}
