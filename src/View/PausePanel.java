package View;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a pause menu with actions to resume the game,
 * return to main menu, or quit the game.
 */
public class PausePanel extends JPanel {

    /** Callback fired when the user chooses to resume game. */
    private Runnable myOnResume;

    /** Callback fired when the user chooses to return to main menu. */
    private Runnable myOnMainMenu;

    /** Callback fired when the user chooses to quit game. */
    private Runnable myOnQuit;

    /**
     * Constructs a PausePanel with three vertically stacked buttons:
     * Resume, Main Menu, and Quit.
     *
     * @param theView the GameView associated with this panel
     */
    public PausePanel(final GameView theView) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        final JButton theResume   = new JButton("Resume");
        final JButton theMainMenu = new JButton("Main Menu");
        final JButton theQuit     = new JButton("Quit");

        theResume.addActionListener(e -> runIfNotNull(myOnResume));
        theMainMenu.addActionListener(e -> runIfNotNull(myOnMainMenu));
        theQuit.addActionListener(e -> runIfNotNull(myOnQuit));

        final GridBagConstraints theGbc = new GridBagConstraints();
        theGbc.insets = new Insets(10, 0, 10, 0);
        theGbc.gridx = 0;

        add(theResume, theGbc);
        add(theMainMenu, theGbc);
        add(theQuit, theGbc);
    }

    /**
     * This sets up action when the user presses "resume".
     *
     * @param theAction the Runnable to execute
     */
    public void setOnResume(final Runnable theAction) {
        myOnResume = theAction;
    }

    /**
     *This sets up action when the user presses "Main Menu".
     *
     * @param theAction the Runnable to execute
     */
    public void setOnMainMenu(final Runnable theAction) {
        myOnMainMenu = theAction;
    }

    /**
     * This sets up action when the user presses "Quit".
     *
     * @param theAction the Runnable to execute
     */
    public void setOnQuit(final Runnable theAction) {
        myOnQuit = theAction;
    }

    /**
     * This runs given action if it is not null.
     *
     * @param theAction the Runnable to execute when non-null
     */
    private void runIfNotNull(final Runnable theAction) {
        if (theAction != null) {
            theAction.run();
        }
    }
}
