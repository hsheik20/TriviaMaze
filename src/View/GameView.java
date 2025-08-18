// View/GameView.java
package View;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    public enum Screen { MAIN_MENU, MAZE, QUESTION, PAUSE, GAME_OVER, VICTORY }

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final MainMenuPanel mainMenu = new MainMenuPanel(this);
    private final MazePanel mazePanel   = new MazePanel(this);
    private final QuestionPanel questionPanel = new QuestionPanel(this);
    private final PausePanel pausePanel = new PausePanel(this);
    private final MenuBar menuBar = new MenuBar();
    private final JPanel gameOverPanel  = new JLabelPanel("GAME OVER");
    private final JPanel victoryPanel   = new JLabelPanel("YOU WIN!");

    public GameView() {
        super("Trivia Maze");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setJMenuBar(menuBar); // Set it on the JFrame


        root.add(mainMenu,     Screen.MAIN_MENU.name());
        root.add(mazePanel,    Screen.MAZE.name());
        root.add(questionPanel,Screen.QUESTION.name());
        root.add(pausePanel,   Screen.PAUSE.name());
        root.add(gameOverPanel,Screen.GAME_OVER.name());
        root.add(victoryPanel, Screen.VICTORY.name());
        add(root, BorderLayout.CENTER);
    }

    // GameView.java
    public void showScreen(Screen s) {
        cards.show(root, s.name());
        if (s == Screen.MAZE) {
            mazePanel.requestFocusInWindow();
        }
    }


    public MainMenuPanel getMainMenu()      { return mainMenu; }
    public MazePanel getMazePanel()         { return mazePanel; }
    public QuestionPanel getQuestionPanel() { return questionPanel; }
    public PausePanel getPausePanel()       { return pausePanel; }
    public View.MenuBar getCustomMenuBar() {
        return menuBar;
    }

    // simple label panel used for GameOver/Victory placeholders
    static class JLabelPanel extends JPanel {
        JLabelPanel(String text) {
            setLayout(new GridBagLayout());
            JLabel l = new JLabel(text);
            l.setFont(l.getFont().deriveFont(Font.BOLD, 32f));
            add(l);
        }
    }
}
