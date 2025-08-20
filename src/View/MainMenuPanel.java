// View/MainMenuPanel.java
package View;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    private Runnable onNewGame, onLoadGame, onInstructions, onAbout, onQuit;

    public MainMenuPanel(GameView view) {
        setLayout(new GridBagLayout());
        setBackground(new Color(92, 64, 51)); // brown background

        Box menuBox = createMainMenuBox();
        add(menuBox);
    }

    // =====================
    // == Helper Methods  ==
    // =====================

    private Box createMainMenuBox() {
        Box box = new Box(BoxLayout.Y_AXIS);

        JLabel title = createTitleLabel();
        box.add(title);
        box.add(Box.createVerticalStrut(30));

        JButton newGameBtn  = createAndWireButton("NEW GAME", () -> runIfNotNull(onNewGame));
        JButton loadBtn     = createAndWireButton("LOAD GAME", () -> runIfNotNull(onLoadGame));
        JButton instructBtn = createAndWireButton("INSTRUCTIONS", () -> runIfNotNull(onInstructions));
        JButton aboutBtn    = createAndWireButton("ABOUT", () -> runIfNotNull(onAbout));
        JButton quitBtn     = createAndWireButton("QUIT", () -> runIfNotNull(onQuit));

        for (JButton b : new JButton[]{newGameBtn, loadBtn, instructBtn, aboutBtn, quitBtn}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            box.add(b);
            box.add(Box.createVerticalStrut(15));
        }

        return box;
    }

    private JLabel createTitleLabel() {
        JLabel title = new JLabel("TRIVIA MAZE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        return title;
    }

    private JButton createAndWireButton(String text, Runnable action) {
        JButton button = createMenuButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 60));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 4),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        addHoverEffect(button, new Color(255, 140, 0), new Color(255, 170, 60));
        return button;
    }

    private void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normal);
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void runIfNotNull(Runnable r) {
        if (r != null) r.run();
    }

    // ======================
    // == Wiring Setters   ==
    // ======================

    public void onNewGame(Runnable r)       { this.onNewGame = r; }
    public void onLoadGame(Runnable r)      { this.onLoadGame = r; }
    public void onInstructions(Runnable r)  { this.onInstructions = r; }
    public void onAbout(Runnable r)         { this.onAbout = r; }
    public void onQuit(Runnable r)          { this.onQuit = r; }
}
