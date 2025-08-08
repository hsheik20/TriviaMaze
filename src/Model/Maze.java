package Model;

import java.io.Serializable;

/**
 *
 * Represents rectangular grid composed of rooms connected by doors making up trivia maze
 * supports movement, state tracking, and reset
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Room[][] myGrid;
    private final int myRows, myCols;
    private final Room myStartRoom, myExitRoom;
    private Room myCurrentPosition;

    /**
     *
     * Constructs new maze with specified amount of rows and colunms
     * Initializes grid, room, doors, and starting position
     * @param rows number of rows in maze
     * @param cols number of colunms in maze
     * @throws IllegalArgumentException if rows and cols are less then 1
     */
    public Maze(final int theRows, final int theCols) {
        validateMazeDimensions(theRows, theCols);
        this.myRows = theRows;
        this.myCols = theCols;
        myGrid = new Room[myRows][myCols];
        createRooms();
        connectDoors();
        myStartRoom = myGrid[0][0];
        myExitRoom = myGrid[myRows - 1][myCols - 1];

        myCurrentPosition = myStartRoom;
        myStartRoom.markVisited();
    }

    /**
     * Returns number of rows in maze
     * @return the row count
     */
    public int getRows() {
        return myRows;
    }

    /**
     * Returns number of colunms in maze
     * @return colunm count
     */
    public int getCols() {
        return myCols;
    }

    /**
     * Fetch room at given position
     * @param row the row index of room
     * @param col the colunm of room
     * @return room object at specified coordinates
     * @throws  IndexOutOfBoundsException if coordinates are outside maze bounds
     */
    public Room getRoom(final int theRow, final int theCol) {
        if (theRow < 0 || theRow >= myRows || theCol < 0 || theCol >= myCols) {
            throw new IndexOutOfBoundsException("Invalid room coordinates: (" + theRow + ", " + theCol + ")");
        }
        return myGrid[theRow][theCol];
    }

    /**
     * This moves player from current room through door in specified direction.
     * If no door exists it remains blocked and the player does not move.
     * @param direction compass direction to move(NWSE)
     * @return true if move was a success, false otherewise
     */
    public boolean move(final Direction theDirection) {
        boolean validMove = false;
        Door door = myCurrentPosition.getDoor(theDirection);
        if (door != null && !door.isBlocked()) {
            Room next = door.getNextRoom(myCurrentPosition);
            myCurrentPosition = next;
            myCurrentPosition.markVisited();
            validMove = true;
        }
        return validMove;
    }

    /**
     * Returns room player is currently in
     * @return current room of player
     */
    public Room getCurrentRoom() {
        return myCurrentPosition;
    }

    /**
     * Returns true if player is standing in maze exit room
     * @return true if player at exit room, false otherwise
     */
    public boolean isAtExit() {
        return myCurrentPosition == myExitRoom;
    }

    /**
     * Resets maze to starting point, player back at start point and all rooms marked unvisited
     */
    public void reset() {
        myCurrentPosition = myStartRoom;
        for (final Room[] row : myGrid) {
            for (final Room room : row) {
                room.clearVisited();
            }
        }
        myStartRoom.markVisited();
    }

    /**
     * This validates given height and width are acceptable for maze
     *
     * @throws  IllegalArgumentException if row or colunm less than 1
     */
    private void validateMazeDimensions(final int theR, final int theC) {
        if (theR < 1 || theC < 1) {
            throw new IllegalArgumentException("Rows and columns must be greater than or equal to 1.");
        }
    }

    /**
     * This instantiates and storing room objects into grid
     */
    private void createRooms() {
        for (int r = 0; r < myRows; r++) {
            for (int c = 0; c < myCols; c++) {
                myGrid[r][c] = new Room(r, c);
            }
        }
    }

    /**
     * Connects every pair of adjacent rooms with doors.
     */
    private void connectDoors() {
        for (int r = 0; r < myRows; r++) {
            for (int c = 0; c < myCols; c++) {
                final Room room = myGrid[r][c];
                // Connect to the room above (North)
                if (r > 0) {
                    final Door nsDoor = new Door(room, myGrid[r - 1][c], null);
                    room.setDoor(Direction.NORTH, nsDoor);
                    myGrid[r - 1][c].setDoor(Direction.SOUTH, nsDoor);
                }
                // Connect to the room to the left (West)
                if (c > 0) {
                    final Door weDoor = new Door(room, myGrid[r][c - 1], null);
                    room.setDoor(Direction.WEST, weDoor);
                    myGrid[r][c - 1].setDoor(Direction.EAST, weDoor);
                }
            }
        }
    }
}