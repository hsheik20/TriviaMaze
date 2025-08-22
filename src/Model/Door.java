package Model;

import java.io.Serial;
import java.io.Serializable;

import java.util.Objects;

/**
 * Represents a door connecting two {@link Room} objects within the maze.
 * Each door is associated with a {@link Question} that must be answered
 * correctly to open it. A door can be in a locked (blocked) state and can be
 * permanently blocked after an incorrect answer.
 *
 * @author Husein & Chan
 */
public class Door implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** The first {@link Room} connected by this door. */
    private final Room myRoomA;

    /** The second {@link Room} connected by this door. */
    private final Room myRoomB;

    /** The {@link Question} that must be answered to open this door. */
    private final Question myQuestion;

    /**
     * The locked state of the door. A door starts as locked and can be
     * permanently blocked after an incorrect answer.
     */
    private boolean myLocked = false;


    /**
     * Constructs a {@code Door} connecting two specified rooms and a question.
     * The door is initially in an unlocked state, but is considered impassable
     * until a question is answered to "open" it.
     *
     * @param theRoomA       One side of the door.
     * @param theRoomB       The other side of the door.
     * @param theQuestion    The {@link Question} that will unlock the door.
     * @throws IllegalArgumentException if either room is null.
     */
    public Door(final Room theRoomA, Room theRoomB, Question theQuestion) {
        if (theRoomA == null || theRoomB == null) {
            throw new IllegalArgumentException("Rooms cannot be null.");
        }
        this.myRoomA = theRoomA;
        this.myRoomB = theRoomB;
        this.myQuestion = theQuestion;
    }

    /**
     * Unlocks the door, allowing passage.
     */
    public void open() {
        this.myLocked = false;
    }

    /**
     * Permanently blocks the door, preventing any future passage.
     * This is typically called after a player fails to answer the question correctly.
     */
    public void block() {
        this.myLocked = true;
    }

    /**
     * Checks if the door is currently blocked.
     *
     * @return {@code true} if the door is blocked (unanswered or incorrect answer given);
     * {@code false} otherwise.
     */
    public boolean isBlocked() {
        return myLocked;
    }

    /**
     * Returns the room on the opposite side of the door from the given room.
     *
     * @param theCurrentRoom The room the user is currently in.
     * @return The opposite room connected by this door.
     * @throws IllegalArgumentException if the provided room is not one of the rooms
     * connected by this door.
     */
    public Room getNextRoom(Room theCurrentRoom) {
        Objects.requireNonNull(theCurrentRoom, "The current room cannot be null");
        if (theCurrentRoom.equals(myRoomA)) {
            return myRoomB;
        }
        if (theCurrentRoom.equals(myRoomB)) {
            return myRoomA;
        }
        throw new IllegalArgumentException("Room " + theCurrentRoom + " is not connected by this door");
    }

    /**
     * Returns the trivia question associated with this door.
     *
     * @return The {@link Question} connected to this door.
     */
    public Question getQuestion() {
        return myQuestion;
    }


}