package View;

import Model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * MazePanel handles the visual representation of the maze game using Swing.
 * It updates room states, manages layout components, and handles user input (buttons/keys).
 */
public class MazePanel extends JPanel {

    // ====== Public callbacks (wired by controller) ======
    private Consumer<Direction> myOnMove;
    private Runnable myOnPause;
    private Runnable myOnNewGame, myOnSave, myOnQuit;

    public void onMove(final Consumer<Direction> theOnMove){ this.myOnMove = theOnMove; }
    public void onPause(final Runnable theOnPause){ this.myOnPause = theOnPause; }
    public void onNewGame(final Runnable theOnNewGame){ this.myOnNewGame = theOnNewGame; }
    public void onSave(final Runnable theOnSave){ this.myOnSave = theOnSave; }
    public void onQuit(final Runnable theOnQuit){ this.myOnQuit = theOnQuit; }

    /** Initializing constants for styling. */
    private static final Color COL_ROOM    = new Color(235,238,241);
    private static final Color COL_START   = new Color(210,230,250);
    private static final Color COL_EXIT    = new Color(217,247,223);
    private static final Color COL_PLAYER  = new Color(255,239,170);
    private static final Color COL_GRID_BG = new Color(245,246,248);
    private static final Color COL_BLOCKED = new Color(255, 150, 150);

    private static final Font FONT_META = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /** Initializing layout componenets . */
    private final JPanel myGridHolder = new JPanel();
    private JPanel myGrid;
    private Cell[][] myCells;

    /** Initializes top head display. */
    private final JLabel myPosLabel   = new JLabel("Pos: (0,0)");
    private final JLabel myHintsLabel = new JLabel("Hints: ∞");

    /** Initializes right side bar components. */
    private final JLabel myAttemptsDoorLabel = new JLabel("—");
    private final JLabel myNorthLabel = createChip("NORTH"), mySouthLabel = createChip("SOUTH"),
            myEastLabel  = createChip("EAST"),  myWestLabel  = createChip("WEST");

    // Custom panels
    private final PositionPanel myPositionPanel   = new PositionPanel();
    private final DirectionPanel myDirectionPanel = new DirectionPanel();
    private final ControlsPanel myControlsPanel   = new ControlsPanel();

    // Cached maze dimensions
    private int myLastRows = -1, myLastCols = -1;

    /**
     * Constructor to initialize layout and interaction logic.
     * @param theView The parent game view
     */
    public MazePanel(final GameView theView) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8,8,8,8));
        setupHud();
        setupCenterArea();
        setupKeyBindings();
        setFocusable(true);
    }

    /** Initializes the top display bar showing player position and hints. */
    private void setupHud() {
        final JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        myPosLabel.setFont(FONT_META);
        myHintsLabel.setFont(FONT_META);
        hud.add(myPosLabel);
        hud.add(myHintsLabel);
        add(hud, BorderLayout.NORTH);
    }

    /** Prepares the center area layout, including grid and sidebar. */
    private void setupCenterArea() {
        final JPanel center = new JPanel(new BorderLayout(8,0));
        add(center, BorderLayout.CENTER);

        myGridHolder.setLayout(new BorderLayout());
        myGridHolder.setBackground(COL_GRID_BG);
        myGridHolder.setBorder(new EmptyBorder(8,8,8,8));
        center.add(myGridHolder, BorderLayout.CENTER);

        final JPanel sidebar = buildRightSidebar();
        center.add(sidebar, BorderLayout.EAST);
    }

    /** Binds keyboard shortcuts to actions. */
    private void setupKeyBindings() {
        bindKey("UP",    () -> fireMove(Direction.NORTH));
        bindKey("DOWN",  () -> fireMove(Direction.SOUTH));
        bindKey("LEFT",  () -> fireMove(Direction.WEST));
        bindKey("RIGHT", () -> fireMove(Direction.EAST));
        bindKey("P", () -> { if (myOnPause   != null) myOnPause.run(); });
        bindKey("N", () -> { if (myOnNewGame != null) myOnNewGame.run(); });
        bindKey("S", () -> { if (myOnSave    != null) myOnSave.run(); });
        bindKey("Q", () -> { if (myOnQuit    != null) myOnQuit.run(); });
    }

    /** Updates HUD text. */
    public void setHud(final int theX, final int theY, final int theHintsLeft) {
        myPosLabel.setText("Pos: (" + theX + "," + theY + ")");
        myHintsLabel.setText("Hints: " + (theHintsLeft == Integer.MAX_VALUE ? "∞" : theHintsLeft));
    }

    /** Updates door attempts label. */
    public void setDoorAttemptsLabel(final Integer theAttemptsLeft) {
        if (theAttemptsLeft == null) {
            myAttemptsDoorLabel.setText("—");
        } else {
            myAttemptsDoorLabel.setText(theAttemptsLeft == Integer.MAX_VALUE ? "∞" : theAttemptsLeft.toString());
        }
    }

    /**
     * Redraws the grid view with player and room data.
     */
    public void render(final Maze theMaze, final Player thePlayer) {
        ensureGrid(theMaze.getRows(), theMaze.getCols());
        updatePositionPanel(theMaze, thePlayer);
        updateCells(theMaze, thePlayer);
        updateDirectionChips(theMaze);
        revalidate();
        repaint();
    }

    public DirectionPanel getDirectionPanel() { return myDirectionPanel; }

    /**
     * This updates the position panel with the player's current coordinates and
     * gives distance to the maze exit, or indicates if no path exists.
     *
     * @param theMaze the current maze
     * @param thePlayer the player navigating the maze
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
     * This updates each cell in the maze grid based on the player's current position,
     * start and exit cells, and blocked or unblocked room status.
     *
     * @param theMaze the current maze
     * @param thePlayer the player navigating the maze
     */
    private void updateCells(final Maze theMaze, final Player thePlayer) {
        final int rows = theMaze.getRows(), cols = theMaze.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                final boolean isPlayer = (thePlayer.getX() == r && thePlayer.getY() == c);
                final boolean isStart = (r == 0 && c == 0);
                final boolean isExit  = (r == rows - 1 && c == cols - 1);
                final Cell cell = myCells[r][c];

                if (isPlayer) cell.setState(Cell.State.PLAYER);
                else if (isExit) cell.setState(Cell.State.EXIT);
                else if (isStart) cell.setState(Cell.State.START);
                else updateCellState(cell, theMaze, r, c);
            }
        }
    }

    /**
     * This updates the state of a specific cell based on the presence of blocked doors
     * in the corresponding room of the maze.
     *
     * @param theCell the cell to update
     * @param theMaze the maze containing the room
     * @param theRow the row index of the room
     * @param theCol the column index of the room
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
     * This enables or disables directional chips based on available doors in the current room.
     *
     * @param theMaze the current maze
     */
    private void updateDirectionChips(final Maze theMaze) {
        final Room room = theMaze.getCurrentRoom();
        final Set<Direction> available = (room != null)
                ? room.getAvailableDirections()
                : EnumSet.noneOf(Direction.class);

        setChipEnabled(myNorthLabel, available.contains(Direction.NORTH));
        setChipEnabled(mySouthLabel, available.contains(Direction.SOUTH));
        setChipEnabled(myEastLabel,  available.contains(Direction.EAST));
        setChipEnabled(myWestLabel,  available.contains(Direction.WEST));
    }

    /**
     * This checks whether the given room has any blocked door.
     *
     * @param theRoom the room to inspect
     * @return true if at least one door is blocked; false otherwise
     */
    private boolean roomHasBlockedDoor(final Room theRoom) {
        for (Direction dir : Direction.values()) {
            Door door = theRoom.getDoor(dir);
            if (door != null && door.isBlocked()) return true;
        }
        return false;
    }

    /**
     * This builds the right sidebar panel containing position, control, and direction panels.
     *
     * @return a JPanel representing the sidebar
     */
    private JPanel buildRightSidebar() {
        final JPanel sidebar = new JPanel(new GridLayout(3, 1));
        sidebar.add(myPositionPanel);
        sidebar.add(myControlsPanel);
        sidebar.add(myDirectionPanel);
        return sidebar;
    }

    /**
     * This creates a visual direction chip label with styling.
     *
     * @param theText the label text
     * @return a styled JLabel acting as a direction chip
     */
    private static JLabel createChip(final String theText) {
        final JLabel label = new JLabel(theText, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(230,230,230));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(2,6,2,6)));
        label.setFont(FONT_META);
        return label;
    }

    /**
     * This sets the visual style of a chip based on its enabled/disabled state.
     *
     * @param theChip the JLabel chip to update
     * @param isEnabled true to enable and highlight; false to dim
     */
    private void setChipEnabled(final JLabel theChip, final boolean isEnabled) {
        theChip.setBackground(isEnabled ? new Color(200,255,200) : new Color(235,235,235));
        theChip.setForeground(isEnabled ? Color.BLACK : new Color(120,120,120));
    }

    /**
     * This ensures the grid panel matches the given number of rows and columns.
     * If not, rebuilds the grid and repopulates it with new cells.
     *
     * @param theRows the desired number of rows
     * @param theCols the desired number of columns
     */
    private void ensureGrid(final int theRows, final int theCols) {
        if (theRows == myLastRows && theCols == myLastCols && myGrid != null) return;
        myLastRows = theRows; myLastCols = theCols;

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


    private void bindKey(final String theKey, final Runnable theAction) {
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(theKey), theKey);
        getActionMap().put(theKey, new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { theAction.run(); }
        });
    }

    private void fireMove(final Direction theDirection) {
        if (myOnMove != null) myOnMove.accept(theDirection);
    }

    /** This represents single cell on the maze grid, rendered with a label and background color. */
    private static final class Cell extends JPanel {
        enum State { ROOM, START, EXIT, PLAYER, BLOCKED }

        private final JLabel myLabel = new JLabel("ROOM", SwingConstants.CENTER);

        Cell() {
            setLayout(new BorderLayout());
            setBackground(COL_ROOM);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210,210,210)),
                    new EmptyBorder(18, 18, 18, 18)));
            myLabel.setFont(FONT_META);
            add(myLabel, BorderLayout.CENTER);
        }

        void setState(final State theState) {
            switch (theState) {
                case PLAYER -> { setBackground(COL_PLAYER); myLabel.setText("PLAYER"); }
                case START  -> { setBackground(COL_START);  myLabel.setText("START"); }
                case EXIT   -> { setBackground(COL_EXIT);   myLabel.setText("EXIT"); }
                case BLOCKED-> { setBackground(COL_BLOCKED); myLabel.setText("BLOCKED"); }
                default      -> { setBackground(COL_ROOM);    myLabel.setText("ROOM"); }
            }
        }
    }
}
