package Model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Map;
import java.util.Set;

/**
 * This represents a room, a cell in trivia maze. It looks which doors lead out to other doors in each of 4 directions.
 *
 * @author Husein & Chan
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int myRow;
    private final int myCol;
    private boolean myVisited = false;
    private final Map<Direction, Door> myDoors = new EnumMap<>(Direction.class);


    /**
     * This creates new room at the specified coordinates
     *
     * @param theRow the row index
     * @param theCol the colunm index
     * @throws IllegalArgumentException if row or colunm is negative
     */
    public Room(final int theRow, final int theCol) {
        if (theRow < 0 || theCol < 0) {
            throw new IllegalArgumentException("Rows and cols must be non-negative");
        }
        myRow = theRow;
        myCol = theCol;
    }

    /**
     * This fetches the room's row
     * @return the row of the room
     */
    public int getRow() {
        return myRow;
    }
    /**
     * This fetches the room's colunm
     * @return the colunm of the room
     */
    public int getCol() {
        return myCol;
    }

    public boolean isVisited() {
      return myVisited;
    }

    /**
     * This marks a room as visited
     *
     */
    public void markVisited() {
        myVisited = true;
    }

    /**
     * This clears and resets all visited rooms
     */
    public void clearVisited() {
        myVisited = false;
    }

    /**
     * Attach a Door in the specified direction from this room.
     *
     * @param theDir  the compass direction; must not be null
     * @param theDoor the Door object; must not be null
     * @throws NullPointerException if either theDir or theDoor is null
     */
    public void setDoor(final Direction theDir, final Door theDoor) {
        Objects.requireNonNull(theDir,  "theDir must not be null");
        Objects.requireNonNull(theDoor, "theDoor must not be null");
        myDoors.put(theDir, theDoor);

    }

    /**
     * This returns the direction of the next door
     *
     * @param theDir the direction to check for door
     * @return the door leading in given direction
     */
    public Door getDoor(final Direction theDir) {

        return myDoors.get(theDir);

    }

    /**
     * This returns set of directions which room currently has doors
     */
    public Set<Direction> getAvailableDirections() {
        return Set.copyOf(myDoors.keySet());
    }








}
