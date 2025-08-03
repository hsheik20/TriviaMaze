package Model;

/**
 * The {@code GameStateManager} class manages the current state of the game,
 * including transitions between different {@link GameState} implementations.
 *
 * It also serves as a central point for accessing shared game components such as
 * the {@link Player}, {@link Maze}, and {@link Trivia} instances.
 *
 * The state manager interacts with the main {@link Game} class to control
 * UI panel switching based on the active game state.
 *
 * This class enables the implementation of a flexible state machine pattern
 * for controlling different phases of the game (e.g., main menu, playing, game over).
 *
 * @author Husein & Chan
 */
public class GameStateManager {
    /** The current active state of the game. */
    private GameState currentState;
    /** Reference to the main game window. */
    private Game game;
    /** The player instance used throughout the game. */
    private Player player;
    /** The maze representing the current game map. */
    private Maze maze;
    /** The trivia system responsible for questions and answers. */
    private Trivia trivia;
    /**
     * Constructs a new {@code GameStateManager} and initializes core components.
     *
     * @param game the main {@link Game} frame which this manager controls
     */
    public GameStateManager(Game game) {
        this.game = game;
        this.player = new Player();
        this.maze = new Maze();
        this.trivia = new Trivia();
    }
    /**
     * Transitions from the current game state to a new one.
     * The existing state's {@code exit()} method is called, followed by the
     * new state's {@code enter()} method.
     * Also triggers the UI to show the panel corresponding to the new state's name.
     *
     * @param newState the new {@link GameState} to transition into
     */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
        game.showPanel(newState.getStateName());
    }
    /**
     * Returns the current active game state.
     *
     * @return the active {@link GameState}
     */
    public GameState getCurrentState() {
        return currentState;
    }
    /**
     * Returns the main game window.
     *
     * @return the {@link Game} instance
     */
    public Game getGame() {
        return game;
    }
    /**
     * Returns the player object.
     *
     * @return the {@link Player} instance
     */
    public Player getPlayer() {
        return player;
    }
    /**
     * Returns the maze map.
     *
     * @return the {@link Maze} instance
     */
    public Maze getMaze() {
        return maze;
    }
    /**
     * Returns the trivia system.
     *
     * @return the {@link Trivia} instance
     */
    public Trivia getTrivia() {
        return trivia;
    }
}
