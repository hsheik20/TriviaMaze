package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Manages state of game (playing, paused, game over),
 * fires property change events when state changes
 */
public class GameStateManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Mark as transient for proper serialization
    private transient PropertyChangeSupport pcs;

    /**
     * The current state of game
     */
    private GameState myState = GameState.PLAYING;

    /**
     * Default constructor - initializes PropertyChangeSupport
     */
    public GameStateManager() {
        initializePropertyChangeSupport();
    }

    /**
     * Initialize the PropertyChangeSupport - called during construction and after deserialization
     */
    private void initializePropertyChangeSupport() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
    }

    /**
     * returns current state of game
     */
    public GameState get() {
        return myState;
    }

    /**
     * This sets game state, and fires property change event if change happens.
     *
     * @param theNewState the new game state.
     */
    public void set(final GameState theNewState) {
        if (theNewState != myState) {
            final GameState old = myState;
            myState = theNewState;
            if (pcs != null) {
                pcs.firePropertyChange("state", old, theNewState);
            }
        }
    }

    /**
     * This pauses the game if it is currently playing
     */
    public void pause() {
        if (myState == GameState.PLAYING)
            set(GameState.PAUSED);
    }

    /**
     * This resumes the game if currently paused
     */
    public void resume() {
        if (myState == GameState.PAUSED)
            set(GameState.PLAYING);
    }

    /**
     * This sets game to be over.
     */
    public void gameOver() {
        if (myState != GameState.GAME_OVER)
            set(GameState.GAME_OVER);
    }

    /**
     * This adds listener for game state changes
     */
    public void addListener(final PropertyChangeListener l) {
        if (pcs == null) {
            initializePropertyChangeSupport();
        }
        pcs.addPropertyChangeListener(l);
    }

    /**
     * This removes listener for game state changes.
     */
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

    /**
     * Recreate PropertyChangeSupport after deserialization.
     * Alternative to readObject method - ensures proper initialization after deserialization
     */
    @Serial
    private Object readResolve() {
        initializePropertyChangeSupport();
        return this;
    }
}