package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

/**
 * Manages state of game (plauing, paused, game over),
 * fires property change events when state changes
 */
public class GameStateManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * The current state of game
     */
    private GameState myState = GameState.PLAYING;

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
            pcs.firePropertyChange("state", old, theNewState);
        }
    }

    /**
     * This pauses the game if it is currently playing
     */
    public void pause()    {
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
        pcs.addPropertyChangeListener(l);
    }

    /**
     * This removes listener for game state changes.
     */
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
