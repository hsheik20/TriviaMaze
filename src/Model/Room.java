package Model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class Room {
    private final int row;
    private final int col;
    private boolean visited = false;
    private final Map<Direction, Door> doors = new EnumMap<>(Direction.class);

    public Room(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Rows and cols must be non-negative");
        }
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isVisited() {
      return visited;
    }
    public void markVisited() {
        this.visited = true;
    }
    public void clearVisited() {
        this.visited = false;
    }
    public void setDoor(Direction dir, Door door) {
        if(dir == null || door == null) {
            throw new IllegalArgumentException("Direction and Door must be non-null");
        }
        doors.put(dir, door);
    }
    public Door getDoor(Direction dir) {
        return doors.get(dir);
    }
    public Set<Direction> getAvailableDirections() {
        return Set.copyOf(doors.keySet());
    }





}
