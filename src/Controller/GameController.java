// Controller/GameController.java
package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class GameController implements PropertyChangeListener {

    // Model (mutable so we can start a new game)
    private Game game;
    private GameStateManager gsm;

    // View (owns all panels)
    private final GameView view;

    // Last pending question context from "askQuestion"
    private Door pendingDoor;
    private Question pendingQuestion;

    public GameController(final Game initialGame,
                          final GameStateManager initialGsm,
                          final GameView view) {
        this.game = Objects.requireNonNull(initialGame);
        this.gsm  = Objects.requireNonNull(initialGsm);
        this.view = Objects.requireNonNull(view);

        // Listen to model events
        game.addListener(this);
        gsm.addListener(this);

        wireMenus();
        wireMazePanel();
        wireQuestionPanel();
        wirePausePanel();

        // Show main menu first
        view.showScreen(GameView.Screen.MAIN_MENU);
        refreshHUDAndGrid();
    }

    /* ======================= Wiring ======================= */

    private void wireMenus() {
        view.getMainMenu().onNewGame(this::handleNewGame);
        view.getMainMenu().onLoadGame(this::handleLoadGame);
        view.getMainMenu().onInstructions(() ->
                JOptionPane.showMessageDialog(view,
                        "Move with WASD/arrow keys.\n" +
                                "Doors ask questions.\n" +
                                "Hints/Skip depend on difficulty.\n" +
                                "Reach the exit. If no path remains, you lose.",
                        "Instructions",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        view.getMainMenu().onAbout(() ->
                JOptionPane.showMessageDialog(view,
                        "Retro Trivia Maze by Husein & team.",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        view.getMainMenu().onQuit(() -> System.exit(0));
    }

    private void wireMazePanel() {
        view.getMazePanel().onMove(dir -> game.attemptMove(dir));
        view.getMazePanel().onPause(() -> gsm.pause());
    }

    private void wireQuestionPanel() {
        view.getQuestionPanel().onSubmit(answer -> {
            if (pendingDoor == null || pendingQuestion == null) return;

            final boolean correct = pendingQuestion.isCorrect(answer);
            game.handleAnswer(pendingDoor, correct);

            if (!correct) {
                // If still has attempts and the door is not blocked, stay on the question panel
                final int left = game.getAttemptsLeft(pendingDoor);
                if (!pendingDoor.isBlocked() && (left == Integer.MAX_VALUE || left > 0)) {
                    view.getQuestionPanel().showWrongAndUpdate(left);
                    return; // remain on QUESTION screen
                }
                // else: either blocked or no path; state events will drive the screen
            }
            // If correct, a "playerMoved" event will arrive and switch to Maze/Victory.
        });

        view.getQuestionPanel().onHint(() -> {
            if (pendingQuestion == null) return;
            final String hint = game.useHint(pendingQuestion);
            if (hint != null) {
                view.getQuestionPanel().showHint(hint, game.getHintsLeft());
            } else {
                JOptionPane.showMessageDialog(view, "No hint available.", "Hint",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.getQuestionPanel().onSkip(() -> {
            if (pendingDoor == null) return;
            if (game.canSkip()) {
                game.skipQuestion(pendingDoor); // this will likely lead back to MAZE via events
            } else {
                JOptionPane.showMessageDialog(view,
                        "Skipping is disabled for this difficulty.",
                        "Skip", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void wirePausePanel() {
        view.getPausePanel().onResume(() -> gsm.resume());
        view.getPausePanel().onMainMenu(() -> view.showScreen(GameView.Screen.MAIN_MENU));
        view.getPausePanel().onQuit(() -> System.exit(0));
    }

    /* ======================= Events ======================= */

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();
        switch (name) {
            case "state" -> {
                final GameState s = (GameState) evt.getNewValue();
                if (s == GameState.PAUSED) {
                    view.showScreen(GameView.Screen.PAUSE);
                } else if (s == GameState.PLAYING) {
                    view.showScreen(GameView.Screen.MAZE);
                } else if (s == GameState.GAME_OVER) {
                    view.showScreen(GameView.Screen.GAME_OVER);
                }
            }
            case "askQuestion" -> {
                // Show the question panel with full context
                final QuestionRequest req = (QuestionRequest) evt.getNewValue();
                pendingDoor = req.door();
                pendingQuestion = req.question();

                final int attempts = game.getAttemptsLeft(pendingDoor);
                final boolean canHint = game.canUseHint(pendingQuestion);
                final boolean canSkip = game.canSkip();

                view.getQuestionPanel().setQuestion(pendingQuestion, attempts, canHint, canSkip);
                view.getMazePanel().setDoorAttemptsLabel(attempts);
                view.showScreen(GameView.Screen.QUESTION);
            }
            case "playerMoved" -> {
                view.getMazePanel().setDoorAttemptsLabel(null); // clear
                refreshHUDAndGrid();
                if (game.getMaze().isAtExit()) {
                    view.showScreen(GameView.Screen.VICTORY);
                } else {
                    view.showScreen(GameView.Screen.MAZE);
                }
            }
            case "doorBlocked" -> {
                view.getMazePanel().setDoorAttemptsLabel(null);
                // Door got blocked (skip or out-of-tries). Just refresh and go back to maze.
                refreshHUDAndGrid();
                view.showScreen(GameView.Screen.MAZE);
            }
        }
    }

    /* ======================= Menu Actions ======================= */

    private void handleNewGame() {
        final Object[] options = {"Easy (3x3)", "Normal (4x4)", "Hard (5x5)"};
        final int choice = JOptionPane.showOptionDialog(
                view, "Choose Difficulty", "New Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);

        final DifficultySettings settings = switch (choice) {
            case 0 -> DifficultyPresets.easy();
            case 2 -> DifficultyPresets.hard();
            default -> DifficultyPresets.normal();
        };

        startNewGame(settings);
    }

    private void handleLoadGame() {
        JOptionPane.showMessageDialog(view,
                "Load game not implemented yet (use serialization).",
                "Load", JOptionPane.INFORMATION_MESSAGE);
    }

    /* ======================= Session Management ======================= */

    /**
     * Builds a fresh model (Maze/Player/Game/GSM) from the chosen difficulty
     * and rebinds listeners + view panels.
     */
    public void startNewGame(final DifficultySettings settings) {
        // 1) Detach listeners from old model
        if (game != null) game.removeListener(this);
        if (gsm != null)  gsm.removeListener(this);

        // 2) Build new model
        final questionFactory qf = new questionFactory("jdbc:sqlite:lib/trivia.db");
        final Maze maze = new Maze(settings.getMazeHeight(), settings.getMazeWidth(), qf);
        final Player player = new Player();
        gsm = new GameStateManager();
        game = new Game(maze, player, gsm, settings);

        // 3) Listen again
        game.addListener(this);
        gsm.addListener(this);



        // 5) Reset pending Q
        pendingDoor = null;
        pendingQuestion = null;

        // 6) Show Maze
        refreshHUDAndGrid();
        view.showScreen(GameView.Screen.MAZE);
    }

    /* ======================= Helpers ======================= */

    private void refreshHUDAndGrid() {
        view.getMazePanel().setHud(
                game.getPlayer().getX(),
                game.getPlayer().getY(),
                game.getHintsLeft()
        );
        view.getMazePanel().render(game.getMaze(), game.getPlayer());
    }
}
