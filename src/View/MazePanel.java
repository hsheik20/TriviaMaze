package View;

import Model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A Swing panel that serves as the primary visual component for the maze game.
 * {@code MazePanel} is responsible for rendering the maze grid, the player's
 * position, heads-up display (HUD) information, and directional controls.
 * It's a "pure view" that provides a public API for a controller to update
 * its state and wire up user input actions.
 *
 * @author Husein & Chan
 */
public class MazePanel extends JPanel {

    // ====== Public callbacks (wired by controller) ======

    /** A consumer that accepts a {@link Direction} for player movement. */
    private Consumer<Direction> myOnMove;

    /** A {@link Runnable} to be executed when the user requests to pause. */
    private Runnable myOnPause;

    /** A {@link Runnable} to be executed when the user requests a new game. */
    private Runnable myOnNewGame;

    /** A {@link Runnable} to be executed when the user requests to save the game. */
    private Runnable myOnSave;

    /** A {@link Runnable} to be executed when the user requests to quit the game. */
    private Runnable myOnQuit;

    /**
     * Wires a consumer to be called when the player attempts to move.
     * @param theOnMove The action to perform on a move command.
     */
    public void onMove(final Consumer<Direction> theOnMove) { this.myOnMove = theOnMove; }

    /**
     * Wires a runnable to be called when the pause key is pressed.
     * @param theOnPause The action to perform on a pause command.
     */
    public void onPause(final Runnable theOnPause) { this.myOnPause = theOnPause; }

    /**
     * Wires a runnable to be called when a new game is requested.
     * @param theOnNewGame The action to perform.
     */
    public void onNewGame(final Runnable theOnNewGame) { this.myOnNewGame = theOnNewGame; }

    /**
     * Wires a runnable to be called when a save is requested.
     * @param theOnSave The action to perform.
     */
    public void onSave(final Runnable theOnSave) { this.myOnSave = theOnSave; }

    /**
     * Wires a runnable to be called when a quit is requested.
     * @param theOnQuit The action to perform.
     */
    public void onQuit(final Runnable theOnQuit) { this.myOnQuit = theOnQuit; }

    /** Initializing constants for styling. */
    private static final Color COL_ROOM = new Color(235, 238, 241);
    private static final Color COL_START = new Color(210, 230, 250);
    private static final Color COL_EXIT = new Color(217, 247, 223);
    private static final Color COL_PLAYER = new Color(255, 239, 170);
    private static final Color COL_GRID_BG = new Color(245, 246, 248);
    private static final Color COL_BLOCKED = new Color(255, 150, 150);

    private static final Font FONT_META = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /** Initializing layout components. */
    private final JPanel myGridHolder = new JPanel();
    private JPanel myGrid;
    private Cell[][] myCells;

    /** Initializes top head display. */
    private final JLabel myPosLabel = new JLabel("Pos: (0,0)");
    private final JLabel myHintsLabel = new JLabel("Hints: ∞");

    /** Initializes right side bar components. */
    private final JLabel myAttemptsDoorLabel = new JLabel("—");
    private final JLabel myNorthLabel = createChip("NORTH"), mySouthLabel = createChip("SOUTH"),
            myEastLabel = createChip("EAST"), myWestLabel = createChip("WEST");

    // Custom panels
    private final PositionPanel myPositionPanel = new PositionPanel();
    private final DirectionPanel myDirectionPanel = new DirectionPanel();
    private final ControlsPanel myControlsPanel = new ControlsPanel();

    // Cached maze dimensions to avoid unnecessary grid rebuilding
    private int myLastRows = -1, myLastCols = -1;

    /**
     * Constructs a {@code MazePanel}.
     * It sets up the overall panel layout, HUD, center area (grid and sidebar),
     * and key bindings for user interaction.
     *
     * @param theView The parent {@link GameView} frame.
     */
    public MazePanel(final GameView theView) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setupHud();
        setupCenterArea();
        setupKeyBindings();
        setFocusable(true);
    }

    /**
     * Initializes the top display bar showing player position and hints.
     */
    private void setupHud() {
        final JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        myPosLabel.setFont(FONT_META);
        myHintsLabel.setFont(FONT_META);
        hud.add(myPosLabel);
        hud.add(myHintsLabel);
        add(hud, BorderLayout.NORTH);
    }

    /**
     * Prepares the center area layout, including the maze grid and a sidebar.
     */
    private void setupCenterArea() {
        final JPanel center = new JPanel(new BorderLayout(8, 0));
        add(center, BorderLayout.CENTER);

        myGridHolder.setLayout(new BorderLayout());
        myGridHolder.setBackground(COL_GRID_BG);
        myGridHolder.setBorder(new EmptyBorder(8, 8, 8, 8));
        center.add(myGridHolder, BorderLayout.CENTER);

        final JPanel sidebar = buildRightSidebar();
        center.add(sidebar, BorderLayout.EAST);
    }

    /**
     * Binds keyboard shortcuts (e.g., arrow keys) to specific actions.
     */
    private void setupKeyBindings() {
        bindKey("UP", () -> fireMove(Direction.NORTH));
        bindKey("DOWN", () -> fireMove(Direction.SOUTH));
        bindKey("LEFT", () -> fireMove(Direction.WEST));
        bindKey("RIGHT", () -> fireMove(Direction.EAST));
        bindKey("P", () -> {
            if (myOnPause != null) myOnPause.run();
        });
        bindKey("N", () -> {
            if (myOnNewGame != null) myOnNewGame.run();
        });
        bindKey("S", () -> {
            if (myOnSave != null) myOnSave.run();
        });
        bindKey("Q", () -> {
            if (myOnQuit != null) myOnQuit.run();
        });
    }

    /**
     * Updates the text labels in the HUD.
     *
     * @param theX         The player's current X-coordinate.
     * @param theY         The player's current Y-coordinate.
     * @param theHintsLeft The number of hints the player has remaining.
     */
    public void setHud(final int theX, final int theY, final int theHintsLeft) {
        myPosLabel.setText("Pos: (" + theX + "," + theY + ")");
        myHintsLabel.setText("Hints: " + (theHintsLeft == Integer.MAX_VALUE ? "∞" : theHintsLeft));
    }

    /**
     * Updates the label showing remaining attempts for a door.
     *
     * @param theAttemptsLeft The number of attempts left, or {@code null} if no door is being interacted with.
     */
    public void setDoorAttemptsLabel(final Integer theAttemptsLeft) {
        if (theAttemptsLeft == null) {
            myAttemptsDoorLabel.setText("—");
        } else {
            myAttemptsDoorLabel.setText(theAttemptsLeft == Integer.MAX_VALUE ? "∞" : theAttemptsLeft.toString());
        }
    }

    /**
     * Redraws the maze grid and all other visual components based on the current
     * game state.
     *
     * @param theMaze   The current {@link Maze} model.
     * @param thePlayer The current {@link Player} model.
     */
    public void render(final Maze theMaze, final Player thePlayer) {
        ensureGrid(theMaze.getRows(), theMaze.getCols());
        updatePositionPanel(theMaze, thePlayer);
        updateCells(theMaze, thePlayer);
        updateDirectionChips(theMaze);
        revalidate();
        repaint();
    }

    /**
     * Returns the panel that visually represents available directions.
     * @return The {@link DirectionPanel} instance.
     */
    public DirectionPanel getDirectionPanel() {
        return myDirectionPanel;
    }

    /**
     * Updates the position panel with the player's current coordinates and
     * the Manhattan distance to the maze exit.
     *
     * @param theMaze   The current maze.
     * @param thePlayer The player navigating the maze.
     */
    private void updatePositionPanel(final Maze theMaze, final Player thePlayer) {
        final int px = thePlayer.getX();
        final int py = thePlayer.getY();
        myPositionPanel.setPosition(px, py);
        if (theMaze.hasPathToExitFromCurrent()) {
            int steps = Math.abs((theMaze.getRows() - 1) - px) + Math.abs((theMaze.getCols() - 1) - py);
            myPositionPanel.setDistanceText(String.valueOf(steps));
        } else {
            myPositionPanel.setDistanceText("No path");
        }
    }

    /**
     * Updates each cell in the maze grid based on its state within the maze model.
     * The cell's color and label are updated to reflect if it's the player's
     * location, start, exit, a blocked room, or a standard room.
     *
     * @param theMaze   The current maze model.
     * @param thePlayer The player navigating the maze.
     */
    private void updateCells(final Maze theMaze, final Player thePlayer) {
        final int rows = theMaze.getRows(), cols = theMaze.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                final boolean isPlayer = (thePlayer.getX() == r && thePlayer.getY() == c);
                final boolean isStart = (r == 0 && c == 0);
                final boolean isExit = (r == rows - 1 && c == cols - 1);
                final Cell cell = myCells[r][c];

                if (isPlayer) cell.setState(Cell.State.PLAYER);
                else if (isExit) cell.setState(Cell.State.EXIT);
                else if (isStart) cell.setState(Cell.State.START);
                else updateCellState(cell, theMaze, r, c);
            }
        }
    }

    /**
     * Updates the state of a single cell based on whether its corresponding
     * room in the maze has a blocked door.
     *
     * @param theCell The cell to update.
     * @param theMaze The maze containing the room.
     * @param theRow  The row index of the room.
     * @param theCol  The column index of the room.
     */
    private void updateCellState(final Cell theCell, final Maze theMaze, final int theRow, final int theCol) {
        final Room room = theMaze.getRoom(theRow, theCol);
        if (roomHasBlockedDoor(room)) {
            theCell.setState(Cell.State.BLOCKED);
        } else {
            theCell.setState(Cell.State.ROOM);
        }
    }

    /**
     * Updates the visual state of the directional chips (labels) in the sidebar
     * to indicate which directions are available from the player's current room.
     *
     * @param theMaze The current maze.
     */
    private void updateDirectionChips(final Maze theMaze) {
        final Room room = theMaze.getCurrentRoom();
        final Set<Direction> available = (room != null)
                ? room.getAvailableDirections()
                : EnumSet.noneOf(Direction.class);

        setChipEnabled(myNorthLabel, available.contains(Direction.NORTH));
        setChipEnabled(mySouthLabel, available.contains(Direction.SOUTH));
        setChipEnabled(myEastLabel, available.contains(Direction.EAST));
        setChipEnabled(myWestLabel, available.contains(Direction.WEST));
    }

    /**
     * Checks if a given room has any blocked doors.
     *
     * @param theRoom The room to inspect.
     * @return {@code true} if at least one door is blocked; {@code false} otherwise.
     */
    private boolean roomHasBlockedDoor(final Room theRoom) {
        for (Direction dir : Direction.values()) {
            Door door = theRoom.getDoor(dir);
            if (door != null && door.isBlocked()) return true;
        }
        return false;
    }

    /**
     * Builds and returns the right sidebar panel containing various sub-panels.
     *
     * @return A {@link JPanel} representing the sidebar.
     */
    private JPanel buildRightSidebar() {
        final JPanel sidebar = new JPanel(new GridLayout(3, 1));
        sidebar.add(myPositionPanel);
        sidebar.add(myControlsPanel);
        sidebar.add(myDirectionPanel);
        return sidebar;
    }

    /**
     * Creates and returns a styled {@link JLabel} used as a directional "chip".
     *
     * @param theText The text for the label (e.g., "NORTH").
     * @return A styled {@link JLabel}.
     */
    private static JLabel createChip(final String theText) {
        final JLabel label = new JLabel(theText, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(230, 230, 230));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(2, 6, 2, 6)));
        label.setFont(FONT_META);
        return label;
    }

    /**
     * Sets the visual style of a directional chip based on its enabled state.
     *
     * @param theChip   The {@link JLabel} chip to update.
     * @param isEnabled {@code true} to highlight the chip, {@code false} to dim it.
     */
    private void setChipEnabled(final JLabel theChip, final boolean isEnabled) {
        theChip.setBackground(isEnabled ? new Color(200, 255, 200) : new Color(235, 235, 235));
        theChip.setForeground(isEnabled ? Color.BLACK : new Color(120, 120, 120));
    }

    /**
     * Ensures the maze grid panel matches the given dimensions. If the dimensions
     * have changed, it rebuilds the entire grid with new cells.
     *
     * @param theRows The desired number of rows for the grid.
     * @param theCols The desired number of columns for the grid.
     */
    private void ensureGrid(final int theRows, final int theCols) {
        if (theRows == myLastRows && theCols == myLastCols && myGrid != null) return;
        myLastRows = theRows;
        myLastCols = theCols;

        myGrid = new JPanel(new GridLayout(theRows, theCols, 8, 8));
        myGrid.setOpaque(false);
        myCells = new Cell[theRows][theCols];

        for (int r = 0; r < theRows; r++) {
            for (int c = 0; c < theCols; c++) {
                final Cell cell = new Cell();
                myCells[r][c] = cell;
                myGrid.add(cell);
            }
        }

        myGridHolder.removeAll();
        myGridHolder.add(myGrid, BorderLayout.CENTER);
        myGridHolder.revalidate();
        myGridHolder.repaint();
    }

    /**
     * A helper method to bind a key stroke to a specific action using Swing's
     * InputMap and ActionMap.
     *
     * @param theKey    The string representation of the key (e.g., "UP").
     * @param theAction The {@link Runnable} to execute when the key is pressed.
     */
    private void bindKey(final String theKey, final Runnable theAction) {
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(theKey), theKey);
        getActionMap().put(theKey, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                theAction.run();
            }
        });
    }

    /**
     * Fires a move event to the wired consumer, if one exists.
     *
     * @param theDirection The {@link Direction} of the move.
     */
    private void fireMove(final Direction theDirection) {
        if (myOnMove != null) myOnMove.accept(theDirection);
    }

    /**
     * A private nested class representing a single visual cell within the maze grid.
     * Each cell has a state (e.g., PLAYER, EXIT, BLOCKED) that determines its
     * appearance.
     */
    private static final class Cell extends JPanel {
        /** The possible states for a maze cell. */
        enum State {ROOM, START, EXIT, PLAYER, BLOCKED}

        private final JLabel myLabel = new JLabel("ROOM", SwingConstants.CENTER);

        /**
         * Constructs a {@code Cell}.
         * It sets up the cell's layout, border, and adds a label.
         */
        Cell() {
            setLayout(new BorderLayout());
            setBackground(COL_ROOM);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 210, 210)),
                    new EmptyBorder(18, 18, 18, 18)));
            myLabel.setFont(FONT_META);
            add(myLabel, BorderLayout.CENTER);
        }

        /**
         * Sets the visual state of the cell, updating its background color and label text.
         *
         * @param theState The new state for the cell.
         */
        void setState(final State theState) {
            switch (theState) {
                case PLAYER -> {
                    setBackground(COL_PLAYER);
                    myLabel.setText("PLAYER");
                }
                case START -> {
                    setBackground(COL_START);
                    myLabel.setText("START");
                }
                case EXIT -> {
                    setBackground(COL_EXIT);
                    myLabel.setText("EXIT");
                }
                case BLOCKED -> {
                    setBackground(COL_BLOCKED);
                    myLabel.setText("BLOCKED");
                }
                default -> {
                    setBackground(COL_ROOM);
                    myLabel.setText("ROOM");
                }
            }
        }
    }
}