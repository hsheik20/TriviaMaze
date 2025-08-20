// View/GameMenuBar.java
package View;

import javax.swing.*;

/**
 * This represents a MenuBar, it holds the main menu bar at the top of the game with actions to pause/resume game, play new game,
 * and quit game. Holds setter methods for wiring controller actions.
 */
public class MenuBar extends JMenuBar {

    /** Initializing callbacks wired to controller. */
    private Runnable myOnNewGame;
    private Runnable myOnPauseToggle;
    private Runnable myOnSaveGame;
    private Runnable myOnQuitGame;

    // ----- New optional callbacks to support spec-compliant menus -----
    /** Callback for "Load Game". */
    private Runnable myOnLoadGame;
    /** Callback for "Instructions" dialog. */
    private Runnable myOnShowInstructions;
    /** Callback for "About" dialog. */
    private Runnable myOnShowAbout;

    /** Initializing main menu references. */
    private final JMenuItem myNEWITEM   = new JMenuItem("New Game");
    private final JMenuItem myPAUSEITEM = new JMenuItem("Pause Game");
    private final JMenuItem mySAVEITEM  = new JMenuItem("Save Game");
    private final JMenuItem myQUITITEM  = new JMenuItem("Quit Game");

    // ----- New menu item references for File/Help -----
    private final JMenuItem myLOADITEM  = new JMenuItem("Load Game");
    private final JMenuItem myEXITITEM  = new JMenuItem("Exit");
    private final JMenuItem myINSTRUCTIONSITEM = new JMenuItem("Instructions");
    private final JMenuItem myABOUTITEM = new JMenuItem("About");

    /**
     * Constructs the MenuBar with File, Game, and Help menus.
     */
    public MenuBar() {
        // ===== File =====
        final JMenu theFile = new JMenu("File");

        mySAVEITEM.addActionListener(e -> runIfNotNull(myOnSaveGame));
        myLOADITEM.addActionListener(e -> runIfNotNull(myOnLoadGame));
        myEXITITEM.addActionListener(e -> runIfNotNull(myOnQuitGame));

        theFile.add(mySAVEITEM);
        theFile.add(myLOADITEM);
        theFile.addSeparator();
        theFile.add(myEXITITEM);

        // ===== Game =====
        final JMenu theGame = new JMenu("Game");

        // click handlers
        myNEWITEM.addActionListener(e   -> runIfNotNull(myOnNewGame));
        myPAUSEITEM.addActionListener(e -> runIfNotNull(myOnPauseToggle));
        myQUITITEM.addActionListener(e  -> runIfNotNull(myOnQuitGame));

        theGame.add(myNEWITEM);
        theGame.add(myPAUSEITEM);
        theGame.addSeparator();
        theGame.add(myQUITITEM);

        // ===== Help =====
        final JMenu theHelp = new JMenu("Help");

        myINSTRUCTIONSITEM.addActionListener(e -> runIfNotNull(myOnShowInstructions));
        myABOUTITEM.addActionListener(e       -> runIfNotNull(myOnShowAbout));

        theHelp.add(myINSTRUCTIONSITEM);
        theHelp.add(myABOUTITEM);

        // add menus to bar
        add(theFile);
        add(theGame);
        add(theHelp);
    }

    /**
     * This sets the "New Game" action.
     * @param theAction the Runnable to execute when the user selects New Game
     */
    public void setOnNewGame(final Runnable theAction) { myOnNewGame = theAction; }

    /**
     * This sets the "Pause/Resume" toggle action.
     * @param theAction the Runnable to execute when the user selects Pause/Resume
     */
    public void setOnPauseToggle(final Runnable theAction) { myOnPauseToggle = theAction; }

    /**
     * This sets the "Save Game" action.
     * @param theAction the Runnable to execute when the user selects Save Game
     */
    public void setOnSaveGame(final Runnable theAction) { myOnSaveGame = theAction; }

    /**
     * This sets the "Quit Game" action.
     * @param theAction the Runnable to execute when the user selects Quit Game
     */
    public void setOnQuitGame(final Runnable theAction) { myOnQuitGame = theAction; }

    // ----- New setters for added items -----

    /**
     * This sets the "Load Game" action.
     * @param theAction the Runnable to execute when the user selects Load Game
     */
    public void setOnLoadGame(final Runnable theAction) { myOnLoadGame = theAction; }

    /**
     * This sets the "Instructions" action under Help.
     * @param theAction the Runnable to execute when the user selects Instructions
     */
    public void setOnShowInstructions(final Runnable theAction) { myOnShowInstructions = theAction; }

    /**
     * This sets the "About" action under Help.
     * @param theAction the Runnable to execute when the user selects About
     */
    public void setOnShowAbout(final Runnable theAction) { myOnShowAbout = theAction; }

    /**
     * This updates the Pause/Resume menu item label based on current state.
     * @param thePaused whether the game is currently paused
     */
    public void setPaused(final boolean thePaused) {
        myPAUSEITEM.setText(thePaused ? "Resume Game" : "Pause Game");
    }

    /**
     * This enables or disables the Save menu item
     * @param theEnabled true to enable, false to disable
     */
    public void setSaveEnabled(final boolean theEnabled) {
        mySAVEITEM.setEnabled(theEnabled);
    }

    /**
     * This sets up an action if it exists.
     * @param theAction the Runnable to run if non-null
     */
    private void runIfNotNull(final Runnable theAction) {
        if (theAction != null) theAction.run();
    }
}
