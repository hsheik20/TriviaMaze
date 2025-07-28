package Test;
import Model.Room;
import Model.Door;
import Model.TrueFalseQuestion;
import Model.Trivia;
import Model.Hint;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DoorTest {
    private Room r1, r2;
    private Trivia question;
    private Door door;

    @BeforeEach
    void setUp() {
        r1 = new Room(0, 0);
        r2 = new Room(0, 1);
        question = new TrueFalseQuestion("Test?", true, new Hint("No hint"));
        door     = new Door(r1, r2, question);
    }

    @Test
    void isBlockedAtStart(){
        assertTrue(door.isBlocked(), "Doors start blocked");
    }
    @Test
    void testOpenBlocks(){
        door.open();
        assertFalse(door.isBlocked(), "open () should unblock the door?");
    }
    @Test
    void testGetNextDoor(){
        Room next = door.getNextRoom(r1);
        assertSame(r2, next);
        assertSame(r1, door.getNextRoom(r2));
    }
    @Test
    void testGetNextRoomInvalid() {
        Room other = new Room(5,5);
        assertThrows(IllegalArgumentException.class,
                () -> door.getNextRoom(other),
                "Should throw if room not part of this door");
    }

    @Test
    void testGetQuestion(){
        assertSame(question, door.getQuestion());
    }

}
