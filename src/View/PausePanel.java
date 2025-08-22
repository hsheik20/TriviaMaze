package View;

import javax.swing.*;
import java.awt.*;

/**
 * A Swing panel that serves as the pause menu for the game.
 * This panel is displayed when the game is paused, providing the player with
 * options to resume the game, return to the main menu, or quit the application.
 * It uses a listener-based design, where a controller can "wire" actions
 * to its buttons.
 *
 * @author Husein & Chan
 */
public class PausePanel extends JPanel {

    /**
     * The callback to be executed when the "Resume" button is clicked.
     */
    private Runnable myOnResume;

    /**
     * The callback to be executed when the "Main Menu" button is clicked.
     */
    private Runnable myOnMainMenu;

    /**
     * The callback to be executed when the "Quit" button is clicked.
     */
    private Runnable myOnQuit;

    /**
     * Constructs a {@code PausePanel}.
     * It sets up the panel's layout and background color, and then creates
     * and wires three vertically stacked buttons: "Resume," "Main Menu," and "Quit."
     *
     * @param theView The parent {@link GameView} instance. This reference is
     * not used in the panel's current logic but is included for consistency
     * with other view components.
     */
    public PausePanel(final GameView theView) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        final JButton theResume = new JButton("Resume");
        final JButton theMainMenu = new JButton("Main Menu");
        final JButton theQuit = new JButton("Quit");

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
     * Sets the action to be performed when the "Resume" button is clicked.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnResume(final Runnable theAction) {
        myOnResume = theAction;
    }

    /**
     * Sets the action to be performed when the "Main Menu" button is clicked.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnMainMenu(final Runnable theAction) {
        myOnMainMenu = theAction;
    }

    /**
     * Sets the action to be performed when the "Quit" button is clicked.
     *
     * @param theAction The {@link Runnable} to execute.
     */
    public void setOnQuit(final Runnable theAction) {
        myOnQuit = theAction;
    }

    /**
     * A helper method to safely execute a {@link Runnable} only if it is not null.
     *
     * @param theAction The {@link Runnable} to execute when non-null.
     */
    private void runIfNotNull(final Runnable theAction) {
        if (theAction != null) {
            theAction.run();
        }
    }
}