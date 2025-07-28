package Model;

public class Door {
    private final Room roomA;
    private final Room roomB;
    private final Trivia question;
    private boolean locked = true;

    public Door(Room roomA, Room roomB, Trivia question) {
        this.roomA = roomA;
        this.roomB = roomB;
        this.question = question;
    }
    public void open() {
        this.locked = false;
    }
    public boolean isBlocked() {
        return locked;
    }
    public Room getNextRoom(Room currentRoom) {
        if (currentRoom == roomA) {
            return roomB;
        }
        if (currentRoom == roomB) {
            return roomA;
        }
        throw new IllegalArgumentException("Room " + currentRoom + " is not connected by this door");
    }
    public Trivia getQuestion() {
        return question;
    }

}
