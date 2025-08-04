package Model;

import javax.swing.JOptionPane;

/**
 * The {@code GameOverState} class represents the state of the game
 * when it has ended—either by the player winning or losing.
 * It displays a dialog summarizing the game and prompts the player
 * to either restart or return to the main menu.
 *
 * This class extends {@link GameState} and interacts with the
 * {@link GameStateManager} to transition to the appropriate next state.
 *
 * @author Husein & Chan
 */
public class GameOverState extends GameState {

    /** The name of this game state. */
    private static final String STATE_NAME = "GAME_OVER";
    /** Message displayed when the player wins. */
    private static final String WIN_MESSAGE = "Congratulations! You won!";
    /** Message displayed when the player loses. */
    private static final String LOSE_MESSAGE = "Game Over! Try again?";
    /** Title for the game over dialog. */
    private static final String DIALOG_TITLE = "Game Over";
    /** Prompt asking the player to play again. */
    private static final String PLAY_AGAIN_PROMPT = "\n\nWould you like to play again?";
    /** Format string for displaying game details. */
    private static final String DETAILS_FORMAT = "Final Score: %d\nQuestions Answered: %d\nPosition Reached: (%d, %d)";


    /** Indicates whether the player has won the game. */
    private final boolean myWon;

    /**
     * Constructs a new {@code GameOverState} with the specified outcome.
     *
     * @param theWon {@code true} if the player won; {@code false} if the player lost.
     */
    public GameOverState(final boolean theWon) {
        this.myWon = theWon;
    }

    /**
     * Called when entering the Game Over state.
     * Displays a dialog summarizing the game and prompts the user to replay or return to menu.
     *
     * @param theManager The {@link GameStateManager} controlling game states. Cannot be null.
     * @throws IllegalArgumentException if theManager is null.
     */
    @Override
    public void enter(final GameStateManager theManager) {
        if (theManager == null) {
            throw new IllegalArgumentException("GameStateManager cannot be null when entering GameOverState.");
        }
        this.myManager = theManager; // Assign the manager to the protected field from GameState

        final String message = myWon ? WIN_MESSAGE : LOSE_MESSAGE;
        final String details = getGameDetails(theManager);

        final int result = JOptionPane.showConfirmDialog(
                myManager.getGame(), // Assuming GameStateManager has a getGame() method that returns the JFrame
                message + "\n" + details + PLAY_AGAIN_PROMPT,
                DIALOG_TITLE,
                JOptionPane.YES_NO_OPTION,
                myWon ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Reset player and maze before transitioning to PlayingState
            myManager.getPlayer().resetPosition();
            // Assuming GameStateManager has a getMaze() method or similar way to access the maze
            // If maze is managed directly by GameStateManager, you'd call myManager.getMaze().reset();
            // For now, assuming PlayingState handles maze initialization/reset.
            myManager.setState(new PlayingState());
        } else {
            myManager.setState(new MainMenuState());
        }
    }

    /**
     * Generates a string detailing the player's final game stats,
     * including score, questions answered, and final position.
     *
     * @param theManager The {@link GameStateManager} from which to fetch player data. Cannot be null.
     * @return A formatted string of game details.
     * @throws IllegalArgumentException if theManager is null.
     */
    private String getGameDetails(final GameStateManager theManager) {
        if (theManager == null) {
            throw new IllegalArgumentException("GameStateManager cannot be null when getting game details.");
        }
        final Player player = theManager.getPlayer();
        // Assuming player is never null from GameStateManager, or handle it if it can be.
        return String.format(
                DETAILS_FORMAT,
                player.getScore(),
                player.getQuestionsAnswered(),
                player.getX(),
                player.getY()
        );
    }

    /**
     * Method called when exiting the Game Over state.
     * No specific cleanup is needed in this case.
     */
    @Override
    public void exit() {
        // No cleanup needed for this state.
    }

    /**
     * Updates logic for this state. No periodic updates needed here.
     */
    @Override
    public void update() {
        // No periodic updates required for this state.
    }

    /**
     * Returns the name of the current state.
     *
     * @return A string representing the state name ("GAME_OVER").
     */
    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    /**
     * Returns whether the player won the game.
     *
     * @return {@code true} if the player won, {@code false} otherwise.
     */
    public boolean isWon() {
        return myWon;
    }
}
