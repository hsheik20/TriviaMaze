package Model;

import java.util.Set;

/**
 * A utility class responsible for all console-based UI output for the game.
 * This class acts as a "pure view" by displaying information from the model
 * without containing any game logic. It provides methods for rendering the
 * title, menus, heads-up display (HUD), maze grid, and game-related messages.
 */
public final class Display {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Display() {}

    // ---- Title & Menus ----

    /**
     * Prints the main title screen to the console, featuring ASCII art and a
     * retro-style banner.
     */
    public static void title() {
        // Small GameBoy sprites (credit: hjw)
        final String[] GB = {
                " |,--.| ",
                " ||__|| ",
                " |+  o| ",
                " |,'o | ",
                " `----' "
        };

        final int totalWidth = 72;
        final int gbW = GB[0].length();       // width of one sprite
        final String titleText = "TRIVIA  MAZE";
        final String border = "=".repeat(totalWidth);

        // Top border
        System.out.println(border);

        // 5 rows that flank the centered title
        for (int row = 0; row < GB.length; row++) {
            boolean isTitleRow = (row == 2); // put the text on the middle row

            String middle;
            if (isTitleRow) {
                int innerWidth = totalWidth - (gbW * 2);
                int pad = Math.max(0, (innerWidth - titleText.length()) / 2);
                middle = " ".repeat(pad) + titleText + " ".repeat(Math.max(0, innerWidth - pad - titleText.length()));
            } else {
                middle = " ".repeat(totalWidth - (gbW * 2));
            }

            System.out.println(GB[row] + middle + GB[row]);
        }

        // Bottom border
        System.out.println(border);
        System.out.println(); // spacer

        // Big retro banner under the sprites
        System.out.println("""
     ______   ______     __     __   __   __     ______        __    __     ______     ______     ______    
    /\\__  _\\ /\\  == \\   /\\ \\   /\\ \\ / /  /\\ \\   /\\  __ \\      /\\ "-./  \\   /\\  __ \\   /\\___  \\   /\\  ___\\   
    \\/_/\\ \\/ \\ \\  __<   \\ \\ \\  \\ \\ \\'/   \\ \\ \\  \\ \\  __ \\     \\ \\ \\-./\\ \\  \\ \\  __ \\  \\/_/  /__  \\ \\  __\\   
       \\ \\_\\  \\ \\_\\ \\_\\  \\ \\_\\  \\ \\__|    \\ \\_\\  \\ \\_\\ \\_\\     \\ \\_\\ \\ \\_\\  \\ \\_\\ \\_\\   /\\_____\\  \\ \\_____\\ 
        \\/_/   \\/_/ /_/   \\/_/   \\/_/      \\/_/   \\/_/\\/_/      \\/_/  \\/_/   \\/_/\\/_/   \\/_____/   \\/_____/ 
    """);


    }

    /**
     * Prints the main menu options to the console.
     */
    public static void mainMenu() {

        System.out.println("• New Game");
        System.out.println("• Load Game");
        System.out.println("• Instructions");
        System.out.println("• About");
        System.out.println("• Quit");
        System.out.print("Choose: ");
    }

    /**
     * Prints the game instructions to the console.
     */
    public static void instructions() {

        System.out.println("""
            How to Play:
            • Move with n/s/e/w. Doors ask a question.
            • Correct opens; wrong may block or reduce attempts.
            • Hints/Skip depend on difficulty.
            • Reach the exit (bottom-right) while a path still exists.
            """);
    }

    /**
     * Prints information about the game's creators.
     */
    public static void about() {

        System.out.println("This is a retro-style Trivia Maze created by Husein & Sam.");
    }

    // ---- HUD & Map ----

    /**
     * Prints the Heads-Up Display (HUD) to the console, showing the player's
     * current position, the game state, and the number of hints remaining.
     *
     * @param game The {@link Game} object to get player information from.
     * @param gsm  The {@link GameStateManager} to get the current state from.
     */
    public static void hud(final Game game, final GameStateManager gsm) {
        final int hints = game.getHintsLeft();
        System.out.printf("Pos=(%d,%d)  State=%s  Hints=%s%n",
                game.getPlayer().getX(),
                game.getPlayer().getY(),
                gsm.get(),
                (hints == Integer.MAX_VALUE ? "∞" : hints));
    }

    /**
     * Prints a simplified text representation of the maze grid.
     * It marks the player's position, the start, the finish, and standard rooms.
     *
     * @param maze   The {@link Maze} to be displayed.
     * @param player The {@link Player} whose position is to be marked.
     */
    public static void grid(final Maze maze, final Player player) {
        final int rows = maze.getRows();
        final int cols = maze.getCols();

        for (int r = 0; r < rows; r++) {
            final StringBuilder line = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                final boolean isPlayer = (player.getX() == r && player.getY() == c);
                final boolean isStart  = (r == 0 && c == 0);
                final boolean isExit   = (r == rows - 1 && c == cols - 1);

                if (isPlayer)      line.append("[PLYR]");
                else if (isExit)   line.append("[FNSH]");
                else if (isStart)  line.append("[STRT]");
                else               line.append("[ROOM]");
            }
            System.out.println(line);
        }
        System.out.println();
    }

    /**
     * Prints a textual representation of the available movement directions
     * from the player's current room.
     *
     * @param maze The {@link Maze} to determine available doors.
     */
    public static void moveGuide(final Maze maze) {
        final Room cur = maze.getCurrentRoom();
        final Set<Direction> avail = cur.getAvailableDirections();

        System.out.print(avail.contains(Direction.NORTH) ? " MOVE NORTH " : "  BLOCKED  ");
        System.out.println();
        System.out.print(avail.contains(Direction.WEST)  ? " MOVE WEST "  : "  BLOCKED  ");
        System.out.print("   Player   ");
        System.out.println(avail.contains(Direction.EAST)  ? " MOVE EAST "  : "  BLOCKED  ");
        System.out.println(avail.contains(Direction.SOUTH) ? " MOVE SOUTH " : "  BLOCKED  ");
        System.out.println();
    }

    // ---- Question & Results ----

    /**
     * Displays a question to the user, including the prompt, remaining attempts,
     * and optional commands for hints and skipping.
     *
     * @param q            The {@link Question} to be displayed.
     * @param attemptsLeft The number of remaining attempts for this question.
     * @param canHint      {@code true} if hints are enabled, {@code false} otherwise.
     * @param canSkip      {@code true} if skipping is allowed, {@code false} otherwise.
     */
    public static void showQuestion(final Question q, final int attemptsLeft,
                                    final boolean canHint, final boolean canSkip) {

        System.out.println("--------------------------------------------------");
        System.out.println("Question: " + q.getPrompt());
        System.out.println("(Attempts left: " + (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft) + ")");
        System.out.print("Your answer"
                + (canHint ? " or 'h' for hint" : "")
                + (canSkip ? " or 'k' to skip" : "")
                + ": ");
    }

    /**
     * Displays a hint for the current question and shows the number of hints remaining.
     *
     * @param hint      The hint string to display.
     * @param hintsLeft The number of hints remaining.
     */
    public static void showHint(final String hint, final int hintsLeft) {
        System.out.println("Hint: " + hint + "  (Hints remaining: "
                + (hintsLeft == Integer.MAX_VALUE ? "∞" : hintsLeft) + ")");
    }

    /** Displays a "Correct!" message and plays a sound effect. */
    public static void correct()     { Sounds.CORRECT.play();   System.out.println("✅ Correct!"); }

    /** Displays a "Wrong." message and plays a sound effect. */
    public static void wrong()       { Sounds.INCORRECT.play(); System.out.println("❌ Wrong."); }

    /** Displays a "Blocked" message. */
    public static void blocked()     { System.out.println("That path is blocked now."); }

    /** Displays a "You reached the exit!" message and plays a sound effect. */
    public static void reachedExit() { Sounds.WIN.play();       System.out.println("🎉 You reached the exit!"); }

    /** Displays a "[GAME OVER]" message and plays a sound effect. */
    public static void gameOver()    { Sounds.LOSE.play();      System.out.println("[GAME OVER]"); }

    // ---- Small helpers ----

    /** Displays a "[PAUSED]" message. */
    public static void paused()         { System.out.println("[PAUSED]"); }

    /** Displays a message for when the user quits the game early. */
    public static void quittingEarly()  { System.out.println("You chose to quit early. Thanks for playing!"); }

    /** Displays a message for an unrecognized user command. */
    public static void unknownCommand() { System.out.println("Unknown command. Type 'h' for help."); }
}