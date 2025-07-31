package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame {
    private GameStateManager gameStateManager;
    private MenuBar menuBar;
    private JPanel contentPanel;

    public Game() {
        setTitle("Trivia Maze Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        gameStateManager = new GameStateManager(this);
        initializeComponents();
    }

    // ... rest of Game class implementation

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Game().setVisible(true);
        });
    }
}
