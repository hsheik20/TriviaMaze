package View;

import javax.swing.*;
import java.awt.*;

/**
 * A Swing panel that displays a list of keyboard controls for the game.
 * This panel is read-only and is designed to provide quick reference
 * for the player on how to navigate and interact with the game.
 *
 * @author Husein
 */
public class ControlsPanel extends JPanel {

    /**
     * A read-only text area used to display the keyboard controls.
     */
    private final JTextArea myTextArea;

    /**
     * Constructs a {@code ControlsPanel}.
     * It sets up the panel's layout, adds a titled border, and initializes
     * a non-editable text area with the list of controls.
     */
    public ControlsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Controls"));
        setBackground(Color.LIGHT_GRAY);

        myTextArea = new JTextArea(
                "↑ : North\n" +
                        "↓ : South\n" +
                        "← : West\n" +
                        "→ : East\n" +
                        "P : Pause"
        );

        myTextArea.setEditable(false);
        myTextArea.setOpaque(false);
        myTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(myTextArea, BorderLayout.CENTER);
    }
}