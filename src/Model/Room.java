package Model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * This represents a room, a cell in trivia maze. It looks which doors lead out to other doors in each of 4 directions.
 */
public class Room {
    private final int row;
    private final int col;
    private boolean visited = false;
    private final Map<Direction, Door> doors = new EnumMap<>(Direction.class);

    /**
     * This creates new room at the specified coordinates
     *
     * @param row the row index
     * @param col the colunm index
     * @throws IllegalArgumentException if row or colunm is negative
     */
    public Room(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Rows and cols must be non-negative");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * This fetches the room's row
     * @return the row of the room
     */
    public int getRow() {
        return row;
    }
    /**
     * This fetches the room's colunm
     * @return the colunm of the room
     */
    public int getCol() {
        return col;
    }

    public boolean isVisited() {
      return visited;
    }

    /**
     * This marks a room as visited
     *
     */
    public void markVisited() {
        this.visited = true;
    }

    /**
     * This clears and resets all visited rooms
     */
    public void clearVisited() {
        this.visited = false;
    }

    /**
     * This attaches a door to the room in the given direction
     *
     * @param dir the direction of the door
     * @param door the door object
     * @throws IllegalArgumentException if dir or door is null
     */
    public void setDoor(Direction dir, Door door) {
        if(dir == null || door == null) {
            throw new IllegalArgumentException("Direction and Door must be non-null");
        }
        doors.put(dir, door);
    }

    /**
     * This returns the direction of the next door
     *
     * @param dir the direction to check for door
     * @return the door leading in given direction
     */
    public Door getDoor(Direction dir) {
        return doors.get(dir);
    }

    /**
     * This returns set of directions which room currently has doors
     * @return
     */
    public Set<Direction> getAvailableDirections() {
        return Set.copyOf(doors.keySet());
    }





}
