package View;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A Swing panel that visually indicates which cardinal directions are available
 * for movement within the maze. The panel's appearance is updated by the controller
 * and is not interactive.
 *
 * @author Husein & Chan
 */
public class DirectionPanel extends JPanel {

    /** The color used for available (movable) directions. */
    private static final Color AVAILABLE_COLOR   = new Color(180, 255, 180); // light green
    /** The color used for unavailable (blocked) directions. */
    private static final Color UNAVAILABLE_COLOR = new Color(220, 220, 220); // light gray

    /**
     * A map to store the JLabel components for each direction, allowing for
     * easy access and color updates.
     */
    private final Map<String, JLabel> myLabels = new HashMap<>();

    /**
     * Constructs a {@code DirectionPanel}.
     * It sets up the panel's layout, a titled border, and initializes
     * four non-interactive JLabels, one for each cardinal direction,
     * with an initial unavailable color.
     */
    public DirectionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Available"));
        setBackground(Color.LIGHT_GRAY);

        final JPanel theGrid = new JPanel(new GridLayout(4, 1, 5, 5));
        theGrid.setOpaque(false);

        for (final String theDir : new String[]{"NORTH", "SOUTH", "EAST", "WEST"}) {
            final JLabel theLabel = new JLabel(theDir, SwingConstants.CENTER);
            theLabel.setOpaque(true); // Key: labels honor background
            theLabel.setBackground(UNAVAILABLE_COLOR);
            theLabel.setFont(theLabel.getFont().deriveFont(Font.BOLD, 13f));
            myLabels.put(theDir, theLabel);
            theGrid.add(theLabel);
        }

        add(theGrid, BorderLayout.CENTER);
    }

    /**
     * Updates the visual availability of each direction. The background color
     * of each direction's label is changed to reflect whether it's available or not.
     *
     * @param theNorth   {@code true} if the North direction is available, {@code false} otherwise.
     * @param theSouth   {@code true} if the South direction is available, {@code false} otherwise.
     * @param theEast    {@code true} if the East direction is available, {@code false} otherwise.
     * @param theWest    {@code true} if the West direction is available, {@code false} otherwise.
     */
    public void setAvailable(final boolean theNorth,
                             final boolean theSouth,
                             final boolean theEast,
                             final boolean theWest) {
        set("NORTH", theNorth);
        set("SOUTH", theSouth);
        set("EAST", theEast);
        set("WEST", theWest);
        revalidate();
        repaint();
    }

    /**
     * A helper method to set the background color of a specific direction's label.
     *
     * @param theDir       The name of the direction (e.g., "NORTH").
     * @param theAvailable {@code true} for an available state (green), {@code false} for an unavailable state (gray).
     */
    private void set(final String theDir, final boolean theAvailable) {
        final JLabel theLabel = myLabels.get(theDir);
        if (theLabel != null) {
            theLabel.setBackground(theAvailable ? AVAILABLE_COLOR : UNAVAILABLE_COLOR);
        }
    }
}