// View/AppGui.java  (acts as a launcher; no JFrame here)
package View;

import Controller.GameController;

import Model.*;

import javax.swing.SwingUtilities;

public final class AppGui {
    private AppGui() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Seed with any difficulty; a *real* game will be created via New Game.
            DifficultySettings settings = DifficultyPresets.normal();

            questionFactory qf = new questionFactory("jdbc:sqlite:lib/trivia.db");
            Maze maze = new Maze(settings.getMazeHeight(), settings.getMazeWidth(), qf);
            Player player = new Player();
            GameStateManager gsm = new GameStateManager();
            Game game = new Game(maze, player, gsm, settings);

            GameView view = new GameView();           // this IS the JFrame
            GameController controller = new GameController(game, gsm, view);

            view.setVisible(true);

           
        });
    }
}
