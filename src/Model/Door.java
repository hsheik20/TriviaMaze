package Model;

/**
 * This represents a door connecting two rooms.
 */
public class Door {
    private final Room roomA;
    private final Room roomB;
    private final Question question;
    private boolean locked = true;

    /**
     * Construct door connecting two given rooms.
     *
     * @param roomA one side of door
     * @param roomB other side of door
     * @param question the question that will unlock door
     * @throws IllegalArgumentException if roomA or roomB is null
     */
    public Door(Room roomA, Room roomB, Question question) {
        this.roomA = roomA;
        this.roomB = roomB;
        this.question = question;
    }

    /**
     * This unlocks door.
     */
    public void open() {
        this.locked = false;
    }

    /**
     *
     * This checks if door is still locked
     * @return true if door still locked( unanswered or incorrect answer given)
     */
    public boolean isBlocked() {
        return locked;
    }

    /**
     * This returns the other side of room the door connects to
     *
     * @param currentRoom the room user is currently in
     * @return the opposite room on other side
     * @throws  IllegalArgumentException if room is neither roomA or roomB
     */
    public Room getNextRoom(Room currentRoom) {
        if (currentRoom == roomA) {
            return roomB;
        }
        if (currentRoom == roomB) {
            return roomA;
        }
        throw new IllegalArgumentException("Room " + currentRoom + " is not connected by this door");
    }

    /**
     * This returns trivia question associated with this door
     * @return trivia question connected to door
     */
    public Question getQuestion() {
        return question;
    }

}
