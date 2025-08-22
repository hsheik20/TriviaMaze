package Test;

import Model.Direction;
import Model.Door;
import Model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the {@link Room} model.
 * It verifies the correct functionality of the Room class, including
 * coordinate handling, visited state management, door associations,
 * and the integrity of available directions.
 *
 * @author Husein
 */
class RoomTest {
    private Room room;
    private Room neighbor;
    private Door door;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new {@link Room} object at coordinates (1, 2),
     * a neighboring room, and a {@link Door} connecting them.
     */
    @BeforeEach
    void setUp() {
        room = new Room(1, 2);
        neighbor = new Room(1, 3);
        door = new Door(room, neighbor, null);
    }

    /**
     * Tests that the {@code getRow()} and {@code getCol()} methods
     * return the correct coordinates that the room was initialized with.
     */
    @Test
    void testCoordinates() {
        assertEquals(1, room.getRow());
        assertEquals(2, room.getCol());
    }

    /**
     * Tests the visited state flags of the room. It verifies that a room
     * is initially unvisited, can be marked as visited, and can have
     * its visited state cleared.
     */
    @Test
    void testVisitedFlags() {
        assertFalse(room.isVisited(), "Should be unvisited initially");
        room.markVisited();
        assertTrue(room.isVisited(), "Should be visited after markVisited");
        room.clearVisited();
        assertFalse(room.isVisited(), "Should be unvisited after clearVisited");
    }

    /**
     * Tests the {@code setDoor()} and {@code getDoor()} methods.
     * It ensures that a door can be correctly associated with a direction
     * and that retrieving the door for that direction works as expected.
     */
    @Test
    void testSetAndGetDoor() {
        assertNull(room.getDoor(Direction.EAST));
        room.setDoor(Direction.EAST, door);
        assertSame(door, room.getDoor(Direction.EAST));
        assertNull(room.getDoor(Direction.NORTH));
    }

    /**
     * Tests the {@code getAvailableDirections()} method. It verifies that
     * the returned set of directions accurately reflects the doors that
     * have been set for the room.
     */
    @Test
    void testAvailableDirections() {
        room.setDoor(Direction.EAST, door);
        Set<Direction> dirs = room.getAvailableDirections();
        assertEquals(1, dirs.size());
        assertTrue(dirs.contains(Direction.EAST));
    }

    /**
     * Tests the immutability of the set returned by {@code getAvailableDirections()}.
     * It ensures that the set is a copy or unmodifiable view, and attempting
     * to modify it throws an {@link UnsupportedOperationException}.
     */
    @Test
    void testAvailableDirectionsIsUnmodifiable() {
        room.setDoor(Direction.EAST, door);
        Set<Direction> dirs = room.getAvailableDirections();
        assertThrows(UnsupportedOperationException.class, () -> dirs.add(Direction.NORTH));
    }

    /**
     * Tests the input validation of the {@code setDoor()} method.
     * It verifies that passing {@code null} for either the direction or the
     * door throws a {@link NullPointerException}.
     */
    @Test
    void testInvalidArgs() {
        assertThrows(NullPointerException.class, () -> room.setDoor(null, door));
        assertThrows(NullPointerException.class, () -> room.setDoor(Direction.EAST, null));
    }

    /**
     * Tests the constructor's input validation.
     * It ensures that the constructor throws an {@link IllegalArgumentException}
     * if negative values are provided for the row or column coordinates.
     */
    @Test
    void testNegativeCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new Room(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Room(0, -1));
    }
}