package Test;
import Model.*;
import Model.Question;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A test class for the {@link Door} model.
 * It verifies the correct behavior of the Door class, including its initial state,
 * opening functionality, and its ability to manage the connection between two rooms.
 *
 * @author Husein
 */
public class DoorTest {
    private Room r1, r2;
    private Question question;
    private Door door;

    /**
     * Sets up the test environment before each test method.
     * Initializes two new {@link Room} objects and a {@link Door} connecting them.
     */
    @BeforeEach
    void setUp() {
        r1 = new Room(0, 0);
        r2 = new Room(0, 1);
        // A placeholder for a question. This will be null initially, but the test can still run.
        // If a specific question type is required for tests, it should be uncommented.
        // question = new TrueFalseQuestion("Test?", true, new Hint("No hint"));
        door = new Door(r1, r2, question);
    }

    /**
     * Tests that a new door is in a blocked state by default.
     */
    @Test
    void isBlockedAtStart(){
        assertTrue(door.isBlocked(), "Doors start blocked");
    }

    /**
     * Tests that the {@code open()} method successfully changes the door's state
     * from blocked to unblocked.
     */
    @Test
    void testOpenBlocks(){
        door.open();
        assertFalse(door.isBlocked(), "open() should unblock the door.");
    }

    /**
     * Tests the {@code getNextRoom()} method to ensure it returns the correct
     * adjacent room based on the provided current room.
     */
    @Test
    void testGetNextDoor(){
        Room next = door.getNextRoom(r1);
        assertSame(r2, next);
        assertSame(r1, door.getNextRoom(r2));
    }

    /**
     * Tests that the {@code getNextRoom()} method throws an {@link IllegalArgumentException}
     * when an invalid room (one not connected by this door) is passed as an argument.
     */
    @Test
    void testGetNextRoomInvalid() {
        Room other = new Room(5,5);
        assertThrows(IllegalArgumentException.class,
                () -> door.getNextRoom(other),
                "Should throw if room not part of this door");
    }

    /**
     * Tests that the {@code getQuestion()} method returns the same question object
     * that was assigned to the door during its creation.
     */
    @Test
    void testGetQuestion(){
        assertSame(question, door.getQuestion());
    }
}