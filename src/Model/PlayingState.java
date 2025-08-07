package Model;

class PlayingState extends GameState {
    public void enter(GameStateManager manager) {
        this.manager = manager;
        manager.getMaze().generateMaze();
        manager.getPlayer().resetPosition();
    }

    public void exit() {}
    public void update() {}
    public String getStateName() { return "MAZE"; }
}
