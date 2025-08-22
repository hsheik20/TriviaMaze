package View;

import javax.swing.*;
import java.awt.*;

/**
 * The main view class for the Trivia Maze game, extending {@link JFrame}.
 * This class acts as the central container for all other GUI panels, managing
 * the different screens of the game (e.g., main menu, maze, question screen)
 * using a {@link CardLayout}. It provides a single point of control for
 * switching between these different views.
 *
 * @author Husein
 */
public class GameView extends JFrame {

    /**
     * An enumeration representing the different screens or states of the game UI.
     * Each screen corresponds to a different {@link JPanel} managed by the {@link CardLayout}.
     */
    public enum Screen { MAIN_MENU, MAZE, QUESTION, PAUSE, GAME_OVER, VICTORY }

    /**
     * The {@link CardLayout} instance that manages the swapping of different panels.
     */
    private final CardLayout cards = new CardLayout();

    /**
     * The root {@link JPanel} that holds all the different screen panels
     * and is managed by the {@link CardLayout}.
     */
    private final JPanel root = new JPanel(cards);

    // Panels for each screen managed by the CardLayout
    private final MainMenuPanel mainMenu = new MainMenuPanel(this);
    private final MazePanel mazePanel = new MazePanel(this);
    private final QuestionPanel questionPanel = new QuestionPanel(this);
    private final PausePanel pausePanel = new PausePanel(this);
    private final MenuBar menuBar = new MenuBar();
    private final JPanel gameOverPanel = new JLabelPanel("GAME OVER");
    private final JPanel victoryPanel = new JLabelPanel("YOU WIN!");

    /**
     * Constructs a {@code GameView} object.
     * It sets up the main application window's properties, initializes
     * all the screen panels, adds them to the root panel under the CardLayout,
     * and sets up the menu bar.
     */
    public GameView() {
        super("Trivia Maze");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setJMenuBar(menuBar); // Set it on the JFrame

        root.add(mainMenu, Screen.MAIN_MENU.name());
        root.add(mazePanel, Screen.MAZE.name());
        root.add(questionPanel, Screen.QUESTION.name());
        root.add(pausePanel, Screen.PAUSE.name());
        root.add(gameOverPanel, Screen.GAME_OVER.name());
        root.add(victoryPanel, Screen.VICTORY.name());
        add(root, BorderLayout.CENTER);
    }

    /**
     * Displays the specified screen by switching the view of the {@link CardLayout}.
     * It also requests focus for the MazePanel when the maze screen is shown
     * to ensure keyboard input is captured correctly.
     *
     * @param s The {@link Screen} to be displayed.
     */
    public void showScreen(Screen s) {
        cards.show(root, s.name());
        if (s == Screen.MAZE) {
            mazePanel.requestFocusInWindow();
        }
    }

    /**
     * Returns the {@link MainMenuPanel} instance.
     * @return The main menu panel.
     */
    public MainMenuPanel getMainMenu() { return mainMenu; }

    /**
     * Returns the {@link MazePanel} instance.
     * @return The maze panel.
     */
    public MazePanel getMazePanel() { return mazePanel; }

    /**
     * Returns the {@link QuestionPanel} instance.
     * @return The question panel.
     */
    public QuestionPanel getQuestionPanel() { return questionPanel; }

    /**
     * Returns the {@link PausePanel} instance.
     * @return The pause panel.
     */
    public PausePanel getPausePanel() { return pausePanel; }

    /**
     * Returns the custom {@link MenuBar} instance used in the frame.
     * @return The custom menu bar.
     */
    public View.MenuBar getCustomMenuBar() {
        return menuBar;
    }

    /**
     * A simple inner class that creates a JPanel with a large, centered JLabel.
     * This is used for simple screens like "GAME OVER" or "YOU WIN!".
     */
    static class JLabelPanel extends JPanel {
        JLabelPanel(String text) {
            setLayout(new GridBagLayout());
            JLabel l = new JLabel(text);
            l.setFont(l.getFont().deriveFont(Font.BOLD, 32f));
            add(l);
        }
    }
}