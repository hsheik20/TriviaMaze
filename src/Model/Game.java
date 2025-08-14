package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This represents main game logic for Trivia Maze.
 * Manages maze, player state, game state, coordinate changes, questions, difficulty settings,
 * and winning and losing conditions.
 *
 * @author Husein & Chan
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Manages event notifications for listeners. */
    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /** Maze layout and doors/questions. */
    private final Maze myMaze;
    /** Player character and their stats. */
    private final Player myPlayer;
    /** Manages current game state. */
    private final GameStateManager myGSM;
    /** Current difficulty settings for the game. */
    private DifficultySettings myDifficultySettings;
    /** Number of hints used in the current game. */
    private int myHintsUsed;
    /** Flag indicating if the game has been initialized with difficulty. */
    private boolean myDifficultyInitialized;

    /**
     * Constructs a new Game instance with default normal difficulty.
     *
     * @param theMaze The maze instance holding rooms and doors.
     * @param thePlayer The player instance holding current stats and position.
     * @param theGSM The game state manager holding different states.
     * @throws NullPointerException if any param is null.
     */
    public Game(final Maze theMaze, final Player thePlayer, final GameStateManager theGSM) {
        this(theMaze, thePlayer, theGSM, DifficultyPresets.normal());
    }

    /**
     * Constructs a new Game instance with specified difficulty.
     *
     * @param theMaze The maze instance holding rooms and doors.
     * @param thePlayer The player instance holding current stats and position.
     * @param theGSM The game state manager holding different states.
     * @param theDifficulty The difficulty settings to apply.
     * @throws NullPointerException if any param is null.
     */
    public Game(final Maze theMaze, final Player thePlayer, final GameStateManager theGSM,
                final DifficultySettings theDifficulty) {
        this.myMaze = Objects.requireNonNull(theMaze, "Maze cannot be null");
        this.myPlayer = Objects.requireNonNull(thePlayer, "Player cannot be null");
        this.myGSM = Objects.requireNonNull(theGSM, "GameStateManager cannot be null");
        this.myDifficultySettings = Objects.requireNonNull(theDifficulty, "DifficultySettings cannot be null");

        myPlayer.setX(myMaze.getCurrentRoom().getRow());
        myPlayer.setY(myMaze.getCurrentRoom().getCol());
        myHintsUsed = 0;
        myDifficultyInitialized = false;

        // Apply difficulty settings to game components
        applyDifficultySettings();
    }

    /**
     * Applies the current difficulty settings to game components.
     */
    private void applyDifficultySettings() {
        if (myDifficultySettings == null) {
            return;
        }

        // Apply settings to GameStateManager
        myGSM.setDifficultySettings(myDifficultySettings);

        // Apply maze-related settings (assuming Maze has these methods)
        // myMaze.applyDifficultySettings(myDifficultySettings);

        // Apply player-related settings (assuming Player has these methods)
        // myPlayer.setMaxHints(myDifficultySettings.getMaxHints());

        myDifficultyInitialized = true;

        // Notify listeners about difficulty change
        pcs.firePropertyChange("difficultyChanged", null, myDifficultySettings);
    }

    /**
     * Sets new difficulty settings and applies them to the game.
     *
     * @param theDifficulty The new difficulty settings to apply.
     * @throws NullPointerException if difficulty is null.
     */
    public void setDifficultySettings(final DifficultySettings theDifficulty) {
        Objects.requireNonNull(theDifficulty, "DifficultySettings cannot be null");

        final DifficultySettings oldDifficulty = myDifficultySettings;
        myDifficultySettings = theDifficulty;

        applyDifficultySettings();

        // Fire property change for listeners
        pcs.firePropertyChange("difficultySettings", oldDifficulty, theDifficulty);
    }

    /**
     * Sets difficulty using a preset difficulty level.
     *
     * @param thePresetName The name of the preset difficulty ("Easy", "Normal", "Hard", "Expert").
     * @return True if the preset was found and applied, false otherwise.
     */
    public boolean setDifficultyByPreset(final String thePresetName) {
        final DifficultySettings preset = getPresetByName(thePresetName);
        if (preset != null) {
            setDifficultySettings(preset);
            return true;
        }
        return false;
    }

    /**
     * Gets a preset difficulty by name.
     *
     * @param thePresetName The name of the preset difficulty.
     * @return The DifficultySettings for the preset, or null if not found.
     */
    private DifficultySettings getPresetByName(final String thePresetName) {
        if (thePresetName == null) {
            return null;
        }

        switch (thePresetName.toLowerCase()) {
            case "easy":
                return DifficultyPresets.easy();
            case "normal":
                return DifficultyPresets.normal();
            case "hard":
                return DifficultyPresets.hard();
            case "expert":
                return DifficultyPresets.expert();
            default:
                return null;
        }
    }

    /**
     * Attempts to move player in given direction.
     * If door exists, an event is triggered to ask question.
     *
     * @param theDir The direction player attempts to move.
     */
    public void attemptMove(final Direction theDir) {
        if (myGSM.get() != GameState.PLAYING) return;

        final Door door = myMaze.getDoor(theDir);
        if (door == null || door.isBlocked()) return;

        final Question q = door.getQuestion();
        pcs.firePropertyChange("askQuestion", null, new QuestionRequest(door, q));
    }

    /**
     * This handles player's answer to question. Updates door status,
     * player position, and game state changes based on difficulty settings.
     *
     * @param theDoor The door associated with question.
     * @param theCorrect Checks whether player's answer was correct.
     */
    public void handleAnswer(final Door theDoor, final boolean theCorrect) {
        myPlayer.incrementQuestionsAnswered();

        if (!theCorrect) {
            theDoor.block();

            // Apply wrong answer penalty based on difficulty
            final int penalty = myDifficultySettings.getWrongAnswerPenalty();
            myPlayer.addScore(-penalty);

            pcs.firePropertyChange("doorBlocked", null, theDoor);
            pcs.firePropertyChange("scoreChanged", null, myPlayer.getScore());

            if (!myMaze.hasPathToExitFromCurrent()) {
                myGSM.gameOver();
            }
            return;
        }

        // Figure out which direction this door is relative to current room
        final Direction stepDir = directionOfDoorFromCurrent(theDoor);
        final Room before = myMaze.getCurrentRoom();
        final Room after = myMaze.step(stepDir);

        // Update player position and points based on difficulty
        myPlayer.setX(after.getRow());
        myPlayer.setY(after.getCol());
        final int points = myDifficultySettings.getCorrectAnswerPoints();
        myPlayer.addScore(points);

        pcs.firePropertyChange("playerMoved", before, after);
        pcs.firePropertyChange("scoreChanged", null, myPlayer.getScore());

        if (myMaze.isAtExit()) {
            myGSM.gameOver(); // if you add a WIN state later, set that instead
        }
    }

    /**
     * Uses a hint if available based on difficulty settings.
     *
     * @return True if hint was used successfully, false if no hints available.
     */
    public boolean useHint() {
        if (!areHintsAvailable()) {
            return false;
        }

        myHintsUsed++;
        final int penalty = myDifficultySettings.getHintPenalty();
        myPlayer.addScore(-penalty);

        pcs.firePropertyChange("hintUsed", null, myHintsUsed);
        pcs.firePropertyChange("scoreChanged", null, myPlayer.getScore());

        return true;
    }

    /**
     * Skips the current question if allowed by difficulty settings.
     *
     * @param theDoor The door associated with the question being skipped.
     * @return True if skip was successful, false if skipping not allowed.
     */
    public boolean skipQuestion(final Door theDoor) {
        if (!myDifficultySettings.isAllowSkipping()) {
            return false;
        }

        final int penalty = myDifficultySettings.getSkipQuestionPenalty();
        myPlayer.addScore(-penalty);

        // Block the door since question wasn't answered
        theDoor.block();

        pcs.firePropertyChange("questionSkipped", null, theDoor);
        pcs.firePropertyChange("scoreChanged", null, myPlayer.getScore());

        // Check if game is still winnable
        if (!myMaze.hasPathToExitFromCurrent()) {
            myGSM.gameOver();
        }

        return true;
    }

    /**
     * Checks if hints are still available based on difficulty settings.
     *
     * @return True if hints are available, false otherwise.
     */
    public boolean areHintsAvailable() {
        return myHintsUsed < myDifficultySettings.getMaxHints();
    }

    /**
     * Gets the number of hints remaining.
     *
     * @return The number of hints remaining.
     */
    public int getHintsRemaining() {
        return Math.max(0, myDifficultySettings.getMaxHints() - myHintsUsed);
    }

    /**
     * Determines direction of given door relative to current room.
     *
     * @param door The door to locate.
     * @return Direction of door.
     * @throws IllegalStateException if door is not connected to current room.
     */
    private Direction directionOfDoorFromCurrent(final Door door) {
        final Room cur = myMaze.getCurrentRoom();
        for (final Direction d : Direction.values()) {
            if (cur.getDoor(d) == door) return d;
        }
        throw new IllegalStateException("Door is not connected to the current room.");
    }

    /**
     * Gets information about all available difficulty presets.
     *
     * @return Array of all available difficulty presets.
     */
    public DifficultySettings[] getAvailablePresets() {
        return DifficultyPresets.getAllPresets();
    }

    /**
     * Gets a summary of the current difficulty settings.
     *
     * @return A formatted string describing the current difficulty.
     */
    public String getDifficultySummary() {
        if (myDifficultySettings == null) {
            return "No difficulty set";
        }

        return String.format(
                "Difficulty: %s | Hints: %d/%d | Score Modifiers: +%d/-%d/-%d/-%d",
                myDifficultySettings.getDifficultyName(),
                getHintsRemaining(),
                myDifficultySettings.getMaxHints(),
                myDifficultySettings.getCorrectAnswerPoints(),
                myDifficultySettings.getWrongAnswerPenalty(),
                myDifficultySettings.getHintPenalty(),
                myDifficultySettings.getSkipQuestionPenalty()
        );
    }

    // Getters following naming convention
    /** Returns maze instance for game. */
    public Maze getMaze() { return myMaze; }

    /** Returns player instance for game. */
    public Player getPlayer() { return myPlayer; }

    /** Returns game state manager instance for game. */
    public GameStateManager getStateManager() { return myGSM; }

    /**
     * Gets the current difficulty settings.
     *
     * @return The current DifficultySettings instance.
     */
    public DifficultySettings getDifficultySettings() { return myDifficultySettings; }

    /**
     * Gets the number of hints used in the current game.
     *
     * @return The number of hints used.
     */
    public int getHintsUsed() { return myHintsUsed; }

    /**
     * Checks if skipping questions is allowed based on difficulty.
     *
     * @return True if skipping is allowed, false otherwise.
     */
    public boolean isSkippingAllowed() { return myDifficultySettings.isAllowSkipping(); }

    /**
     * Checks if difficulty has been properly initialized.
     *
     * @return True if difficulty is initialized, false otherwise.
     */
    public boolean isDifficultyInitialized() { return myDifficultyInitialized; }

    /** Adds property change listener for observing game events. */
    public void addListener(final PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /** Removes property change listener. */
    public void removeListener(final PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** Recreate PropertyChangeSupport after deserialization. */
    @Serial
    private Object readResolve() {
        pcs = new PropertyChangeSupport(this);
        return this;
    }
}