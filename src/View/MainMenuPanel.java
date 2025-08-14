package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main menu panel for the Trivia Maze Game.
 * Provides options to start new game, load game, view settings, and exit.
 *
 * @author Generated for Trivia Maze
 */
public class MainMenuPanel extends JPanel {

    private static final Font TITLE_FONT = new Font(Font.SERIF, Font.BOLD, 36);
    private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color BUTTON_COLOR = new Color(65, 125, 177);

    private JButton myNewGameButton;
    private JButton myLoadGameButton;
    private JButton myHighScoresButton;
    private JButton mySettingsButton;
    private JButton myExitButton;

    /**
     * Constructs the main menu panel with all UI components.
     */
    public MainMenuPanel() {
        initializePanel();
        createComponents();
        layoutComponents();
    }

    /**
     * Initializes the panel's basic properties.
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Creates all the UI components.
     */
    private void createComponents() {
        myNewGameButton = createStyledButton("New Game");
        myLoadGameButton = createStyledButton("Load Game");
        myHighScoresButton = createStyledButton("High Scores");
        mySettingsButton = createStyledButton("Settings");
        myExitButton = createStyledButton("Exit");
    }

    /**
     * Creates a styled button with consistent appearance.
     *
     * @param text The button text
     * @return A styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    /**
     * Lays out all components in the panel.
     */
    private void layoutComponents() {
        // Title
        JLabel titleLabel = new JLabel("TRIVIA MAZE", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);

        // Menu buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(myNewGameButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(myLoadGameButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(myHighScoresButton, gbc);

        gbc.gridy = 3;
        buttonPanel.add(mySettingsButton, gbc);

        gbc.gridy = 4;
        buttonPanel.add(myExitButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("Navigate the maze and answer trivia questions!", JLabel.CENTER);
        footerLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        footerLabel.setForeground(Color.GRAY);
        add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * Sets the action listener for the new game button.
     *
     * @param listener The action listener
     */
    public void setNewGameAction(ActionListener listener) {
        myNewGameButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the load game button.
     *
     * @param listener The action listener
     */
    public void setLoadGameAction(ActionListener listener) {
        myLoadGameButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the high scores button.
     *
     * @param listener The action listener
     */
    public void setHighScoresAction(ActionListener listener) {
        myHighScoresButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the settings button.
     *
     * @param listener The action listener
     */
    public void setSettingsAction(ActionListener listener) {
        mySettingsButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the exit button.
     *
     * @param listener The action listener
     */
    public void setExitAction(ActionListener listener) {
        myExitButton.addActionListener(listener);
    }

    /**
     * Enables or disables the load game button.
     *
     * @param enabled Whether the button should be enabled
     */
    public void setLoadGameEnabled(boolean enabled) {
        myLoadGameButton.setEnabled(enabled);
    }
}