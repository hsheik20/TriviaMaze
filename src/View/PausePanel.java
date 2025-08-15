// View/PausePanel.java
package View;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {
    private Runnable onResume, onMainMenu, onQuit;

    public PausePanel(GameView view) {
        setLayout(new GridBagLayout());
        var box = new Box(BoxLayout.Y_AXIS);
        box.add(btn("Resume",    () -> { if (onResume != null) onResume.run(); }));
        box.add(Box.createVerticalStrut(10));
        box.add(btn("Main Menu", () -> { if (onMainMenu != null) onMainMenu.run(); }));
        box.add(Box.createVerticalStrut(10));
        box.add(btn("Quit",      () -> { if (onQuit != null) onQuit.run(); }));
        add(box);
    }

    private JComponent btn(String text, Runnable r){
        var b = new JButton(text);
        b.setAlignmentX(CENTER_ALIGNMENT);
        b.addActionListener(e -> r.run());
        b.setMaximumSize(new Dimension(220, 40));
        return b;
    }

    public void onResume(Runnable r){ this.onResume = r; }
    public void onMainMenu(Runnable r){ this.onMainMenu = r; }
    public void onQuit(Runnable r){ this.onQuit = r; }
}
