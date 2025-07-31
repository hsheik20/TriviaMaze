package Model;

import javax.swing.JOptionPane;

public class GameOverState extends GameState {
    private boolean won;

    public GameOverState(boolean won) {
        this.won = won;
    }

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

    public void exit() {}
    public void update() {}
    public String getStateName() {
        return "MAIN_MENU";
    }

    // Getter for won status
    public boolean isWon() {
        return won;
    }
}
