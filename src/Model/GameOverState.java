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
    /** Indicates whether the player has won the game. */
    private boolean won;
    /**
     * Constructs a new {@code GameOverState} with the specified outcome.
     *
     * @param won {@code true} if the player won; {@code false} if the player lost
     */
    public GameOverState(boolean won) {
        this.won = won;
    }
    /**
     * Called when entering the Game Over state.
     * Displays a dialog summarizing the game and prompts the user to replay or return to menu.
     *
     * @param manager the {@link GameStateManager} controlling game states
     */
    public void enter(GameStateManager manager) {
        this.manager = manager;

        String message = won ? "Congratulations! You won!" : "Game Over! Try again?";
        String details = getGameDetails(manager);

        int result = JOptionPane.showConfirmDialog(
                manager.getGame(),
                message + "\n" + details + "\n\nWould you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                won ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            manager.setState(new PlayingState());
        } else {
            manager.setState(new MainMenuState());
        }
    }
    /**
     * Generates a string detailing the player's final game stats,
     * including score, questions answered, and final position.
     *
     * @param manager the {@link GameStateManager} from which to fetch player data
     * @return a formatted string of game details
     */
    private String getGameDetails(GameStateManager manager) {
        Player player = manager.getPlayer();
        return String.format(
                "Final Score: %d\nQuestions Answered: %d\nPosition Reached: (%d, %d)",
                player.getScore(),
                player.getQuestionsAnswered(),
                player.getX(),
                player.getY()
        );
    }
    /**
     * Method called when exiting the Game Over state.
     * No cleanup is needed in this case.
     */
    public void exit() {}
    /**
     * Updates logic for this state. No periodic updates needed here.
     */
    public void update() {}
    /**
     * Returns the name of the current state.
     *
     * @return a string representing the state name ("MAIN_MENU")
     */
    public String getStateName() {
        return "MAIN_MENU";
    }
    /**
     * Returns whether the player won the game.
     *
     * @return {@code true} if the player won, {@code false} otherwise
     */
    public boolean isWon() {
        return won;
    }
}
