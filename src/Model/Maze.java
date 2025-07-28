package Model;

public class Maze {
    private final Room[][] grid;
    private final int rows, cols;
    private final Room startRoom,exitRoom;
    private Room currentPosition;

    public Maze(int rows, int cols) {
        validateMazeDimensions(rows, cols);
        this.rows = rows;
        this.cols = cols;
        grid = new Room[rows][cols];
        createRooms();
        connectDoors();
        startRoom = grid[0][0];
        exitRoom =  grid[rows-1][cols-1];

        currentPosition = startRoom;
        startRoom.markVisited();

        }

    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public Room getRoom(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid room coordinates: (" + row + ", " + col + ")");
            }
        return grid[row][col];
        }


    public boolean move(Direction direction) {
        boolean validmove = false;
        Door door = currentPosition.getDoor(direction);
        if (door != null && !door.isBlocked()) {
            Room next = door.getNextRoom(currentPosition);
            currentPosition = next;
            currentPosition.markVisited();
            validmove = true;
        }
        return validmove;

    }
    public Room getCurrentRoom() {
        return currentPosition;
    }
    public boolean isAtExit() {
        return currentPosition == exitRoom;
    }

    public void reset() {
        currentPosition = startRoom;
        for (Room[] row : grid) {
            for (Room room : row) {
                room.clearVisited();
            }
        }
        startRoom.markVisited();
    }
    private void validateMazeDimensions(int r, int c) {
        if (r < 1 || c < 1) {
            throw new IllegalArgumentException(" rows/cols must be greater than 1");
        }
    }

    private void createRooms() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Room(r, c);
            }
        }

    }
    private void connectDoors() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Room room = grid[r][c];
                if (r > 0) {
                    Door nsDoor = new Door(room, grid[r - 1][c], null);
                    room.setDoor(Direction.NORTH, nsDoor);
                    grid[r - 1][c].setDoor(Direction.SOUTH, nsDoor);
                }
                if (c > 0) {
                    Door weDoor = new Door(room, grid[r][c - 1], null);
                    room.setDoor(Direction.WEST, weDoor);
                    grid[r][c - 1].setDoor(Direction.EAST, weDoor);
                }
            }
        }

    }






}
