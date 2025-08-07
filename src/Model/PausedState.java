package Model;

class PauseState extends GameState {
    public void enter(GameStateManager manager) {
        this.manager = manager;
    }

    public void exit() {}
    public void update() {}
    public String getStateName() { return "PAUSE"; }
}

