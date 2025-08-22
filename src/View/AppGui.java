package View;

import Controller.GameController;

import Model.*;

import javax.swing.SwingUtilities;

/**
 * The main launcher class for the Maze of Doom game.
 * This class serves as the entry point of the application, responsible for
 * initializing the model, view, and controller components and linking them
 * together to start the game's GUI.
 *
 * @author Husein & Chan
 */
public final class AppGui {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppGui() {
    }

    /**
     * The main method and entry point of the application.
     * It sets up the Model-View-Controller (MVC) components for the game
     * and displays the main game window.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Seed with a default difficulty; a *real* game will be
            // created and configured by the user via the "New Game" menu.
            DifficultySettings settings = DifficultyPresets.normal();

            // 1. Create the Model components
            questionFactory qf = new questionFactory("jdbc:sqlite:lib/trivia.db");
            Maze maze = new Maze(settings.getMazeHeight(), settings.getMazeWidth(), qf);
            Player player = new Player();
            GameStateManager gsm = new GameStateManager();
            Game game = new Game(maze, player, gsm, settings);

            // 2. Create the View component (the main JFrame)
            GameView view = new GameView();

            // 3. Create the Controller and link all components
            GameController controller = new GameController(game, gsm, view);

            // 4. Make the game window visible to the user
            view.setVisible(true);
        });
    }
}