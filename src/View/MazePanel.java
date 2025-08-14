package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel for displaying and interacting with the maze.
 * Shows rooms, doors, player position, and blocked paths.
 *
 * @author Generated for Trivia Maze
 */
public class MazePanel extends JPanel {

    private static final int ROOM_SIZE = 80;
    private static final int DOOR_SIZE = 20;
    private static final Color ROOM_COLOR = Color.WHITE;
    private static final Color CURRENT_ROOM_COLOR = new Color(144, 238, 144);
    private static final Color EXIT_ROOM_COLOR = new Color(255, 215, 0);
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color DOOR_OPEN_COLOR = new Color(173, 216, 230);
    private static final Color DOOR_BLOCKED_COLOR = Color.RED;
    private static final Color PLAYER_COLOR = Color.BLUE;

    private Game myGame;
    private Maze myMaze;
    private Player myPlayer;
    private int myMazeWidth;
    private int myMazeHeight;
    private ActionListener myMoveListener;

    /**
     * Constructs a maze panel for displaying the game maze.
     *
     * @param game The game instance
     */
    public MazePanel(Game game) {
        myGame = game;
        myMaze = game.getMaze();
        myPlayer = game.getPlayer();

        initializePanel();
        calculateDimensions();
    }

    /**
     * Initializes the panel properties.
     */
    private void initializePanel() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createTitledBorder("Maze"));
        setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Calculates maze dimensions for rendering.
     */
    private void calculateDimensions() {
        // These would typically come from the maze instance
        // For now, using default values that can be overridden
        myMazeWidth = 4;
        myMazeHeight = 4;

        int panelWidth = (myMazeWidth * ROOM_SIZE) + ((myMazeWidth - 1) * DOOR_SIZE) + 40;
        int panelHeight = (myMazeHeight * ROOM_SIZE) + ((myMazeHeight - 1) * DOOR_SIZE) + 60;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            drawMaze(g2d);
            drawPlayer(g2d);
            drawLegend(g2d);
        } finally {
            g2d.dispose();
        }
    }

    /**
     * Draws the maze structure including rooms and doors.
     *
     * @param g2d Graphics context
     */
    private void drawMaze(Graphics2D g2d) {
        int startX = 20;
        int startY = 40;

        // Draw rooms and doors
        for (int row = 0; row < myMazeHeight; row++) {
            for (int col = 0; col < myMazeWidth; col++) {
                int x = startX + col * (ROOM_SIZE + DOOR_SIZE);
                int y = startY + row * (ROOM_SIZE + DOOR_SIZE);

                // Draw room
                drawRoom(g2d, x, y, row, col);

                // Draw doors (horizontal and vertical)
                if (col < myMazeWidth - 1) {
                    drawHorizontalDoor(g2d, x + ROOM_SIZE, y, row, col);
                }
                if (row < myMazeHeight - 1) {
                    drawVerticalDoor(g2d, x, y + ROOM_SIZE, row, col);
                }
            }
        }
    }

    /**
     * Draws a single room at the specified position.
     *
     * @param g2d Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param row Room row
     * @param col Room column
     */
    private void drawRoom(Graphics2D g2d, int x, int y, int row, int col) {
        Color roomColor = ROOM_COLOR;

        // Determine room color based on state
        if (isCurrentRoom(row, col)) {
            roomColor = CURRENT_ROOM_COLOR;
        } else if (isExitRoom(row, col)) {
            roomColor = EXIT_ROOM_COLOR;
        }

        // Draw room
        g2d.setColor(roomColor);
        g2d.fillRect(x, y, ROOM_SIZE, ROOM_SIZE);

        // Draw room border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, ROOM_SIZE, ROOM_SIZE);

        // Draw room coordinates
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        String coords = "(" + row + "," + col + ")";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (ROOM_SIZE - fm.stringWidth(coords)) / 2;
        int textY = y + (ROOM_SIZE + fm.getAscent()) / 2;
        g2d.drawString(coords, textX, textY);
    }

    /**
     * Draws a horizontal door between rooms.
     *
     * @param g2d Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param row Room row
     * @param col Room column
     */
    private void drawHorizontalDoor(Graphics2D g2d, int x, int y, int row, int col) {
        boolean isBlocked = isDoorBlocked(row, col, Direction.EAST);
        Color doorColor = isBlocked ? DOOR_BLOCKED_COLOR : DOOR_OPEN_COLOR;

        g2d.setColor(doorColor);
        g2d.fillRect(x, y + (ROOM_SIZE - DOOR_SIZE) / 2, DOOR_SIZE, DOOR_SIZE);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y + (ROOM_SIZE - DOOR_SIZE) / 2, DOOR_SIZE, DOOR_SIZE);

        // Draw X if blocked
        if (isBlocked) {
            drawBlockedSymbol(g2d, x, y + (ROOM_SIZE - DOOR_SIZE) / 2, DOOR_SIZE, DOOR_SIZE);
        }
    }

    /**
     * Draws a vertical door between rooms.
     *
     * @param g2d Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param row Room row
     * @param col Room column
     */
    private void drawVerticalDoor(Graphics2D g2d, int x, int y, int row, int col) {
        boolean isBlocked = isDoorBlocked(row, col, Direction.SOUTH);
        Color doorColor = isBlocked ? DOOR_BLOCKED_COLOR : DOOR_OPEN_COLOR;

        g2d.setColor(doorColor);
        g2d.fillRect(x + (ROOM_SIZE - DOOR_SIZE) / 2, y, DOOR_SIZE, DOOR_SIZE);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x + (ROOM_SIZE - DOOR_SIZE) / 2, y, DOOR_SIZE, DOOR_SIZE);

        // Draw X if blocked
        if (isBlocked) {
            drawBlockedSymbol(g2d, x + (ROOM_SIZE - DOOR_SIZE) / 2, y, DOOR_SIZE, DOOR_SIZE);
        }
    }

    /**
     * Draws an X symbol to indicate a blocked door.
     *
     * @param g2d Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width of the area
     * @param height Height of the area
     */
    private void drawBlockedSymbol(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x + 2, y + 2, x + width - 2, y + height - 2);
        g2d.drawLine(x + width - 2, y + 2, x + 2, y + height - 2);
    }

    /**
     * Draws the player at their current position.
     *
     * @param g2d Graphics context
     */
    private void drawPlayer(Graphics2D g2d) {
        if (myPlayer == null) return;

        int startX = 20;
        int startY = 40;
        int playerRow = myPlayer.getX();
        int playerCol = myPlayer.getY();

        int x = startX + playerCol * (ROOM_SIZE + DOOR_SIZE) + ROOM_SIZE / 2;
        int y = startY + playerRow * (ROOM_SIZE + DOOR_SIZE) + ROOM_SIZE / 2;
        int playerSize = 16;

        // Draw player as a circle
        g2d.setColor(PLAYER_COLOR);
        g2d.fillOval(x - playerSize / 2, y - playerSize / 2, playerSize, playerSize);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - playerSize / 2, y - playerSize / 2, playerSize, playerSize);

        // Draw P inside the circle
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x - fm.stringWidth("P") / 2;
        int textY = y + fm.getAscent() / 2 - 1;
        g2d.drawString("P", textX, textY);
    }

    /**
     * Draws a legend explaining the maze symbols.
     *
     * @param g2d Graphics context
     */
    private void drawLegend(Graphics2D g2d) {
        int legendX = getWidth() - 150;
        int legendY = 20;

        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Legend:", legendX, legendY);

        int itemHeight = 18;
        int currentY = legendY + itemHeight;

        // Player
        g2d.setColor(PLAYER_COLOR);
        g2d.fillOval(legendX, currentY - 8, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Player", legendX + 20, currentY);
        currentY += itemHeight;

        // Current room
        g2d.setColor(CURRENT_ROOM_COLOR);
        g2d.fillRect(legendX, currentY - 8, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Current", legendX + 20, currentY);
        currentY += itemHeight;

        // Exit room
        g2d.setColor(EXIT_ROOM_COLOR);
        g2d.fillRect(legendX, currentY - 8, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Exit", legendX + 20, currentY);
        currentY += itemHeight;

        // Open door
        g2d.setColor(DOOR_OPEN_COLOR);
        g2d.fillRect(legendX, currentY - 8, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Open Door", legendX + 20, currentY);
        currentY += itemHeight;

        // Blocked door
        g2d.setColor(DOOR_BLOCKED_COLOR);
        g2d.fillRect(legendX, currentY - 8, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Blocked", legendX + 20, currentY);
    }

    /**
     * Checks if the specified room is the current player room.
     *
     * @param row Room row
     * @param col Room column
     * @return True if this is the current room
     */
    private boolean isCurrentRoom(int row, int col) {
        if (myPlayer == null) return false;
        return myPlayer.getX() == row && myPlayer.getY() == col;
    }

    /**
     * Checks if the specified room is the exit room.
     *
     * @param row Room row
     * @param col Room column
     * @return True if this is the exit room
     */
    private boolean isExitRoom(int row, int col) {
        // This would typically check against the maze's exit position
        // For now, assuming exit is at bottom-right corner
        return row == myMazeHeight - 1 && col == myMazeWidth - 1;
    }

    /**
     * Checks if a door in the specified direction from a room is blocked.
     *
     * @param row Room row
     * @param col Room column
     * @param direction Direction of the door
     * @return True if the door is blocked
     */
    private boolean isDoorBlocked(int row, int col, Direction direction) {
        // This would typically check the actual maze state
        // For now, returning false (all doors open) for demonstration
        // In real implementation: return myMaze.getRoomAt(row, col).getDoor(direction).isBlocked();
        return false;
    }

    /**
     * Updates the maze display with current game state.
     */
    public void updateDisplay() {
        repaint();
    }

    /**
     * Sets the maze dimensions for rendering.
     *
     * @param width Maze width
     * @param height Maze height
     */
    public void setMazeDimensions(int width, int height) {
        myMazeWidth = width;
        myMazeHeight = height;
        calculateDimensions();
        revalidate();
        repaint();
    }

    /**
     * Sets the action listener for movement actions.
     *
     * @param listener The action listener
     */
    public void setMoveActionListener(ActionListener listener) {
        myMoveListener = listener;
    }
}