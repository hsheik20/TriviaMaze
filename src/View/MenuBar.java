package View;

import Model.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Menu bar for the Trivia Maze Game.
 * Provides access to game functions, settings, and help.
 *
 * @author Generated for Trivia Maze
 */
public class MenuBar extends JMenuBar {

    // File Menu Items
    private JMenuItem myNewGameItem;
    private JMenuItem mySaveGameItem;
    private JMenuItem myLoadGameItem;
    private JMenuItem myExitItem;

    // Game Menu Items
    private JMenuItem myPauseResumeItem;
    private JMenuItem myRestartGameItem;
    private JMenuItem myMainMenuItem;

    // Difficulty Menu Items
    private JMenuItem myEasyItem;
    private JMenuItem myNormalItem;
    private JMenuItem myHardItem;
    private JMenuItem myExpertItem;
    private JMenuItem myCustomDifficultyItem;

    // View Menu Items
    private JCheckBoxMenuItem myShowHintsItem;
    private JCheckBoxMenuItem myShowTimerItem;
    private JCheckBoxMenuItem myShowScoreItem;
    private JMenuItem myToggleFullScreenItem;

    // Tools Menu Items
    private JMenuItem myUseHintItem;
    private JMenuItem mySkipQuestionItem;
    private JMenuItem myHighScoresItem;
    private JMenuItem myGameStatsItem;

    // Help Menu Items
    private JMenuItem myGameRulesItem;
    private JMenuItem myControlsItem;
    private JMenuItem myAboutItem;

    private Game myGame;

    /**
     * Constructs the game menu bar.
     *
     * @param game The game instance
     */
    public MenuBar(Game game) {
        myGame = game;
        createMenus();
    }

    /**
     * Creates all menus and menu items.
     */
    private void createMenus() {
        add(createFileMenu());
        add(createGameMenu());
        add(createDifficultyMenu());
        add(createViewMenu());
        add(createToolsMenu());
        add(createHelpMenu());
    }

    /**
     * Creates the File menu.
     *
     * @return The File menu
     */
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        myNewGameItem = new JMenuItem("New Game");
        myNewGameItem.setMnemonic(KeyEvent.VK_N);
        myNewGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(myNewGameItem);

        fileMenu.addSeparator();

        mySaveGameItem = new JMenuItem("Save Game");
        mySaveGameItem.setMnemonic(KeyEvent.VK_S);
        mySaveGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(mySaveGameItem);

        myLoadGameItem = new JMenuItem("Load Game");
        myLoadGameItem.setMnemonic(KeyEvent.VK_L);
        myLoadGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(myLoadGameItem);

        fileMenu.addSeparator();

        myExitItem = new JMenuItem("Exit");
        myExitItem.setMnemonic(KeyEvent.VK_X);
        myExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(myExitItem);

        return fileMenu;
    }

    /**
     * Creates the Game menu.
     *
     * @return The Game menu
     */
    private JMenu createGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);

        myPauseResumeItem = new JMenuItem("Pause");
        myPauseResumeItem.setMnemonic(KeyEvent.VK_P);
        myPauseResumeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        gameMenu.add(myPauseResumeItem);

        gameMenu.addSeparator();

        myRestartGameItem = new JMenuItem("Restart Game");
        myRestartGameItem.setMnemonic(KeyEvent.VK_R);
        myRestartGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        gameMenu.add(myRestartGameItem);

        myMainMenuItem = new JMenuItem("Main Menu");
        myMainMenuItem.setMnemonic(KeyEvent.VK_M);
        myMainMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        gameMenu.add(myMainMenuItem);

        return gameMenu;
    }

    /**
     * Creates the Difficulty menu.
     *
     * @return The Difficulty menu
     */
    private JMenu createDifficultyMenu() {
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setMnemonic(KeyEvent.VK_D);

        ButtonGroup difficultyGroup = new ButtonGroup();

        myEasyItem = new JRadioButtonMenuItem("Easy");
        myEasyItem.setMnemonic(KeyEvent.VK_E);
        difficultyGroup.add(myEasyItem);
        difficultyMenu.add(myEasyItem);

        myNormalItem = new JRadioButtonMenuItem("Normal");
        myNormalItem.setMnemonic(KeyEvent.VK_N);
        myNormalItem.setSelected(true); // Default selection
        difficultyGroup.add(myNormalItem);
        difficultyMenu.add(myNormalItem);

        myHardItem = new JRadioButtonMenuItem("Hard");
        myHardItem.setMnemonic(KeyEvent.VK_H);
        difficultyGroup.add(myHardItem);
        difficultyMenu.add(myHardItem);

        myExpertItem = new JRadioButtonMenuItem("Expert");
        myExpertItem.setMnemonic(KeyEvent.VK_X);
        difficultyGroup.add(myExpertItem);
        difficultyMenu.add(myExpertItem);

        difficultyMenu.addSeparator();

        myCustomDifficultyItem = new JMenuItem("Custom Difficulty...");
        myCustomDifficultyItem.setMnemonic(KeyEvent.VK_C);
        difficultyMenu.add(myCustomDifficultyItem);

        return difficultyMenu;
    }

    /**
     * Creates the View menu.
     *
     * @return The View menu
     */
    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        myShowHintsItem = new JCheckBoxMenuItem("Show Hints Panel");
        myShowHintsItem.setMnemonic(KeyEvent.VK_H);
        myShowHintsItem.setSelected(true);
        viewMenu.add(myShowHintsItem);

        myShowTimerItem = new JCheckBoxMenuItem("Show Timer");
        myShowTimerItem.setMnemonic(KeyEvent.VK_T);
        myShowTimerItem.setSelected(true);
        viewMenu.add(myShowTimerItem);

        myShowScoreItem = new JCheckBoxMenuItem("Show Score");
        myShowScoreItem.setMnemonic(KeyEvent.VK_S);
        myShowScoreItem.setSelected(true);
        viewMenu.add(myShowScoreItem);

        viewMenu.addSeparator();

        myToggleFullScreenItem = new JMenuItem("Toggle Full Screen");
        myToggleFullScreenItem.setMnemonic(KeyEvent.VK_F);
        myToggleFullScreenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        viewMenu.add(myToggleFullScreenItem);

        return viewMenu;
    }

    /**
     * Creates the Tools menu.
     *
     * @return The Tools menu
     */
    private JMenu createToolsMenu() {
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        myUseHintItem = new JMenuItem("Use Hint");
        myUseHintItem.setMnemonic(KeyEvent.VK_H);
        myUseHintItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
        toolsMenu.add(myUseHintItem);

        mySkipQuestionItem = new JMenuItem("Skip Question");
        mySkipQuestionItem.setMnemonic(KeyEvent.VK_S);
        mySkipQuestionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        toolsMenu.add(mySkipQuestionItem);

        toolsMenu.addSeparator();

        myHighScoresItem = new JMenuItem("High Scores");
        myHighScoresItem.setMnemonic(KeyEvent.VK_I);
        toolsMenu.add(myHighScoresItem);

        myGameStatsItem = new JMenuItem("Game Statistics");
        myGameStatsItem.setMnemonic(KeyEvent.VK_G);
        toolsMenu.add(myGameStatsItem);

        return toolsMenu;
    }

    /**
     * Creates the Help menu.
     *
     * @return The Help menu
     */
    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        myGameRulesItem = new JMenuItem("Game Rules");
        myGameRulesItem.setMnemonic(KeyEvent.VK_R);
        myGameRulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(myGameRulesItem);

        myControlsItem = new JMenuItem("Controls");
        myControlsItem.setMnemonic(KeyEvent.VK_C);
        helpMenu.add(myControlsItem);

        helpMenu.addSeparator();

        myAboutItem = new JMenuItem("About");
        myAboutItem.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(myAboutItem);

        return helpMenu;
    }

    // Action Listener Setters for File Menu
    public void setNewGameAction(ActionListener listener) {
        myNewGameItem.addActionListener(listener);
    }

    public void setSaveGameAction(ActionListener listener) {
        mySaveGameItem.addActionListener(listener);
    }

    public void setLoadGameAction(ActionListener listener) {
        myLoadGameItem.addActionListener(listener);
    }

    public void setExitAction(ActionListener listener) {
        myExitItem.addActionListener(listener);
    }

    // Action Listener Setters for Game Menu
    public void setPauseResumeAction(ActionListener listener) {
        myPauseResumeItem.addActionListener(listener);
    }

    public void setRestartGameAction(ActionListener listener) {
        myRestartGameItem.addActionListener(listener);
    }

    public void setMainMenuAction(ActionListener listener) {
        myMainMenuItem.addActionListener(listener);
    }

    // Action Listener Setters for Difficulty Menu
    public void setEasyDifficultyAction(ActionListener listener) {
        myEasyItem.addActionListener(listener);
    }

    public void setNormalDifficultyAction(ActionListener listener) {
        myNormalItem.addActionListener(listener);
    }

    public void setHardDifficultyAction(ActionListener listener) {
        myHardItem.addActionListener(listener);
    }

    public void setExpertDifficultyAction(ActionListener listener) {
        myExpertItem.addActionListener(listener);
    }

    public void setCustomDifficultyAction(ActionListener listener) {
        myCustomDifficultyItem.addActionListener(listener);
    }

    // Action Listener Setters for View Menu
    public void setShowHintsAction(ActionListener listener) {
        myShowHintsItem.addActionListener(listener);
    }

    public void setShowTimerAction(ActionListener listener) {
        myShowTimerItem.addActionListener(listener);
    }

    public void setShowScoreAction(ActionListener listener) {
        myShowScoreItem.addActionListener(listener);
    }

    public void setToggleFullScreenAction(ActionListener listener) {
        myToggleFullScreenItem.addActionListener(listener);
    }

    // Action Listener Setters for Tools Menu
    public void setUseHintAction(ActionListener listener) {
        myUseHintItem.addActionListener(listener);
    }

    public void setSkipQuestionAction(ActionListener listener) {
        mySkipQuestionItem.addActionListener(listener);
    }

    public void setHighScoresAction(ActionListener listener) {
        myHighScoresItem.addActionListener(listener);
    }

    public void setGameStatsAction(ActionListener listener) {
        myGameStatsItem.addActionListener(listener);
    }

    // Action Listener Setters for Help Menu
    public void setGameRulesAction(ActionListener listener) {
        myGameRulesItem.addActionListener(listener);
    }

    public void setControlsAction(ActionListener listener) {
        myControlsItem.addActionListener(listener);
    }

    public void setAboutAction(ActionListener listener) {
        myAboutItem.addActionListener(listener);
    }

    // State Management Methods
    /**
     * Updates the pause/resume menu item text based on game state.
     *
     * @param isPaused Whether the game is currently paused
     */
    public void updatePauseResumeText(boolean isPaused) {
        myPauseResumeItem.setText(isPaused ? "Resume" : "Pause");
    }

    /**
     * Enables or disables game-related menu items.
     *
     * @param gameInProgress Whether a game is currently in progress
     */
    public void setGameMenusEnabled(boolean gameInProgress) {
        myPauseResumeItem.setEnabled(gameInProgress);
        myRestartGameItem.setEnabled(gameInProgress);
        mySaveGameItem.setEnabled(gameInProgress);
        myUseHintItem.setEnabled(gameInProgress);
        mySkipQuestionItem.setEnabled(gameInProgress);
    }

    /**
     * Updates the difficulty menu selection based on current difficulty.
     *
     * @param difficulty The current difficulty settings
     */
    public void updateDifficultySelection(DifficultySettings difficulty) {
        if (difficulty == null) return;

        String difficultyName = difficulty.getDifficultyName().toLowerCase();
        switch (difficultyName) {
            case "easy":
                myEasyItem.setSelected(true);
                break;
            case "normal":
                myNormalItem.setSelected(true);
                break;
            case "hard":
                myHardItem.setSelected(true);
                break;
            case "expert":
                myExpertItem.setSelected(true);
                break;
            default:
                // Custom difficulty - deselect all preset options
                myEasyItem.setSelected(false);
                myNormalItem.setSelected(false);
                myHardItem.setSelected(false);
                myExpertItem.setSelected(false);
                break;
        }
    }

    /**
     * Enables or disables hint-related menu items based on hint availability.
     *
     * @param hintsAvailable Whether hints are available
     */
    public void setHintMenuEnabled(boolean hintsAvailable) {
        myUseHintItem.setEnabled(hintsAvailable);
    }

    /**
     * Enables or disables the skip question menu item.
     *
     * @param canSkip Whether question skipping is allowed
     */
    public void setSkipQuestionEnabled(boolean canSkip) {
        mySkipQuestionItem.setEnabled(canSkip);
    }

    /**
     * Gets the current state of the show hints checkbox.
     *
     * @return True if show hints is selected
     */
    public boolean isShowHintsSelected() {
        return myShowHintsItem.isSelected();
    }

    /**
     * Gets the current state of the show timer checkbox.
     *
     * @return True if show timer is selected
     */
    public boolean isShowTimerSelected() {
        return myShowTimerItem.isSelected();
    }

    /**
     * Gets the current state of the show score checkbox.
     *
     * @return True if show score is selected
     */
    public boolean isShowScoreSelected() {
        return myShowScoreItem.isSelected();
    }
}