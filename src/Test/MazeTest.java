package Test;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    // ---- Inline test doubles (live only in this file) ----

    /** Always-correct dummy question so Maze can build doors without touching a DB. */
    private static class DummyQuestion extends Question implements java.io.Serializable {
        DummyQuestion(String prompt) { super(prompt, null); }
        @Override public boolean isCorrect(String a) { return true; }
        @Override public String getCorrectAnswer() { return "any"; }
    }

    /** Stub factory that feeds deterministic questions and never hits SQLite. */
    private static class StubFactory extends questionFactory implements java.io.Serializable {
        private final Queue<Question> q = new ArrayDeque<>();
        StubFactory() { super("jdbc:sqlite::memory:"); } // ctor arg unused by this stub
        void add(Question x) { q.add(x); }
        @Override public Question getNextAvailableQuestion() { return q.poll(); }
    }

    // ---- Test fixtures ----

    private Maze maze;
    private Room r00, r01, r10, r11;

    private StubFactory filledFactory(int count) {
        var f = new StubFactory();
        // 2x2 grid with north/west linking creates up to ~8 doors; 10 is safe
        for (int i = 0; i < count; i++) f.add(new DummyQuestion("Q"+i));
        return f;
    }

    @BeforeEach
    void setUp() {
        maze = new Maze(2, 2, filledFactory(10));
        r00  = maze.getRoom(0, 0);
        r01  = maze.getRoom(0, 1);
        r10  = maze.getRoom(1, 0);
        r11  = maze.getRoom(1, 1);
    }

    @Test
    void dimensionsAndRoomAccess() {
        assertEquals(2, maze.getRows());
        assertEquals(2, maze.getCols());
        assertSame(r00, maze.getRoom(0, 0));
        assertSame(r11, maze.getRoom(1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(0, 2));
    }

    @Test
    void connectDoors_basicTopology() {
        Door east  = r00.getDoor(Direction.EAST);
        Door south = r00.getDoor(Direction.SOUTH);
        assertTrue(east != null || south != null, "Start should have at least one outward door");

        if (east != null)  assertSame(r01, east.getNextRoom(r00));
        if (south != null) assertSame(r10, south.getNextRoom(r00));

        Door west  = r11.getDoor(Direction.WEST);
        Door north = r11.getDoor(Direction.NORTH);
        assertTrue(west != null || north != null, "Exit should have at least one inward door");
        if (west != null)  assertSame(r10, west.getNextRoom(r11));
        if (north != null) assertSame(r01, north.getNextRoom(r11));
    }

    @Test
    void helpers_canMove_step_moveWrapper() {
        Direction d = maze.getDoor(Direction.EAST) != null ? Direction.EAST
                : maze.getDoor(Direction.SOUTH) != null ? Direction.SOUTH : null;
        assertNotNull(d, "There should be a move from start");

        assertTrue(maze.canMove(d));
        Room before = maze.getCurrentRoom();
        Room after  = maze.step(d);
        assertNotEquals(before, after);
        assertTrue(after.isVisited());

        maze.reset();
        assertTrue(maze.move(d)); // wrapper uses canMove + step
        assertNotEquals(before, maze.getCurrentRoom());
    }

    @Test
    void blockedMoveFails() {
        Direction d = maze.getDoor(Direction.EAST) != null ? Direction.EAST
                : maze.getDoor(Direction.SOUTH) != null ? Direction.SOUTH : null;
        assertNotNull(d);
        Door door = maze.getDoor(d);
        door.block();

        assertFalse(maze.canMove(d));
        Room cur = maze.getCurrentRoom();
        assertFalse(maze.move(d));
        assertSame(cur, maze.getCurrentRoom());
    }

    @Test
    void exitDetection_and_reset() {
        assertFalse(maze.isAtExit());

        if (maze.getDoor(Direction.EAST) != null)  maze.step(Direction.EAST);
        if (maze.getDoor(Direction.SOUTH) != null) maze.step(Direction.SOUTH);

        assertTrue(maze.isAtExit() || maze.getCurrentRoom() == maze.getExitRoom());

        maze.reset();
        assertSame(r00, maze.getCurrentRoom());
        assertFalse(r01.isVisited());
    }

    @Test
    void bfs_hasPathToExitFromCurrent_trueThenFalse() {
        assertTrue(maze.hasPathToExitFromCurrent());

        // Block all doors everywhere → no path
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Room room = maze.getRoom(r, c);
                for (Direction dir : room.getAvailableDirections()) {
                    Door d = room.getDoor(dir);
                    if (d != null) d.block();
                }
            }
        }
        assertFalse(maze.hasPathToExitFromCurrent());
    }
}
