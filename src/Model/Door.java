package Model;

import java.io.Serial;
import java.io.Serializable;

import java.util.Objects;

/**
 * This represents a door connecting two rooms.
 */
public class Door implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Room myRoomA;
    private final Room myRoomB;
    private final Question myQuestion;
    private boolean myLocked = false;


    /**
     * Construct door connecting two given rooms.
     *
     * @param theRoomA one side of door
     * @param theRoomB other side of door
     * @param theQuestion the myQuestion that will unlock door
     * @throws IllegalArgumentException if myRoomA or myRoomB is null
     */
    public Door(final Room theRoomA, Room theRoomB, Question theQuestion) {
        this.myRoomA = theRoomA;
        this.myRoomB = theRoomB;
        this.myQuestion = theQuestion;
    }

    /**
     * This unlocks door.
     */
    public void open() {
        this.myLocked = false;
    }

    /**
     * Blocks the door permanently after an incorrect answer.
     */
    public void block() {
        this.myLocked = true;
    }

    /**
     *
     * This checks if door is still myLocked
     * @return true if door still myLocked( unanswered or incorrect answer given)
     */
    public boolean isBlocked() {
        return myLocked;
    }

    /**
     * This returns the other side of room the door connects to
     *
     * @param theCurrentRoom the room user is currently in
     * @return the opposite room on other side
     * @throws  IllegalArgumentException if room is neither myRoomA or myRoomB
     */
    public Room getNextRoom(Room theCurrentRoom) {
        Objects.requireNonNull(theCurrentRoom, "The current room cannot be null");
        if (theCurrentRoom == myRoomA) {
            return myRoomB;
        }
        if (theCurrentRoom == myRoomB) {
            return myRoomA;
        }
        throw new IllegalArgumentException("Room " + theCurrentRoom + " is not connected by this door");
    }

    /**
     * This returns trivia myQuestion associated with this door
     * @return trivia myQuestion connected to door
     */
    public Question getQuestion() {
        return myQuestion;
    }

}