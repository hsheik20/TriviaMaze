package Test;

import Model.Direction;
import Model.Door;
import Model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private Room room;
    private Room neighbor;
    private Door door;

    @BeforeEach
    void setUp() {
        room = new Room(1, 2);
        neighbor = new Room(1, 3);
        door = new Door(room, neighbor, null);
    }

    @Test
    void testCoordinates() {
        assertEquals(1, room.getRow());
        assertEquals(2, room.getCol());
    }

    @Test
    void testVisitedFlags() {
        assertFalse(room.isVisited(), "Should be unvisited initially");
        room.markVisited();
        assertTrue(room.isVisited(), "Should be visited after markVisited");
        room.clearVisited();
        assertFalse(room.isVisited(), "Should be unvisited after clearVisited");
    }

    @Test
    void testSetAndGetDoor() {
        assertNull(room.getDoor(Direction.EAST));
        room.setDoor(Direction.EAST, door);
        assertSame(door, room.getDoor(Direction.EAST));
        assertNull(room.getDoor(Direction.NORTH));
    }

    @Test
    void testAvailableDirections() {
        room.setDoor(Direction.EAST, door);
        Set<Direction> dirs = room.getAvailableDirections();
        assertEquals(1, dirs.size());
        assertTrue(dirs.contains(Direction.EAST));
    }

    @Test
    void testAvailableDirectionsIsUnmodifiable() {
        room.setDoor(Direction.EAST, door);
        Set<Direction> dirs = room.getAvailableDirections();
        assertThrows(UnsupportedOperationException.class, () -> dirs.add(Direction.NORTH));
    }

    @Test
    void testInvalidArgs() {
        assertThrows(NullPointerException.class, () -> room.setDoor(null, door));
        assertThrows(NullPointerException.class, () -> room.setDoor(Direction.EAST, null));
    }

    @Test
    void testNegativeCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new Room(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Room(0, -1));
    }
}
