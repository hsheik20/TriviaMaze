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
    private GameState myCurrentState;
    /** Reference to the main game window. */
    private final Game myGame; // Made final as it's set once in constructor
    /** The player instance used throughout the game. */
    private final Player myPlayer; // Made final
    /** The maze representing the current game map. */
    private final Maze myMaze; // Made final
    /** The trivia system responsible for questions and answers. */
    private final Trivia myTrivia; // Made final

    /** Default rows for the maze if not specified. */
    private static final int DEFAULT_MAZE_ROWS = 5;
    /** Default columns for the maze if not specified. */
    private static final int DEFAULT_MAZE_COLS = 5;

    /**
     * Constructs a new {@code GameStateManager} and initializes core components.
     *
     * @param theGame The main {@link Game} frame which this manager controls. Cannot be null.
     * @throws IllegalArgumentException if theGame is null.
     */
    public GameStateManager(final Game theGame) {
        if (theGame == null) {
            throw new IllegalArgumentException("The Game instance cannot be null.");
        }
        this.myGame = theGame;
        this.myPlayer = new Player();
        // Initialize Maze with default dimensions, assuming a no-arg constructor is not available
        this.myMaze = new Maze(DEFAULT_MAZE_ROWS, DEFAULT_MAZE_COLS);
        this.myTrivia = new Trivia();

        // Set the initial state, e.g., to MainMenuState
        // This should be done after all components are initialized.
        setState(new MainMenuState()); // Assuming MainMenuState exists
    }

    /**
     * Transitions from the current game state to a new one.
     * The existing state's {@code exit()} method is called, followed by the
     * new state's {@code enter()} method.
     * Also triggers the UI to show the panel corresponding to the new state's name.
     *
     * @param theNewState The new {@link GameState} to transition into. Cannot be null.
     * @throws IllegalArgumentException if theNewState is null.
     */
    public void setState(final GameState theNewState) {
        if (theNewState == null) {
            throw new IllegalArgumentException("The new GameState cannot be null.");
        }

        if (myCurrentState != null) {
            myCurrentState.exit();
        }
        myCurrentState = theNewState;
        myCurrentState.enter(this); // Pass 'this' GameStateManager to the new state

        // This method assumes the 'Game' class has a method 'showPanel'
        // that can switch the displayed JPanel based on the state name.
        // You would need to implement 'public void showPanel(String panelName)' in your Game class.
        myGame.showPanel(theNewState.getStateName());
    }

    /**
     * Returns the current active game state.
     *
     * @return The active {@link GameState}.
     */
    public GameState getCurrentState() {
        return myCurrentState;
    }

    /**
     * Returns the main game window.
     *
     * @return The {@link Game} instance.
     */
    public Game getGame() {
        return myGame;
    }

    /**
     * Returns the player object.
     *
     * @return The {@link Player} instance.
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * Returns the maze map.
     *
     * @return The {@link Maze} instance.
     */
    public Maze getMaze() {
        return myMaze;
    }

    /**
     * Returns the trivia system.
     *
     * @return The {@link Trivia} instance.
     */
    public Trivia getTrivia() {
        return myTrivia;
    }
}
