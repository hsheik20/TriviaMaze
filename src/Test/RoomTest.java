package Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

import Model.Direction;
import Model.Door;
import Model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomTest {
    private Room room;
    private Room neighbor;
    private Door door;

    @BeforeEach
    void setUp() {
        room = new Room(1, 2);             // keep this as-is
        neighbor = new Room(1, 3);         // assign neighbor separately
        door = new Door(room, neighbor, null);

    }
    @Test
    void testCoordinates() {
        assertEquals(1, room.getRow());
        assertEquals(2, room.getCol());
    }
    @Test
    void testisVisited() {
        assertFalse(room.isVisited(), "Should be unvisited initially");
        room.markVisited();
        assertTrue(room.isVisited(), "Should be visited after marked visited");

    }
    @Test
    void testSetAndGetDoor() {
        room.setDoor(Direction.EAST, door);
        assertSame(door, room.getDoor(Direction.EAST));
        assertNull(room.getDoor(Direction.NORTH));
    }
    @Test
    void testAvailableDirections() {
        room.setDoor(Direction.EAST, door);
        Set<Direction> directions = room.getAvailableDirections();
        assertEquals(1, directions.size());
        assertTrue(directions.contains(Direction.EAST));
    }
    void testInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> {room.setDoor(null, door);});
        assertThrows(IllegalArgumentException.class, () -> {room.setDoor(Direction.EAST, null);});
    }

}
