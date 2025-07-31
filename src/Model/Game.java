package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The {@code Game} class represents the main window for the Trivia Maze Game.
 * It sets up the user interface, initializes game states, and launches the application.
 * This class extends {@link JFrame} and serves as the entry point of the game.
 *
 * Responsibilities include:
 * <ul>
 *   <li>Initializing the main window and components</li>
 *   <li>Managing game state through {@link GameStateManager}</li>
 *   <li>Setting look and feel for consistent UI design</li>
 * </ul>
 *
 * @author Husein & Chan
 */
public class Game {
    /** Manages the various game states and transitions between them. */
    private GameStateManager gameStateManager;
    /** Represents the top menu bar of the application. */
    private MenuBar menuBar;
    /** The main content panel where UI components are placed. */
    private JPanel contentPanel;
    /**
     * Constructs a new {@code Game} instance.
     * Sets up the window properties and initializes game components.
     */
    public Game() {
        setTitle("Trivia Maze Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center the window on screen

        gameStateManager = new GameStateManager(this);
        initializeComponents();
    }

    // Additional methods for setting up the UI and handling state changes
    // would be implemented here.

    /**
     * Main method that launches the Trivia Maze Game application.
     * Sets the system look and feel, then creates and displays the main game window.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set native look and feel for the operating system
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Game().setVisible(true);
        });
    }
}
