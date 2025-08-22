package Test;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the {@link Maze} model.
 * This suite verifies the core functionality of a maze, including its
 * construction, room and door connectivity, movement mechanics, and pathfinding.
 * It uses specialized test doubles to ensure isolated and predictable behavior.
 *
 * @author Husein
 */
class MazeTest {

    // ---- Inline test doubles (live only in this file) ----

    /**
     * An inline test double for a {@link Question}.
     * This dummy implementation is always correct, allowing tests to
     * proceed without needing a real question or an external database.
     */
    private static class DummyQuestion extends Question implements java.io.Serializable {
        DummyQuestion(String prompt) {
            super(prompt, null);
        }

        @Override
        public boolean isCorrect(String a) {
            return true;
        }

        @Override
        public String getCorrectAnswer() {
            return "any";
        }
    }

    /**
     * An inline test double for a {@link questionFactory}.
     * This stub provides a deterministic queue of {@link Question} objects,
     * ensuring that maze creation is predictable and independent of a
     * live SQLite database connection.
     */
    private static class StubFactory extends questionFactory implements java.io.Serializable {
        private final Queue<Question> q = new ArrayDeque<>();

        StubFactory() {
            super("jdbc:sqlite::memory:"); // ctor arg unused by this stub
        }

        /**
         * Adds a question to the internal queue to be served.
         *
         * @param x The question to add.
         */
        void add(Question x) {
            q.add(x);
        }

        /**
         * Returns the next question from the queue.
         *
         * @return The next available question, or null if the queue is empty.
         */
        @Override
        public Question getNextAvailableQuestion() {
            return q.poll();
        }
    }

    // ---- Test fixtures ----

    private Maze maze;
    private Room r00, r01, r10, r11;

    /**
     * Helper method to create a {@link StubFactory} pre-populated with
     * a specified number of {@link DummyQuestion} instances.
     *
     * @param count The number of questions to add to the factory.
     * @return A pre-populated {@link StubFactory}.
     */
    private StubFactory filledFactory(int count) {
        var f = new StubFactory();
        // A 2x2 grid with north/west linking creates up to ~8 doors; 10 is a safe number
        for (int i = 0; i < count; i++) f.add(new DummyQuestion("Q" + i));
        return f;
    }

    /**
     * Sets up a small, 2x2 maze for testing before each test method runs.
     * This provides a consistent and predictable state for all subsequent tests.
     */
    @BeforeEach
    void setUp() {
        maze = new Maze(2, 2, filledFactory(10));
        r00 = maze.getRoom(0, 0);
        r01 = maze.getRoom(0, 1);
        r10 = maze.getRoom(1, 0);
        r11 = maze.getRoom(1, 1);
    }

    /**
     * Tests the maze's dimensions and the ability to access specific rooms
     * using coordinates. It also verifies that invalid coordinates
     * throw an {@link IndexOutOfBoundsException}.
     */
    @Test
    void dimensionsAndRoomAccess() {
        assertEquals(2, maze.getRows());
        assertEquals(2, maze.getCols());
        assertSame(r00, maze.getRoom(0, 0));
        assertSame(r11, maze.getRoom(1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getRoom(0, 2));
    }

    /**
     * Tests the basic topological connections of the maze, ensuring that
     * the start and exit rooms are correctly connected to their neighbors.
     */
    @Test
    void connectDoors_basicTopology() {
        Door east = r00.getDoor(Direction.EAST);
        Door south = r00.getDoor(Direction.SOUTH);
        assertTrue(east != null || south != null, "Start should have at least one outward door");

        if (east != null) assertSame(r01, east.getNextRoom(r00));
        if (south != null) assertSame(r10, south.getNextRoom(r00));

        Door west = r11.getDoor(Direction.WEST);
        Door north = r11.getDoor(Direction.NORTH);
        assertTrue(west != null || north != null, "Exit should have at least one inward door");
        if (west != null) assertSame(r10, west.getNextRoom(r11));
        if (north != null) assertSame(r01, north.getNextRoom(r11));
    }

    /**
     * Tests the movement helper methods: {@code canMove}, {@code step}, and the
     * convenience wrapper {@code move}. It verifies that movement is possible,
     * changes the current room, and marks the new room as visited.
     */
    @Test
    void helpers_canMove_step_moveWrapper() {
        // Find a valid starting direction
        Direction d = maze.getDoor(Direction.EAST) != null ? Direction.EAST
                : maze.getDoor(Direction.SOUTH) != null ? Direction.SOUTH : null;
        assertNotNull(d, "There should be a move from start");

        // Test canMove and step
        assertTrue(maze.canMove(d));
        Room before = maze.getCurrentRoom();
        Room after = maze.step(d);
        assertNotEquals(before, after);
        assertTrue(after.isVisited());

        // Test the convenience wrapper move() and reset()
        maze.reset();
        assertTrue(maze.move(d)); // wrapper uses canMove + step
        assertNotEquals(before, maze.getCurrentRoom());
    }

    /**
     * Tests that a blocked door prevents movement. It verifies that
     * {@code canMove} returns false and {@code move} does not change the
     * current room's location.
     */
    @Test
    void blockedMoveFails() {
        // Find a valid door from the current room
        Direction d = maze.getDoor(Direction.EAST) != null ? Direction.EAST
                : maze.getDoor(Direction.SOUTH) != null ? Direction.SOUTH : null;
        assertNotNull(d);
        Door door = maze.getDoor(d);
        // Explicitly block the door
        door.block();

        // Verify that movement is now impossible
        assertFalse(maze.canMove(d));
        Room cur = maze.getCurrentRoom();
        assertFalse(maze.move(d));
        assertSame(cur, maze.getCurrentRoom());
    }

    /**
     * Tests the {@code isAtExit} and {@code reset} methods. It verifies that
     * the maze correctly detects when the player is at the exit and that the
     * {@code reset} method restores the maze to its initial state.
     */
    @Test
    void exitDetection_and_reset() {
        assertFalse(maze.isAtExit());

        // Move to the exit room (in a 2x2 maze, this will be r11)
        if (maze.getDoor(Direction.EAST) != null) maze.step(Direction.EAST);
        if (maze.getDoor(Direction.SOUTH) != null) maze.step(Direction.SOUTH);

        // One of these moves should have led to the exit
        assertTrue(maze.isAtExit() || maze.getCurrentRoom() == maze.getExitRoom());

        // Reset the maze and verify the state
        maze.reset();
        assertSame(r00, maze.getCurrentRoom());
        assertFalse(r01.isVisited()); // One of the rooms should no longer be visited
    }

    /**
     * Tests the {@code hasPathToExitFromCurrent} method, which uses a BFS
     * algorithm. It first verifies that a path exists, then blocks all doors
     * to confirm that the method correctly identifies the absence of a path.
     */
    @Test
    void bfs_hasPathToExitFromCurrent_trueThenFalse() {
        assertTrue(maze.hasPathToExitFromCurrent());

        // Block all doors to create a state with no path to the exit
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