// View/MazePanel.java
package View;

import Model.Direction;
import Model.Maze;
import Model.Player;
import Model.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public class MazePanel extends JPanel {

    /* --------- Public callbacks (wired by controller) --------- */
    private Consumer<Direction> onMove;
    private Runnable onPause;

    public void onMove(Consumer<Direction> c){ this.onMove = c; }
    public void onPause(Runnable r){ this.onPause = r; }

    /* ----------------- Colors & styling ----------------- */
    private static final Color COL_ROOM   = new Color(235,238,241);   // light gray card
    private static final Color COL_START  = new Color(210,230,250);   // soft blue
    private static final Color COL_EXIT   = new Color(217,247,223);   // soft green
    private static final Color COL_PLAYER = new Color(255,239,170);   // soft yellow
    private static final Color COL_GRID_BG= new Color(245,246,248);

    private static final Font  FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private static final Font  FONT_META  = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /* ----------------- Layout pieces ----------------- */
    private final JPanel gridHolder   = new JPanel();  // wraps the grid, rebuilt on size change
    private JPanel grid;                                // actual grid with cells
    private Cell[][] cells;

    // top HUD
    private final JLabel posLbl   = new JLabel("Pos: (0,0)");
    private final JLabel hintsLbl = new JLabel("Hints: ∞");

    // right sidebar
    private final JLabel attemptsDoorLbl = new JLabel("—");
    private final JLabel northLbl = chip("NORTH"), southLbl = chip("SOUTH"),
            eastLbl  = chip("EAST"),  westLbl  = chip("WEST");

    // bottom controls
    private final JButton myUp    = new JButton("↑");
    private final JButton myDown = new JButton("↓");
    private final JButton myLeft= new JButton("←");
    private final JButton myRight = new JButton("→");
    private final JButton myPause = new JButton("Pause");

    // cached dims (so we can rebuild grid only when rows/cols change)
    private int lastRows = -1, lastCols = -1;

    public MazePanel(final GameView view) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8,8,8,8));

        /* ---------- HUD (NORTH) ---------- */
        final JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        posLbl.setFont(FONT_META);
        hintsLbl.setFont(FONT_META);
        hud.add(posLbl);
        hud.add(hintsLbl);
        add(hud, BorderLayout.NORTH);

        /* ---------- Center area (grid + right rail) ---------- */
        final JPanel center = new JPanel(new BorderLayout(8,0));
        add(center, BorderLayout.CENTER);

        // grid holder (we swap actual grid panel inside when maze size changes)
        gridHolder.setLayout(new BorderLayout());
        gridHolder.setBackground(COL_GRID_BG);
        gridHolder.setBorder(new EmptyBorder(8,8,8,8));
        center.add(gridHolder, BorderLayout.CENTER);

        // right sidebar
        final JPanel right = buildRightSidebar();
        center.add(right, BorderLayout.EAST);

        /* ---------- Bottom controls ---------- */
        final JPanel controls = new JPanel();
        controls.add(myUp); controls.add(myLeft); controls.add(myDown); controls.add(myRight); controls.add(myPause);
        add(controls, BorderLayout.SOUTH);

        // actions
        myUp.addActionListener(e -> fireMove(Direction.NORTH));
        myDown.addActionListener(e -> fireMove(Direction.SOUTH));
        myLeft.addActionListener(e -> fireMove(Direction.WEST));
        myRight.addActionListener(e -> fireMove(Direction.EAST));
        myPause.addActionListener(e -> { if (onPause != null) onPause.run(); });

        // key bindings
        bindKey("UP",    () -> fireMove(Direction.NORTH));
        bindKey("W",     () -> fireMove(Direction.NORTH));
        bindKey("DOWN",  () -> fireMove(Direction.SOUTH));
        bindKey("S",     () -> fireMove(Direction.SOUTH));
        bindKey("LEFT",  () -> fireMove(Direction.WEST));
        bindKey("A",     () -> fireMove(Direction.WEST));
        bindKey("RIGHT", () -> fireMove(Direction.EAST));
        bindKey("D",     () -> fireMove(Direction.EAST));
        bindKey("P",     () -> { if (onPause != null) onPause.run(); });

        setFocusable(true);
    }

    /* ============================================================
       Public API used by the controller
       ============================================================ */

    /** Update HUD numbers. */
    public void setHud(final int x, final int y, final int hintsLeft) {
        posLbl.setText("Pos: (" + x + "," + y + ")");
        hintsLbl.setText("Hints: " + (hintsLeft == Integer.MAX_VALUE ? "∞" : hintsLeft));
    }

    /** Show the remaining attempts for the door currently being asked about. */
    public void setDoorAttemptsLabel(final Integer attemptsLeftOrNull) {
        if (attemptsLeftOrNull == null) {
            attemptsDoorLbl.setText("—");
        } else {
            attemptsDoorLbl.setText(
                    attemptsLeftOrNull == Integer.MAX_VALUE ? "∞" : attemptsLeftOrNull.toString()
            );
        }
    }

    /** Re-render the grid and the direction availability sidebar. */
    public void render(final Maze maze, final Player player) {
        ensureGrid(maze.getRows(), maze.getCols());

        // paint cells
        final int rows = maze.getRows(), cols = maze.getCols();
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                final boolean isPlayer = (player.getX()==r && player.getY()==c);
                final boolean isStart  = (r==0 && c==0);
                final boolean isExit   = (r==rows-1 && c==cols-1);
                final Cell cell = cells[r][c];

                if (isPlayer)      cell.setState(Cell.State.PLAYER);
                else if (isExit)   cell.setState(Cell.State.EXIT);
                else if (isStart)  cell.setState(Cell.State.START);
                else               cell.setState(Cell.State.ROOM);
            }
        }

        // update “available directions” rail
        final Room cur = maze.getCurrentRoom();
        final Set<Direction> avail = (cur != null)
                ? cur.getAvailableDirections()
                : EnumSet.noneOf(Direction.class);

        setChipEnabled(northLbl, avail.contains(Direction.NORTH));
        setChipEnabled(southLbl, avail.contains(Direction.SOUTH));
        setChipEnabled(eastLbl,  avail.contains(Direction.EAST));
        setChipEnabled(westLbl,  avail.contains(Direction.WEST));

        revalidate();
        repaint();
    }

    /* ============================================================
       Internal helpers
       ============================================================ */

    private JPanel buildRightSidebar() {
        final JPanel rail = new JPanel();
        rail.setLayout(new BoxLayout(rail, BoxLayout.Y_AXIS));
        rail.setBorder(new EmptyBorder(4, 8, 4, 8));

        final JLabel hudTitle = new JLabel("Stats");
        hudTitle.setFont(FONT_TITLE);

        final JPanel stats = new JPanel(new GridLayout(0,1,0,4));
        stats.add(labelRow("Hints left:",  hintsLbl)); // reuse
        stats.add(labelRow("Attempts (door):", attemptsDoorLbl));

        final JLabel dirTitle = new JLabel("Available");
        dirTitle.setFont(FONT_TITLE);

        final JPanel dirs = new JPanel(new GridLayout(0,1,0,4));
        dirs.add(northLbl); dirs.add(southLbl); dirs.add(eastLbl); dirs.add(westLbl);

        final JLabel ctrlTitle = new JLabel("Controls");
        ctrlTitle.setFont(FONT_TITLE);

        final JTextArea keys = new JTextArea("""
                ↑/W  : North
                ↓/S  : South
                ←/A  : West
                →/D  : East
                P    : Pause
                """);
        keys.setEditable(false);
        keys.setFont(FONT_META);
        keys.setOpaque(false);

        rail.add(hudTitle);
        rail.add(Box.createVerticalStrut(4));
        rail.add(stats);
        rail.add(Box.createVerticalStrut(12));
        rail.add(dirTitle);
        rail.add(Box.createVerticalStrut(4));
        rail.add(dirs);
        rail.add(Box.createVerticalStrut(12));
        rail.add(ctrlTitle);
        rail.add(Box.createVerticalStrut(4));
        rail.add(keys);

        return rail;
    }

    private JPanel labelRow(String label, JLabel value) {
        final JPanel p = new JPanel(new BorderLayout(6,0));
        final JLabel l = new JLabel(label);
        l.setFont(FONT_META);
        value.setFont(FONT_META);
        p.add(l, BorderLayout.WEST);
        p.add(value, BorderLayout.CENTER);
        p.setOpaque(false);
        return p;
    }

    private static JLabel chip(String text) {
        final JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(new Color(230,230,230));
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(2,6,2,6)
        ));
        l.setFont(FONT_META);
        return l;
    }

    private void setChipEnabled(JLabel chip, boolean enabled) {
        chip.setBackground(enabled ? new Color(200,255,200) : new Color(235,235,235));
        chip.setForeground(enabled ? Color.BLACK : new Color(120,120,120));
    }

    private void ensureGrid(int rows, int cols) {
        if (rows == lastRows && cols == lastCols && grid != null) return;

        lastRows = rows; lastCols = cols;

        grid = new JPanel(new GridLayout(rows, cols, 8, 8));
        grid.setOpaque(false);
        cells = new Cell[rows][cols];

        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                final Cell cell = new Cell();
                cells[r][c] = cell;
                grid.add(cell);
            }
        }

        gridHolder.removeAll();
        gridHolder.add(grid, BorderLayout.CENTER);
        gridHolder.revalidate();
        gridHolder.repaint();
    }

    private void bindKey(String key, Runnable r) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e){ r.run(); }
        });
    }

    private void fireMove(Direction d){ if (onMove != null) onMove.accept(d); }

    /* ----------------- Room cell "card" ----------------- */
    private static final class Cell extends JPanel {
        enum State { ROOM, START, EXIT, PLAYER }

        private final JLabel label = new JLabel("ROOM", SwingConstants.CENTER);

        Cell() {
            setLayout(new BorderLayout());
            setBackground(COL_ROOM);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210,210,210)),
                    new EmptyBorder(18, 18, 18, 18)
            ));
            label.setFont(FONT_META);
            add(label, BorderLayout.CENTER);
        }

        void setState(State s) {
            switch (s) {
                case PLAYER -> { setBackground(COL_PLAYER); label.setText("PLAYER"); }
                case START  -> { setBackground(COL_START);  label.setText("START"); }
                case EXIT   -> { setBackground(COL_EXIT);   label.setText("EXIT"); }
                default     -> { setBackground(COL_ROOM);   label.setText("ROOM"); }
            }
        }
    }
}
