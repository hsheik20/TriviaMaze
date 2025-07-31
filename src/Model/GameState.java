package Model;

/**
 * The {@code GameState} abstract class defines the structure for all
 * game states in the Trivia Maze application.
 *
 * Game states include various phases such as main menu, gameplay, and game over.
 * Subclasses of {@code GameState} must implement lifecycle methods that control
 * the behavior of the game when entering, exiting, and updating that state.
 *
 * This class is managed by {@link GameStateManager}, which handles transitions
 * between different states.
 *
 * @author Husein & Chan
 */
public abstract class GameState {
    /** Reference to the game state manager handling this state. */
    protected GameStateManager manager;
    /**
     * Default constructor for {@code GameState}.
     * Initializes an empty state; the {@code manager} is assigned during {@link #enter}.
     */
    public GameState() {}
    /**
     * Called when the game enters this state.
     * Use this method to initialize UI, logic, or reset values as needed.
     *
     * @param manager the {@link GameStateManager} managing this state
     */
    public abstract void enter(GameStateManager manager);
    /**
     * Called when the game exits this state.
     * Use this method to perform cleanup operations.
     */
    public abstract void exit();
    /**
     * Called periodically (if needed) to update state-specific logic.
     * Subclasses may override this for animations, timers, or game logic.
     */
    public abstract void update();
    /**
     * Returns a string identifier for the current game state.
     * Useful for debugging or conditional logic based on current state.
     *
     * @return the name of the state (e.g., "PLAYING", "MAIN_MENU", "GAME_OVER")
     */
    public abstract String getStateName();
}
