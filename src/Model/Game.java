package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This represents main game logic for Trivia Maze.
 * Manages maze, player state, game state, coordinate changes, questions, and winning and losing condition.
 *
 */
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /*
    Manages event notifications for listeners
     */
    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /*
    Maze layout and doors/questions
     */
    private final Maze myMaze;
    /*
    player character and their stats
     */
    private final Player myPlayer;
    /*
    Manages current game state
     */
    private final GameStateManager myGSM;

    /**
     * Constructs a new Game instance.
     *
     * @param theMaze The maze instance holding rooms and doors.
     * @param thePlayer The player instance holding current stats and posiition
     * @param theGSM The game state manager holding different states
     * @throws NullPointerException if any param is null.
     */
    public Game(final Maze theMaze, final Player thePlayer, final GameStateManager theGSM) {
        this.myMaze = Objects.requireNonNull(theMaze);
        this.myPlayer = Objects.requireNonNull(thePlayer);
        this.myGSM  = Objects.requireNonNull(theGSM);
        myPlayer.setX(myMaze.getCurrentRoom().getRow());
        myPlayer.setY(myMaze.getCurrentRoom().getCol());
    }
    /*
    returns maze instance for game
     */
    public Maze getMaze() { return myMaze; }
    /*
    returns player instance for game
     */
    public Player getPlayer() { return myPlayer; }
    /*
    returns game state manager instance for game
     */
    public GameStateManager getStateManager() { return myGSM; }

    /**
     * Attempts to move player in given direction.
     * if door exisits, an event is triggered to ask question.
     *
     * @param theDir the direction player attempts to move
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
     * player position, and game state changes
     *
     * @param theDoor The door associated with question.
     * @param theCorrect Checks whether player's answer was correct.
     */
    public void handleAnswer(final Door theDoor, final boolean theCorrect) {
        myPlayer.incrementQuestionsAnswered();

        if (!theCorrect) {
            theDoor.block();
            pcs.firePropertyChange("doorBlocked", null, theDoor);

            if (!myMaze.hasPathToExitFromCurrent()) {
                myGSM.gameOver();
            }
            return;
        }

        // figure out which direction this door is relative to current room
        final Direction stepDir = directionOfDoorFromCurrent(theDoor);
        final Room before = myMaze.getCurrentRoom();
        final Room after  = myMaze.step(stepDir);

        // update player position and points
        myPlayer.setX(after.getRow());
        myPlayer.setY(after.getCol());
        myPlayer.addScore(10);


        pcs.firePropertyChange("playerMoved", before, after);

        if (myMaze.isAtExit()) {
            myGSM.gameOver(); // if you add a WIN state later, set that instead
        }
    }

    /**
     * Determines direction of given door relative to current room
     * @param door The door to locate
     * @return Direction of door
     * @throws IllegalArgumentException if door is not connected to current room
     */
    private Direction directionOfDoorFromCurrent(final Door door) {
        final Room cur = myMaze.getCurrentRoom();
        for (final Direction d : Direction.values()) {
            if (cur.getDoor(d) == door) return d;
        }
        throw new IllegalStateException("Door is not connected to the current room.");
    }

    /*
    Adds property change listener for observing game events
     */
    public void addListener(final PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
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