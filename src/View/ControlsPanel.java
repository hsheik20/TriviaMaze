// View/ControlsPanel.java
package View;

import javax.swing.*;
import java.awt.*;

/**
 * This represents a ControlsPanel which displays a list of keyboard controls.
 */
public class ControlsPanel extends JPanel {

    /** Read-only text area describing controls. */
    private final JTextArea myTextArea;

    /** Constructs a ControlsPanel with a titled border and static control text. */
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
