package View;

import javax.swing.*;
import java.awt.*;

/**
 * A Swing panel that displays the player's current coordinates and the
 * estimated distance to the maze's exit. It is a read-only, informational
 * component of the game's graphical user interface.
 *
 * @author Husein & Chan
 */
public class PositionPanel extends JPanel {

    /**
     * A label that displays the player's current (x, y) coordinates.
     */
    private final JLabel myCurrentLabel  = new JLabel("Current: (0,0)", SwingConstants.CENTER);

    /**
     * A label that displays the distance or path information to the exit.
     */
    private final JLabel myDistanceLabel = new JLabel("To exit: —", SwingConstants.CENTER);

    /**
     * Constructs a {@code PositionPanel}.
     * It sets up the panel's layout, adds a titled border, and initializes
     * the labels for current position and distance to the exit.
     */
    public PositionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Position"));
        setBackground(Color.LIGHT_GRAY);

        final JPanel theCenter = new JPanel(new GridLayout(3, 1));
        theCenter.setBackground(Color.LIGHT_GRAY);
        theCenter.add(myCurrentLabel);
        theCenter.add(myDistanceLabel);

        add(theCenter, BorderLayout.CENTER);
    }

    /**
     * Updates the text of the current position label with the specified coordinates.
     *
     * @param theX The current x-coordinate (row).
     * @param theY The current y-coordinate (column).
     */
    public void setPosition(final int theX, final int theY) {
        myCurrentLabel.setText("Current: (" + theX + "," + theY + ")");
    }

    /**
     * Updates the text of the distance-to-exit label.
     *
     * @param theText The string to be displayed, which can be a number
     * (e.g., "5") or a message (e.g., "No path").
     */
    public void setDistanceText(final String theText) {
        myDistanceLabel.setText("To exit: " + theText);
    }
}