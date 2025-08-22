package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The core game model that orchestrates all game logic.
 * This class manages the state of the maze, player, and game. It handles
 * player movement, question flow, hint and skip rules, and win/loss conditions.
 * It also serves as a central hub for firing property change events to notify
 * the view and controller of state changes.
 */
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The event bus used to fire property change events to listeners (e.g., the view).
     * This field is marked as {@code transient} to prevent serialization.
     */
    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // World & actors
    /** The maze containing the rooms and doors. */
    private final Maze myMaze;

    /** The player character. */
    private final Player myPlayer;

    /** The manager for the overall game state (e.g., PLAYING, PAUSED, GAME_OVER). */
    private final GameStateManager myGSM;

    // Difficulty
    /** The difficulty settings for the current game. */
    private final DifficultySettings mySettings;

    // Runtime counters
    /** The number of hints the player has used. */
    private int myHintsUsed = 0;

    /** A map to track the number of remaining attempts for each door. */
    private final Map<Door, Integer> myAttemptsLeft = new HashMap<>();

    /**
     * Constructs a {@code Game} instance by wiring together the core model components.
     * It ensures all necessary components are present and initializes the player's
     * position to the maze's starting room.
     *
     * @param theMaze     The maze for the game.
     * @param thePlayer   The player character.
     * @param theGSM      The game state manager.
     * @param theSettings The difficulty settings.
     * @throws NullPointerException if any of the arguments are {@code null}.
     */
    public Game(final Maze theMaze,
                final Player thePlayer,
                final GameStateManager theGSM,
                final DifficultySettings theSettings) {
        myMaze     = Objects.requireNonNull(theMaze);
        myPlayer   = Objects.requireNonNull(thePlayer);
        myGSM      = Objects.requireNonNull(theGSM);
        mySettings = Objects.requireNonNull(theSettings);

        // sync player to maze's current room
        myPlayer.setX(myMaze.getCurrentRoom().getRow());
        myPlayer.setY(myMaze.getCurrentRoom().getCol());
    }

    // --- Basic getters ---

    /**
     * Returns the maze instance.
     * @return The {@link Maze}.
     */
    public Maze getMaze() { return myMaze; }

    /**
     * Returns the player instance.
     * @return The {@link Player}.
     */
    public Player getPlayer() { return myPlayer; }

    /**
     * Returns the game state manager.
     * @return The {@link GameStateManager}.
     */
    public GameStateManager getStateManager() { return myGSM; }

    /**
     * Returns the difficulty settings.
     * @return The {@link DifficultySettings}.
     */
    public DifficultySettings getSettings() { return mySettings; }

    /**
     * Calculates the number of hints left based on difficulty settings and hints already used.
     *
     * @return The number of hints remaining. Returns {@link Integer#MAX_VALUE} if hints are unlimited.
     */
    public int getHintsLeft() {
        int max = mySettings.getMaxHints();
        if (max == 0) return Integer.MAX_VALUE; // unlimited
        int left = max - myHintsUsed;
        return Math.max(0, left);
    }

    /**
     * Returns the number of attempts remaining for a specific door.
     *
     * @param door The door to check.
     * @return The number of attempts left. Returns {@link Integer#MAX_VALUE} if attempts are unlimited.
     */
    public int getAttemptsLeft(final Door door) {
        return attemptsLeft(door);
    }

    // --- Movement entry point from controller ---

    /**
     * Attempts to move the player in the given direction.
     * If the move is valid and the door is not blocked, it fires a property change
     * event to request a question from the view/controller.
     *
     * @param theDir The desired direction of movement.
     */
    public void attemptMove(final Direction theDir) {
        if (myGSM.get() != GameState.PLAYING) return;

        final Door door = myMaze.getDoor(theDir);
        if (door == null || door.isBlocked()) return;

        final Question q = door.getQuestion();
        pcs.firePropertyChange("askQuestion", null, new QuestionRequest(door, q));
    }

    // --- Resolving an answered question ---

    /**
     * Handles the outcome of a question answer.
     * If the answer is correct, the player moves through the door.
     * If incorrect, an attempt is consumed, and the door may become blocked.
     *
     * @param theDoor    The door associated with the question.
     * @param theCorrect {@code true} if the answer was correct, {@code false} otherwise.
     */
    public void handleAnswer(final Door theDoor, final boolean theCorrect) {

        myPlayer.incrementQuestionsAnswered();

        if (!theCorrect) {
            // Use up an attempt; block the door if out of tries
            consumeAttempt(theDoor);
            final boolean outOfTries = attemptsLeft(theDoor) <= 0;
            if (outOfTries) {
                theDoor.block();
                pcs.firePropertyChange("doorBlocked", null, theDoor);
            }

            // If the maze is now impossible, end the game
            if (!myMaze.hasPathToExitFromCurrent()) {
                myGSM.gameOver();
            }

            return;
        }

        // Move through the door on correct answer
        final Direction stepDir = directionOfDoorFromCurrent(theDoor);
        final Room before = myMaze.getCurrentRoom();
        final Room after = myMaze.step(stepDir);

        myPlayer.setX(after.getRow());
        myPlayer.setY(after.getCol());

        pcs.firePropertyChange("playerMoved", before, after);

        if (myMaze.isAtExit()) {
            myGSM.gameOver(); // or a WIN state if you add one later
        }
    }

    // --- Hints/skip rules ---

    /**
     * Checks if the player can use a hint for the given question.
     *
     * @param q The question to check.
     * @return {@code true} if a hint is available and allowed by difficulty settings, {@code false} otherwise.
     */
    public boolean canUseHint(final Question q) {
        if (q == null) return false;
        if (q.getHint() == null) return false;
        if (q.getHint().isUsed()) return false;

        final int maxHints = mySettings.getMaxHints();
        if (maxHints == 0) return true; // unlimited
        return myHintsUsed < maxHints;
    }

    /**
     * Marks a hint as used and returns the hint text.
     *
     * @param q The question for which the hint is being used.
     * @return The hint text, or {@code null} if a hint is not allowed.
     */
    public String useHint(final Question q) {
        if (!canUseHint(q)) return null;
        myHintsUsed += 1;
        return q.getHint().useHint();
    }

    /**
     * Checks if skipping a question is allowed by the current difficulty settings.
     *
     * @return {@code true} if skipping is allowed, {@code false} otherwise.
     */
    public boolean canSkip() {
        return mySettings.isAllowSkipping();
    }

    /**
     * Skips a question by permanently blocking the associated door.
     * Checks if this action leads to a dead end and ends the game if it does.
     *
     * @param door The door associated with the question being skipped.
     */
    public void skipQuestion(final Door door) {
        if (!mySettings.isAllowSkipping()) return;

        door.block();
        pcs.firePropertyChange("doorBlocked", null, door);

        if (!myMaze.hasPathToExitFromCurrent()) {
            myGSM.gameOver();
        }
    }

    // --- Internals ---

    /**
     * Retrieves the number of attempts left for a given door, initializing the counter
     * if it's the first time checking.
     *
     * @param door The door to check.
     * @return The number of attempts left.
     */
    private int attemptsLeft(final Door door) {
        Integer left = myAttemptsLeft.get(door);
        if (left == null) {
            final int max = mySettings.getMaxAttemptsPerDoor();
            if (max == 0) {
                myAttemptsLeft.put(door, Integer.MAX_VALUE); // unlimited
                return Integer.MAX_VALUE;
            } else {
                myAttemptsLeft.put(door, max);
                return max;
            }
        }
        return left;
    }

    /**
     * Consumes one attempt for the given door.
     * This method does nothing if attempts are unlimited.
     *
     * @param door The door for which to consume an attempt.
     */
    private void consumeAttempt(final Door door) {
        final int max = mySettings.getMaxAttemptsPerDoor();
        if (max == 0) return; // unlimited
        final int left = attemptsLeft(door);
        final int updated = (left > 0) ? left - 1 : 0;
        myAttemptsLeft.put(door, updated);
    }

    /**
     * Finds the direction from the current room to the specified door.
     *
     * @param door The door to find the direction to.
     * @return The direction (e.g., {@link Direction#NORTH}).
     * @throws IllegalStateException if the door is not connected to the current room.
     */
    private Direction directionOfDoorFromCurrent(final Door door) {
        final Room cur = myMaze.getCurrentRoom();
        for (final Direction d : Direction.values()) {
            if (cur.getDoor(d) == door) return d;
        }
        throw new IllegalStateException("Door is not connected to the current room.");
    }

    // --- Property change plumbing ---

    /**
     * Adds a {@link PropertyChangeListener} to the game model.
     *
     * @param l The listener to add.
     */
    public void addListener(final PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Removes a {@link PropertyChangeListener} from the game model.
     *
     * @param l The listener to remove.
     */
    public void removeListener(final PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * A method for deserialization that re-initializes the transient {@link PropertyChangeSupport} object.
     *
     * @return The deserialized {@code Game} instance.
     */
    @Serial
    private Object readResolve() {
        pcs = new PropertyChangeSupport(this);
        return this;
    }
}