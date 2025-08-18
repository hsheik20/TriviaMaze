package View;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {
    public ControlsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Controls"));
        setBackground(Color.LIGHT_GRAY);

        JTextArea textArea = new JTextArea(
                "↑ / W : North\n" +
                        "↓ / S : South\n" +
                        "← / A : West\n" +
                        "→ / D : East\n" +
                        "P : Pause"
        );
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(textArea, BorderLayout.CENTER);
    }
}
