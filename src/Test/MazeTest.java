package Test;

import static org.junit.jupiter.api.Assertions.*;

import Model.Direction;
import Model.Door;
import Model.Maze;
import Model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MazeTest {

    private Maze maze;
    private Room r00, r01, r10, r11;

    @BeforeEach
    void setUp() {
        maze = new Maze(2, 2);
        r00  = maze.getRoom(0, 0);
        r01  = maze.getRoom(0, 1);
        r10  = maze.getRoom(1, 0);
        r11  = maze.getRoom(1, 1);
    }

    @Test
    void testReturnCorrectDimensions() {
        assertEquals(2, maze.getRows(), "getRows()");
        assertEquals(2, maze.getCols(), "getCols()");
    }

    @Test
    void testgetRoom() {
        assertSame(r00, maze.getRoom(0, 0), "getRoom(0,0)");
        assertSame(r11, maze.getRoom(1, 1), "getRoom(1,1)");
    }

    @Test
    void getRoom_invalidCoordinates() {
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(0, 2));
    }

    @Test
    void connectDoors() {

        Door east = r00.getDoor(Direction.EAST);
        assertNotNull(east, "Door to the EAST of r00 must exist");
        assertSame(r01, east.getNextRoom(r00), "east r(00)");


        assertNull(r00.getDoor(Direction.NORTH), "No door to the NORTH of r00");


        Door west = r11.getDoor(Direction.WEST);
        assertNotNull(west, "Door to the WEST of r11 must exist");
        assertSame(r10, west.getNextRoom(r11), "west r(11)");
    }

    @Test
    void testmoveBlocked() {

        assertFalse(maze.move(Direction.EAST), "move(EAST) should fail when blocked");

        assertFalse(maze.move(Direction.NORTH), "move(NORTH) should fail into a wall");
    }

    @Test
    void moveSuccess() {
        Door east = r00.getDoor(Direction.EAST);
        east.open();
        assertTrue(maze.move(Direction.EAST), "move(EAST) should succeed after open()");
        assertSame(r01, maze.getCurrentRoom(), "getCurrentRoom()");
    }

    @Test
    void isAtExitAtExit() {
        assertFalse(maze.isAtExit(), "Start is not exit");


        r00.getDoor(Direction.EAST).open();
        maze.move(Direction.EAST);

        maze.getCurrentRoom().getDoor(Direction.SOUTH).open();
        maze.move(Direction.SOUTH);

        assertTrue(maze.isAtExit(), "Should be at exit after valid path");
    }

    @Test
    void testReset() {

        r00.getDoor(Direction.EAST).open();
        maze.move(Direction.EAST);
        assertTrue(maze.getCurrentRoom().isVisited());

        maze.reset();
        assertFalse(r01.isVisited(), "Visited flags should be cleared");
        assertSame(r00, maze.getCurrentRoom(), "reset should return to startRoom");
    }
}
