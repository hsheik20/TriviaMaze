package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel displayed when the game is paused.
 * Provides options to resume, restart, change settings, or return to main menu.
 *
 * @author Generated for Trivia Maze
 */
public class PausePanel extends JPanel {

    private static final Font TITLE_FONT = new Font(Font.SERIF, Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 150); // Semi-transparent overlay
    private static final Color PANEL_COLOR = new Color(245, 245, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);

    private JButton myResumeButton;
    private JButton myRestartButton;
    private JButton mySettingsButton;
    private JButton mySaveGameButton;
    private JButton myMainMenuButton;
    private JLabel myGameInfoLabel;
    private Game myGame;

    /**
     * Constructs the pause panel.
     *
     * @param game The current game instance
     */
    public PausePanel(Game game) {
        myGame = game;
        initializePanel();
        createComponents();
        layoutComponents();
    }

    /**
     * Initializes the panel's basic properties.
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        setOpaque(false); // Allow transparency for overlay effect
        setPreferredSize(new Dimension(400, 300));
    }

    /**
     * Creates all UI components.
     */
    private void createComponents() {
        myResumeButton = createStyledButton("Resume Game");
        myRestartButton = createStyledButton("Restart Game");
        mySettingsButton = createStyledButton("Settings");
        mySaveGameButton = createStyledButton("Save Game");
        myMainMenuButton = createStyledButton("Main Menu");

        myGameInfoLabel = new JLabel();
        myGameInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        myGameInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        updateGameInfo();
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
        button.setPreferredSize(new Dimension(180, 40));
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
        // Create main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel("GAME PAUSED", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(PANEL_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(myResumeButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(myRestartButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(mySettingsButton, gbc);

        gbc.gridy = 3;
        buttonPanel.add(mySaveGameButton, gbc);

        gbc.gridy = 4;
        buttonPanel.add(myMainMenuButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Game info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(PANEL_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        infoPanel.add(myGameInfoLabel, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Center the main panel
        setLayout(new GridBagLayout());
        GridBagConstraints centerGbc = new GridBagConstraints();
        add(mainPanel, centerGbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw semi-transparent overlay
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    /**
     * Updates the game information display.
     */
    public void updateGameInfo() {
        if (myGame == null) {
            myGameInfoLabel.setText("No game information available");
            return;
        }

        StringBuilder info = new StringBuilder("<html><center>");

        // Player info
        if (myGame.getPlayer() != null) {
            info.append(String.format("Score: %d<br/>", myGame.getPlayer().getScore()));
            info.append(String.format("Position: (%d, %d)<br/>",
                    myGame.getPlayer().getX(), myGame.getPlayer().getY()));
        }

        // Difficulty info
        if (myGame.getDifficultySettings() != null) {
            info.append(String.format("Difficulty: %s<br/>",
                    myGame.getDifficultySettings().getDifficultyName()));
        }

        // Hints info
        info.append(String.format("Hints Remaining: %d", myGame.getHintsRemaining()));

        info.append("</center></html>");
        myGameInfoLabel.setText(info.toString());
    }

    /**
     * Shows the pause panel with updated information.
     */
    public void showPausePanel() {
        updateGameInfo();
        setVisible(true);
    }

    /**
     * Hides the pause panel.
     */
    public void hidePausePanel() {
        setVisible(false);
    }

    /**
     * Sets the action listener for the resume button.
     *
     * @param listener The action listener
     */
    public void setResumeAction(ActionListener listener) {
        myResumeButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the restart button.
     *
     * @param listener The action listener
     */
    public void setRestartAction(ActionListener listener) {
        myRestartButton.addActionListener(listener);
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
     * Sets the action listener for the save game button.
     *
     * @param listener The action listener
     */
    public void setSaveGameAction(ActionListener listener) {
        mySaveGameButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the main menu button.
     *
     * @param listener The action listener
     */
    public void setMainMenuAction(ActionListener listener) {
        myMainMenuButton.addActionListener(listener);
    }

    /**
     * Enables or disables the save game button.
     *
     * @param enabled Whether the button should be enabled
     */
    public void setSaveGameEnabled(boolean enabled) {
        mySaveGameButton.setEnabled(enabled);
    }

    /**
     * Sets focus to the resume button (default action).
     */
    public void setDefaultFocus() {
        SwingUtilities.invokeLater(() -> myResumeButton.requestFocusInWindow());
    }
}