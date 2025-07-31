package Model;

public class GameStateManager {
    private GameState currentState;
    private Game game;
    private Player player;
    private Maze maze;
    private Trivia trivia;

    public GameStateManager(Game game) {
        this.game = game;
        this.player = new Player();
        this.maze = new Maze();
        this.trivia = new Trivia();
    }

    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
        game.showPanel(newState.getStateName());
    }

    // Getters
    public GameState getCurrentState() {
        return currentState;
    }
    public Game getGame() {
        return game;
    }
    public Player getPlayer() {
        return player;
    }
    public Maze getMaze() {
        return maze;
    }
    public Trivia getTrivia() {
        return trivia;
    }
}

