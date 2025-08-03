package Model;

/**
 *
 * Represents rectangular grid composed of rooms connected by doors making up trivia maze
 * supports movement, state tracking, and reset
 */
public class Maze {
    private final Room[][] grid;
    private final int rows, cols;
    private final Room startRoom,exitRoom;
    private Room currentPosition;

    /**
     *
     * Constructs new maze with specified amount of rows and colunms
     * Initializes grid, room, doors, and starting position
     * @param rows number of rows in maze
     * @param cols number of colunms in maze
     * @throws IllegalArgumentException if rows and cols are less then 1
     */
    public Maze(int rows, int cols) {
        validateMazeDimensions(rows, cols);
        this.rows = rows;
        this.cols = cols;
        grid = new Room[rows][cols];
        createRooms();
        connectDoors();
        startRoom = grid[0][0];
        exitRoom =  grid[rows-1][cols-1];

        currentPosition = startRoom;
        startRoom.markVisited();

        }

    /**
     * Returns number of rows in maze
     * @return the row count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns number of colunms in maze
     * @return colunm count
     */
    public int getCols() {
        return cols;
    }

    /**
     * Fetch room at given position
     * @param row the row index of room
     * @param col the colunm of room
     * @return room object at specified coordinates
     * @throws  IndexOutOfBoundsException if coordinates are outside maze bounds
     */
    public Room getRoom(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid room coordinates: (" + row + ", " + col + ")");
            }
        return grid[row][col];
        }

    /**
     * This moves player from current room through door in specified direction.
     * If no door exists it remains blocked and the player does not move.
      * @param direction compass direction to move(NWSE)
     * @return true if move was a success, false otherewise
     */
    public boolean move(Direction direction) {
        boolean validmove = false;
        Door door = currentPosition.getDoor(direction);
        if (door != null && !door.isBlocked()) {
            Room next = door.getNextRoom(currentPosition);
            currentPosition = next;
            currentPosition.markVisited();
            validmove = true;
        }
        return validmove;

    }

    /**
     * Returns room player is currently in
     * @return current room of player
     */
    public Room getCurrentRoom() {
        return currentPosition;
    }

    /**
     * Returns true if player is standing in maze exit room
     * @return true if player at exit room, false otherwise
     */
    public boolean isAtExit() {
        return currentPosition == exitRoom;
    }

    /**
     * Resets maze to starting point, player back at start point and all rooms marked unvisited
     */
    public void reset() {
        currentPosition = startRoom;
        for (Room[] row : grid) {
            for (Room room : row) {
                room.clearVisited();
            }
        }
        startRoom.markVisited();
    }

    /**
     * This validates given height and width are acceptable for maze
     *
     * @throws  IllegalArgumentException if row or colunm less than 1
     */
    private void validateMazeDimensions(int r, int c) {
        if (r < 1 || c < 1) {
            throw new IllegalArgumentException(" rows/cols must be greater than 1");
        }
    }

    /**
     * This instantiates and storing room objects into grid
     */
    private void createRooms() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Room(r, c);
            }
        }

    }

    /**
     * This connects every pair of adjacent rooms
     */
    private void connectDoors() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Room room = grid[r][c];
                if (r > 0) {
                    Door nsDoor = new Door(room, grid[r - 1][c], null);
                    room.setDoor(Direction.NORTH, nsDoor);
                    grid[r - 1][c].setDoor(Direction.SOUTH, nsDoor);
                }
                if (c > 0) {
                    Door weDoor = new Door(room, grid[r][c - 1], null);
                    room.setDoor(Direction.WEST, weDoor);
                    grid[r][c - 1].setDoor(Direction.EAST, weDoor);
                }
            }
        }

    }






}
