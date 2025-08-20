package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Core game logic (model). Orchestrates movement, question flow,
 * hints/skip rules, attempts per door, and win/lose checks.
 */
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Event bus for the view/controller - marked transient for proper serialization
    private transient PropertyChangeSupport pcs;

    // World & actors
    private final Maze myMaze;
    private final Player myPlayer;
    private final GameStateManager myGSM;

    // Difficulty
    private final DifficultySettings mySettings;

    // Runtime counters
    private int myHintsUsed = 0;
    private final Map<Door, Integer> myAttemptsLeft = new HashMap<>();

    public Game(final Maze theMaze,
                final Player thePlayer,
                final GameStateManager theGSM,
                final DifficultySettings theSettings) {
        myMaze     = Objects.requireNonNull(theMaze);
        myPlayer   = Objects.requireNonNull(thePlayer);
        myGSM      = Objects.requireNonNull(theGSM);
        mySettings = Objects.requireNonNull(theSettings);

        // Initialize PropertyChangeSupport
        initializePropertyChangeSupport();

        // sync player to maze's current room
        myPlayer.setX(myMaze.getCurrentRoom().getRow());
        myPlayer.setY(myMaze.getCurrentRoom().getCol());
    }

    /**
     * Initialize the PropertyChangeSupport - called during construction and after deserialization
     */
    private void initializePropertyChangeSupport() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
    }

    // --- Basic getters ---
    public Maze getMaze() { return myMaze; }
    public Player getPlayer() { return myPlayer; }
    public GameStateManager getStateManager() { return myGSM; }
    public DifficultySettings getSettings() { return mySettings; }

    /** Hints left based on difficulty. Integer.MAX_VALUE means unlimited. */
    public int getHintsLeft() {
        int max = mySettings.getMaxHints();
        if (max == 0) return Integer.MAX_VALUE;
        int left = max - myHintsUsed;
        return Math.max(0, left);
    }

    /** Attempts left for a specific door (Integer.MAX_VALUE means unlimited). */
    public int getAttemptsLeft(final Door door) {
        return attemptsLeft(door);
    }

    // --- Movement entry point from controller ---
    public void attemptMove(final Direction theDir) {
        if (myGSM.get() != GameState.PLAYING) return;

        final Door door = myMaze.getDoor(theDir);
        if (door == null || door.isBlocked()) return;

        final Question q = door.getQuestion();
        pcs.firePropertyChange("askQuestion", null, new QuestionRequest(door, q));
    }

    // --- Resolving an answered question ---
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
        final Room after  = myMaze.step(stepDir);

        myPlayer.setX(after.getRow());
        myPlayer.setY(after.getCol());

        pcs.firePropertyChange("playerMoved", before, after);

        if (myMaze.isAtExit()) {
            myGSM.gameOver(); // or a WIN state if you add one later
        }
    }

    // --- Hints/skip rules ---
    public boolean canUseHint(final Question q) {
        if (q == null) return false;
        if (q.getHint() == null) return false;
        if (q.getHint().isUsed()) return false;

        final int maxHints = mySettings.getMaxHints();
        if (maxHints == 0) return true; // unlimited
        return myHintsUsed < maxHints;
    }

    /** Marks hint used. Returns hint text, or null if not allowed. */
    public String useHint(final Question q) {
        if (!canUseHint(q)) return null;
        myHintsUsed += 1;
        return q.getHint().useHint();
    }

    public boolean canSkip() {
        return mySettings.isAllowSkipping();
    }

    /** Applies skip: block the door and check for dead-end loss. */
    public void skipQuestion(final Door door) {
        if (!mySettings.isAllowSkipping()) return;

        door.block();
        pcs.firePropertyChange("doorBlocked", null, door);

        if (!myMaze.hasPathToExitFromCurrent()) {
            myGSM.gameOver();
        }
    }

    // --- Internals ---
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

    private void consumeAttempt(final Door door) {
        final int max = mySettings.getMaxAttemptsPerDoor();
        if (max == 0) return; // unlimited
        final int left = attemptsLeft(door);
        final int updated = (left > 0) ? left - 1 : 0;
        myAttemptsLeft.put(door, updated);
    }

    private Direction directionOfDoorFromCurrent(final Door door) {
        final Room cur = myMaze.getCurrentRoom();
        for (final Direction d : Direction.values()) {
            if (cur.getDoor(d) == door) return d;
        }
        throw new IllegalStateException("Door is not connected to the current room.");
    }

    // --- Property change plumbing ---
    public void addListener(final PropertyChangeListener l) {
        if (pcs == null) {
            initializePropertyChangeSupport();
        }
        pcs.addPropertyChangeListener(l);
    }

    public void removeListener(final PropertyChangeListener l) {
        if (pcs != null) {
            pcs.removePropertyChangeListener(l);
        }
    }

    // --- Custom serialization methods ---
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Write all non-transient fields
        out.defaultWriteObject();
        // Note: PropertyChangeSupport is transient and will be recreated on deserialization
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read all non-transient fields
        in.defaultReadObject();
        // Recreate the transient PropertyChangeSupport
        initializePropertyChangeSupport();
    }

    // Alternative approach using readResolve (can be used instead of readObject)
    @Serial
    private Object readResolve() {
        initializePropertyChangeSupport();
        return this;
    }
}