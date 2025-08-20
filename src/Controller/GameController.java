package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import View.audio.Sounds;

/**
 * This represents the controller class starts new game and rebinds listeners
 */
public class GameController implements PropertyChangeListener {

    /** The current game model */
     private Game game;
    /** Manages game states and fires state property changes*/
    private GameStateManager gsm;

    /** The main swing window containing all panels*/
    private final GameView view;

    /**The door player is attempting to pass*/
    private Door pendingDoor;
    /**The question currently being asked to the player*/
    private Question pendingQuestion;

    /**This is a check for if player has won*/
    private boolean hasWon = false;

    /**
     * This builds a new controller around initial model and view
     *
     * @param initialGame the initial game instance to attach
     * @param initialGsm the initial game state manager to attach
     * @param view the game view
     * @throws NullPointerException if any argument is null
     */
    public GameController(final Game initialGame,
                          final GameStateManager initialGsm,
                          final GameView view) {
        this.game = Objects.requireNonNull(initialGame);
        this.gsm  = Objects.requireNonNull(initialGsm);
        this.view = Objects.requireNonNull(view);

        // listening to model events
        game.addListener(this);
        gsm.addListener(this);

        //Wiring the UI
        wireMenus();
        wireMazePanel();
        wireQuestionPanel();
        wirePausePanel();

        //Menu bar click actions
        view.getCustomMenuBar().setOnNewGame(this::handleNewGame);
        view.getCustomMenuBar().setOnPauseToggle(this::togglePause);
        view.getCustomMenuBar().setOnSaveGame(this::saveGame);
        view.getCustomMenuBar().setOnQuitGame(this::quit);
        view.getCustomMenuBar().setOnLoadGame(this::handleLoadGame);
        view.getCustomMenuBar().setOnShowInstructions(this::showInstructionsDialog);
        view.getCustomMenuBar().setOnShowAbout(this::showAboutDialog);


        //showing initial screen
        view.showScreen(GameView.Screen.MAIN_MENU);
        view.setVisible(true);

        Sounds.MENU.play();
    }

    /**
     * Wiring main menu buttons to controller actions
     */

    private void wireMenus() {
        view.getMainMenu().onNewGame(() -> { Sounds.MENU.play(); handleNewGame(); });
        view.getMainMenu().onLoadGame(() -> {
            Sounds.MENU.play();
            handleLoadGame();
        });
        view.getMainMenu().onInstructions(() -> {
            Sounds.MENU.play();
            JOptionPane.showMessageDialog(view,
                    "Move with arrow keys.\n" +
                            "Doors ask questions.\n" +
                            "Hints/Skip depend on difficulty.\n" +
                            "Reach the exit. If no path remains, you lose.",
                    "Instructions",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        view.getMainMenu().onAbout(() -> {
            Sounds.MENU.play();
            JOptionPane.showMessageDialog(view,
                    "Retro Trivia Maze by Husein & team.",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        view.getMainMenu().onQuit(() -> { Sounds.MENU.play(); System.exit(0); });
    }

    /**
     * This wires player movement pause toggle from maze panel to model
     */
    private void wireMazePanel() {
        // Arrow-key and button movement
        view.getMazePanel().onMove(this::handleMove);
        // Pause button
        view.getMazePanel().onPause(this::togglePause);
    }

    private void showInstructionsDialog() {
        JOptionPane.showMessageDialog(view,
                "Move with arrow keys.\n" +
                        "Doors ask questions.\n" +
                        "Hints/Skip depend on difficulty.\n" +
                        "Reach the exit. If no path remains, you lose.",
                "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(view,
                "Retro Trivia Maze by Husein & team.",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * This wires answering, hinting, skipping and cheating actions for question panel.
     */
    // inside GameController

    private void wireQuestionPanel() {
        view.getQuestionPanel().setOnSubmit(this::handleQuestionSubmit);
        view.getQuestionPanel().setOnHint(this::handleQuestionHint);
        view.getQuestionPanel().setOnSkip(this::handleQuestionSkip);
        view.getQuestionPanel().setOnCheat(this::handleQuestionCheat);
    }

    private void handleQuestionSubmit(final String theAnswer) {
        if (!hasPendingQuestion()) return;

        final boolean theCorrect = pendingQuestion.isCorrect(theAnswer);

        if (theCorrect) Sounds.CORRECT.play();
        else            Sounds.INCORRECT.play();

        System.out.println("Controller is sending door to Game.handleAnswer: " + pendingDoor);
        game.handleAnswer(pendingDoor, theCorrect);

        if (!theCorrect) {
            final int theLeft = game.getAttemptsLeft(pendingDoor);
            if (!pendingDoor.isBlocked() && theLeft > 0) {
                view.getQuestionPanel().showWrongAndUpdate(theLeft);
                return; // stay on question screen
            }
        }

        clearPendingQuestion();
    }

    private void handleQuestionHint() {
        if (pendingQuestion == null) return;

        final String theHint = game.useHint(pendingQuestion);
        if (theHint != null) {
            view.getQuestionPanel().showHint(theHint, game.getHintsLeft());
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "No hint available.",
                    "Hint",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void handleQuestionSkip() {
        if (pendingDoor == null) return;

        if (game.canSkip()) {
            game.skipQuestion(pendingDoor);
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Skipping is disabled for this difficulty.",
                    "Skip",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void handleQuestionCheat() {
        if (pendingQuestion == null) return;
        view.getQuestionPanel().showCheat(pendingQuestion.cheatToken());
    }

// ===== helpers =====

    /** @return true if both pendingDoor and pendingQuestion are set. */
    private boolean hasPendingQuestion() {
        return pendingDoor != null && pendingQuestion != null;
    }

    /** Clears the pending door/question context after resolution. */
    private void clearPendingQuestion() {
        pendingDoor = null;
        pendingQuestion = null;
    }


    /**
     * This wires the pause-panel buttons
     */
    private void wirePausePanel() {
        view.getPausePanel().setOnResume(() -> {
            // always hits the current gsm field
            if (gsm.get() == GameState.PAUSED) gsm.resume();
        });

        view.getPausePanel().setOnMainMenu(() -> {
            Sounds.stopLoop();
            Sounds.MENU.play();
            view.showScreen(GameView.Screen.MAIN_MENU);
        });

        view.getPausePanel().setOnQuit(() -> {
            Sounds.stopLoop();
            System.exit(0);
        });
    }


    /**
     * This recieves property-change evernts from model and updates GUI and audio
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();
        switch (name) {
            case "state" -> {
                // If we’ve already shown Victory, do not allow a later GAME_OVER to override it.
                if (hasWon) return;

                final GameState s = (GameState) evt.getNewValue();
                view.getCustomMenuBar().setPaused(s == GameState.PAUSED);

                if (s == GameState.PAUSED) {
                    Sounds.stopLoop();
                    view.showScreen(GameView.Screen.PAUSE);
                } else if (s == GameState.PLAYING) {
                    if (!Sounds.isLooping()) Sounds.GAME.loop();
                    view.showScreen(GameView.Screen.MAZE);

                    // 🔑 update directions because game resumed
                    updateDirectionPanel();
                    view.showScreen(GameView.Screen.MAZE);
                }

                 else if (s == GameState.GAME_OVER) {
                    Sounds.stopLoop();
                    Sounds.LOSE.play();
                    view.showScreen(GameView.Screen.GAME_OVER);
                }
            }

            case "askQuestion" -> {
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
                view.getMazePanel().setDoorAttemptsLabel(null);
                refreshHUDAndGrid();

                // 🔑 update available directions now that the player moved
                updateDirectionPanel();

                if (game.getMaze().isAtExit()) {
                    hasWon = true;
                    Sounds.stopLoop();
                    Sounds.WIN.play();
                    JOptionPane.showMessageDialog(view, "Congrats! You won the game 🎉",
                            "Victory", JOptionPane.INFORMATION_MESSAGE);
                    view.showScreen(GameView.Screen.VICTORY);
                } else {
                    view.showScreen(GameView.Screen.MAZE);
                }
            }


            case "doorBlocked" -> {
                view.getMazePanel().setDoorAttemptsLabel(null);
                refreshHUDAndGrid();

                // 🔑 door state changed, so update directions too
                updateDirectionPanel();

                view.showScreen(GameView.Screen.MAZE);
            }


        }
    }



    /**
     * This opens new game difficulty chooser and starts fresh new game
     */
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

    /**
     * This is a placeholder for loading game (serliazation)
     */
    private void handleLoadGame() {
        JOptionPane.showMessageDialog(view,
                "Load game not implemented yet (use serialization).",
                "Load", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This handles movement attempt in maze
     * @param theDir
     */
    private void handleMove(final Direction theDir) {
        Sounds.MENU.play();       // small click on movement attempt
        game.attemptMove(theDir);
    }

    // GameController.java

    /** Updates the Available panel with current exits from the player's room. */
    private void updateDirectionPanel() {
        final Maze theMaze = game.getMaze();
        final boolean theNorth = theMaze.canMove(Direction.NORTH);
        final boolean theSouth = theMaze.canMove(Direction.SOUTH);
        final boolean theEast  = theMaze.canMove(Direction.EAST);
        final boolean theWest  = theMaze.canMove(Direction.WEST);

        // either call the accessor...
        view.getMazePanel().getDirectionPanel()
                .setAvailable(theNorth, theSouth, theEast, theWest);

        // ...or, if you add the pass-through on MazePanel, call that instead:
        // view.getMazePanel().setAvailableDirections(theNorth, theSouth, theEast, theWest);
    }



    /**
     * This toggles between paused and playing
     */
    private void togglePause() {
        if (gsm.get() == GameState.PLAYING) {
            gsm.pause();
        } else if (gsm.get() == GameState.PAUSED) {
            gsm.resume();
        }
    }

    /**
     * This is a placeholder for saving the game
     */
    private void saveGame() {
        JOptionPane.showMessageDialog(view,
                "Save not implemented yet (wire serialization here).",
                "Save Game", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This exits application and stops any music
     */
    private void quit() {
        Sounds.stopLoop();
        System.exit(0);
    }



    /** This builds a fresh model from chosen difficulty and rebind listeners. */
    public void startNewGame(final DifficultySettings settings) {
        // Detach listeners
        if (game != null) game.removeListener(this);
        if (gsm != null)  gsm.removeListener(this);

        // Build new model
        final questionFactory qf = new questionFactory("jdbc:sqlite:lib/trivia.db");
        final Maze maze = new Maze(settings.getMazeHeight(), settings.getMazeWidth(), qf);
        final Player player = new Player();
        gsm = new GameStateManager();
        game = new Game(maze, player, gsm, settings);

        // Listen again
        game.addListener(this);
        gsm.addListener(this);

        // Reset context/flags
        pendingDoor = null;
        pendingQuestion = null;
        hasWon = false;

        // enabling Save, refresh view, start gameplay music
        view.getCustomMenuBar().setSaveEnabled(true);
        refreshHUDAndGrid();
        Sounds.GAME.loop();
        updateDirectionPanel();

        view.showScreen(GameView.Screen.MAZE);
    }

    /**
     * This pushes HUD values pos/hints and paints grid for current game state.
     */
    private void refreshHUDAndGrid() {
        view.getMazePanel().setHud(
                game.getPlayer().getX(),
                game.getPlayer().getY(),
                game.getHintsLeft()
        );
        view.getMazePanel().render(game.getMaze(), game.getPlayer());
    }
}
