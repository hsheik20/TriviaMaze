package Model;

/**
 * Represents the different states the game can be in.
 * This enum helps manage the game's flow and logic.
 */
public enum GameState {
    /**
     * The game is currently in progress.
     * This is the default state when a new game begins.
     */
    PLAYING,

    /**
     * The game is temporarily suspended.
     * This state is typically entered when the user pauses the game.
     */
    PAUSED,

    /**
     * The game has ended.
     * This state is entered when the player has lost or the game's objective has been met.
     */
    GAME_OVER
}