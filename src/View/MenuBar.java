package View;

import javax.swing.*;

/**
 * A custom {@link JMenuBar} for the game's main window.
 * This menu bar provides standard game actions like starting a new game,
 * saving, loading, pausing, and quitting, as well as accessing help information.
 * It follows a listener-based design, providing public setter methods for a
 * controller to "wire" its logic to the menu item clicks.
 *
 * @author Husein & Chan
 */
public class MenuBar extends JMenuBar {

    /**
     * Callback for the "New Game" menu item. This is a {@link Runnable}
     * provided by the controller.
     */
    private Runnable myOnNewGame;

    /**
     * Callback for the "Pause Game" menu item.
     */
    private Runnable myOnPauseToggle;

    /**
     * Callback for the "Save Game" menu item.
     */
    private Runnable myOnSaveGame;

    /**
     * Callback for the "Quit Game" menu item.
     */
    private Runnable myOnQuitGame;

    /**
     * Callback for the "Load Game" menu item.
     */
    private Runnable myOnLoadGame;

    /**
     * Callback for the "Instructions" menu item.
     */
    private Runnable myOnShowInstructions;

    /**
     * Callback for the "About" menu item.
     */
    private Runnable myOnShowAbout;

    // ----- Menu item references for all menu options -----
    private final JMenuItem myNEWITEM = new JMenuItem("New Game");
    private final JMenuItem myPAUSEITEM = new JMenuItem("Pause Game");
    private final JMenuItem mySAVEITEM  = new JMenuItem("Save Game");
    private final JMenuItem myQUITITEM  = new JMenuItem("Quit Game");

    // ----- New menu item references for File/Help -----
    private final JMenuItem myLOADITEM  = new JMenuItem("Load Game");
    private final JMenuItem myEXITITEM  = new JMenuItem("Exit");
    private final JMenuItem myINSTRUCTIONSITEM = new JMenuItem("Instructions");
    private final JMenuItem myABOUTITEM = new JMenuItem("About");

    /**
     * Constructs the {@code MenuBar} and initializes all menus and menu items.
     * It sets up the "File," "Game," and "Help" menus and wires their action
     * listeners to the corresponding internal callbacks.
     */
    public MenuBar() {
        // ===== File Menu =====
        final JMenu theFile = new JMenu("File");
        mySAVEITEM.addActionListener(e -> runIfNotNull(myOnSaveGame));
        myLOADITEM.addActionListener(e -> runIfNotNull(myOnLoadGame));
        myEXITITEM.addActionListener(e -> runIfNotNull(myOnQuitGame));
        theFile.add(mySAVEITEM);
        theFile.add(myLOADITEM);
        theFile.addSeparator();
        theFile.add(myEXITITEM);

        // ===== Game Menu =====
        final JMenu theGame = new JMenu("Game");
        myNEWITEM.addActionListener(e -> runIfNotNull(myOnNewGame));
        myPAUSEITEM.addActionListener(e -> runIfNotNull(myOnPauseToggle));
        myQUITITEM.addActionListener(e -> runIfNotNull(myOnQuitGame));
        theGame.add(myNEWITEM);
        theGame.add(myPAUSEITEM);
        theGame.addSeparator();
        theGame.add(myQUITITEM);

        // ===== Help Menu =====
        final JMenu theHelp = new JMenu("Help");
        myINSTRUCTIONSITEM.addActionListener(e -> runIfNotNull(myOnShowInstructions));
        myABOUTITEM.addActionListener(e -> runIfNotNull(myOnShowAbout));
        theHelp.add(myINSTRUCTIONSITEM);
        theHelp.add(myABOUTITEM);

        // Add menus to the menu bar
        add(theFile);
        add(theGame);
        add(theHelp);
    }

    // ======================
    // == Wiring Setters   ==
    // ======================

    /**
     * Sets the action to be performed when the "New Game" menu item is selected.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnNewGame(final Runnable theAction) {
        this.myOnNewGame = theAction;
    }

    /**
     * Sets the action for the "Pause Game" menu item, which typically toggles
     * the game's paused state.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnPauseToggle(final Runnable theAction) {
        this.myOnPauseToggle = theAction;
    }

    /**
     * Sets the action for the "Save Game" menu item.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnSaveGame(final Runnable theAction) {
        this.myOnSaveGame = theAction;
    }

    /**
     * Sets the action for the "Quit Game" and "Exit" menu items.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnQuitGame(final Runnable theAction) {
        this.myOnQuitGame = theAction;
    }

    /**
     * Sets the action for the "Load Game" menu item.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnLoadGame(final Runnable theAction) {
        this.myOnLoadGame = theAction;
    }

    /**
     * Sets the action for the "Instructions" menu item.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnShowInstructions(final Runnable theAction) {
        this.myOnShowInstructions = theAction;
    }

    /**
     * Sets the action for the "About" menu item.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnShowAbout(final Runnable theAction) {
        this.myOnShowAbout = theAction;
    }

    // ======================
    // == State Setters    ==
    // ======================

    /**
     * Updates the text of the "Pause/Resume" menu item based on the
     * current paused state of the game.
     *
     * @param thePaused {@code true} if the game is paused, {@code false} otherwise.
     */
    public void setPaused(final boolean thePaused) {
        myPAUSEITEM.setText(thePaused ? "Resume Game" : "Pause Game");
    }

    /**
     * Enables or disables the "Save Game" menu item. This is typically used
     * to prevent saving when it's not a valid operation (e.g., at the main menu).
     *
     * @param theEnabled {@code true} to enable, {@code false} to disable.
     */
    public void setSaveEnabled(final boolean theEnabled) {
        mySAVEITEM.setEnabled(theEnabled);
    }

    /**
     * A helper method to safely execute a {@link Runnable} only if it is not null.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    private void runIfNotNull(final Runnable theAction) {
        if (theAction != null) {
            theAction.run();
        }
    }
}