package Model;

public final class MazeRenderer {
    private MazeRenderer() {}

    /** Draws the maze as rows of [ROOM]/[PLYR]/[STRT]/[FNSH]. */
    public static void printGrid(final Maze maze, final Player p) {
        for (int r = 0; r < maze.getRows(); r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < maze.getCols(); c++) {
                boolean isPlayer = (p.getX() == r && p.getY() == c);
                boolean isStart  = (r == 0 && c == 0);
                boolean isExit   = (r == maze.getRows() - 1 && c == maze.getCols() - 1);
                line.append(isPlayer ? "[PLYR]" : isExit ? "[FNSH]" : isStart ? "[STRT]" : "[ROOM]");
            }
            System.out.println(line);
        }
        System.out.println();
    }

    /** Shows which directions from the current room are open vs blocked. */
    public static void printMoveGuide(final Maze maze) {
        Room cur = maze.getCurrentRoom();
        Door north = cur.getDoor(Direction.NORTH);
        Door south = cur.getDoor(Direction.SOUTH);
        Door east  = cur.getDoor(Direction.EAST);
        Door west  = cur.getDoor(Direction.WEST);

        // Top hint line
        System.out.println(north == null ? "      " : (north.isBlocked() ? "   BLOCKED" : "     MOVE NORTH"));
        // Middle line with west/east and a "Player" label
        System.out.println((west == null ? "BLOCKED" : (west.isBlocked() ? "BLOCKED" : " MOVE WEST"))
                + "   Player   "
                + (east == null ? "DEAD DOOR" : (east.isBlocked() ? "DEAD DOOR" : " MOVE EAST")));
        // Bottom hint line
        System.out.println(south == null ? "      " : (south.isBlocked() ? "   BLOCKED" : "     MOVE SOUTH"));
        System.out.println();
    }
}
