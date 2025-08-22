package Model;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * Represents rectangular grid composed of rooms connected by doors making up trivia maze
 * supports movement, state tracking, and reset
 *
 * @author Husein
 */
public class Maze implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Room[][] myGrid;
    private final int myRows, myCols;
    private final Room myStartRoom, myExitRoom;
    private Room myCurrentPosition;
    private final questionFactory myQuestionFactory;

    /**
     *
     * Constructs new maze with specified amount of rows and colunms
     * Initializes grid, room, doors, and starting position
     * @param theRows number of rows in maze
     * @param theCols number of colunms in maze
     * @throws IllegalArgumentException if rows and cols are less then 1
     */
    public Maze(final int theRows, final int theCols, questionFactory theQuestionFactory) {
        validateMazeDimensions(theRows, theCols);
        this.myRows = theRows;
        this.myCols = theCols;
        myQuestionFactory = theQuestionFactory;
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
     * @param theRow the row index of room
     * @param theCol the colunm of room
     * @return room object at specified coordinates
     * @throws  IndexOutOfBoundsException if coordinates are outside maze bounds
     */
    public Room getRoom(final int theRow, final int theCol) {
        if (theRow < 0 || theRow >= myRows || theCol < 0 || theCol >= myCols) {
            throw new IndexOutOfBoundsException("Invalid room coordinates: (" + theRow + ", " + theCol + ")");
        }
        return myGrid[theRow][theCol];
    }
    public Room getStartRoom() {
        return myStartRoom;
    }
    public Room getExitRoom()  {
        return myExitRoom;
    }


    public Door getDoor(final Direction theDir) {
        return myCurrentPosition.getDoor(theDir);
    }

    public boolean canMove(final Direction theDir) {
        final Door door = getDoor(theDir);
        return door != null && !door.isBlocked();
    }

    /**
     * Step one room through an unblocked door. Assumes validation by caller.
     * Returns the new current room.
     */
    public Room step(final Direction theDir) {
        Door door = getDoor(theDir);
        if (door == null || door.isBlocked()) return myCurrentPosition;
        myCurrentPosition = door.getNextRoom(myCurrentPosition);
        myCurrentPosition.markVisited();
        door = getCurrentRoom().getDoor(theDir);
        System.out.println("Attempting to step through door: " + door);
        return myCurrentPosition;
    }

//    /**
//     * This moves player from current room through door in specified direction.
//     * If no door exists it remains blocked and the player does not move.
//      * @param theDirection compass direction to move(NWSE)
//     * @return true if move was a success, false otherewise
//     */
public boolean move(final Direction theDir) {
    if (!canMove(theDir)) return false;
    step(theDir);
    return true;
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

    /** Connectivity check from current position to exit using only unblocked doors. */
    public boolean hasPathToExitFromCurrent() {
        return hasPath(myCurrentPosition, myExitRoom);
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
                     Room upNeighbor = myGrid[r - 1][c];
                     Question question = myQuestionFactory.getNextAvailableQuestion();
                     Door door = new Door(room, upNeighbor, question);
                    System.out.println("Connecting door: " + door);
                     room.setDoor(Direction.NORTH, door);
                     upNeighbor.setDoor(Direction.SOUTH, door);
                    }

                // Connect to the room to the left (West)
                if (c > 0) {
                    Room leftNeighbor = myGrid[r][c - 1];
                    final Question question = myQuestionFactory.getNextAvailableQuestion();
                    Door door = new Door(room, leftNeighbor, question);
                    System.out.println("Connecting door: " + door);
                    room.setDoor(Direction.WEST, door);
                    leftNeighbor.setDoor(Direction.EAST, door);

                    }
                }
            }
        }

    // BFS over rooms via unblocked doors
    private boolean hasPath(final Room start, final Room goal) {
        if (start == goal) return true;

        final java.util.Set<Room> visited = new java.util.HashSet<>();
        final java.util.ArrayDeque<Room> q = new java.util.ArrayDeque<>();
        visited.add(start);
        q.add(start);

        while (!q.isEmpty()) {
            final Room r = q.poll();
            for (final Direction d : r.getAvailableDirections()) {
                final Door door = r.getDoor(d);
                if (door == null || door.isBlocked()) continue;
                final Room nxt = door.getNextRoom(r);
                if (nxt == goal) return true;
                if (visited.add(nxt)) q.add(nxt);
            }
        }
        return false;
    }
    }



