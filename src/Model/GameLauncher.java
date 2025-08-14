package Model;

import View.Display;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * Console launcher/controller for Trivia Maze.
 * Shows a start menu, builds a game from a chosen difficulty,
 * renders HUD + grid, and handles question prompts via property changes.
 */
public class GameLauncher implements PropertyChangeListener {

    // --- runtime state (re-created when starting a new game) ---
    private Game game;
    private GameStateManager gsm;

    // simple scanner for all input
    private final Scanner in = new Scanner(System.in);

    // optional: a single-file quick-load. keep simple for console prototype.
    private static final String SAVE_FILE = "trivia_save.dat";



    // ===== entry point =====
    public static void main(String[] args) {
        new GameLauncher().showStartMenu();
    }

    // ===== start menu =====
    private void showStartMenu() {
        while (true) {
            Display.title();
            Display.mainMenu();

            String choice = in.nextLine().trim().toLowerCase(Locale.ROOT);
            switch (choice) {
                case "new game", "new", "n" -> startNewGameFlow();
                case "load game", "load", "l" -> loadGameFlow();
                case "instructions", "i" -> {
                    Display.instructions();
                    waitForKey();
                }
                case "about", "a" -> {
                    Display.about();
                    waitForKey();
                }
                case "quit", "q", "exit" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> {
                    System.out.println("Please type: New Game, Load Game, Instructions, About, or Quit.");
                    waitForKey();
                }
            }
        }
    }

    private void waitForKey() {
        System.out.print("Press ENTER to continue... ");
        in.nextLine();
        System.out.println();
    }

    // ===== new game flow =====
    private void startNewGameFlow() {
        // pick difficulty
        DifficultySettings settings = promptForDifficulty();

        // build maze based on difficulty size; use your SQLite factory
        questionFactory qf = new questionFactory("jdbc:sqlite:lib/trivia.db");
        Maze maze = new Maze(settings.getMazeHeight(), settings.getMazeWidth(), qf);

        // core actors
        Player player = new Player();
        gsm = new GameStateManager();
        game = new Game(maze, player, gsm, settings);

        // subscribe to events
        game.addListener(this);
        gsm.addListener(this);

        // play loop
        playLoop();
    }

    private DifficultySettings promptForDifficulty() {
        while (true) {
            System.out.println("Choose difficulty: (e)asy, (n)ormal, (h)ard");
            System.out.print("> ");
            String c = in.nextLine().trim().toLowerCase(Locale.ROOT);
            switch (c) {
                case "e", "easy" -> {
                    System.out.println("Starting on: Easy");
                    return DifficultyPresets.easy();
                }
                case "h", "hard" -> {
                    System.out.println("Starting on: Hard");
                    return DifficultyPresets.hard();
                }
                case "n", "normal", "" -> {
                    System.out.println("Starting on: Normal");
                    return DifficultyPresets.normal();
                }
                default -> System.out.println("Please type e / n / h.");
            }
        }
    }

    // ===== optional quick load =====
    private void loadGameFlow() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Game loaded = (Game) ois.readObject();
            // re-hook listeners (PropertyChangeSupport is rebuilt by readResolve)
            this.game = loaded;
            this.gsm = loaded.getStateManager();
            game.addListener(this);
            gsm.addListener(this);
            System.out.println("Loaded saved game.");
            playLoop();
        } catch (FileNotFoundException e) {
            System.out.println("No saved game found yet.");
            waitForKey();
        } catch (Exception e) {
            System.out.println("Could not load save (this console prototype saves only in-game with 'save').");
            waitForKey();
        }
    }

    // ===== main in-game loop =====
    private void playLoop() {
        // basic controls reminder
        System.out.println("Controls: n/s/e/w move, p pause, r resume, q quit, h help, save quick-save");
        while (gsm.get() != GameState.GAME_OVER) {
            // HUD + map + move guide
            Display.hud(game, gsm);
            Display.grid(game.getMaze(), game.getPlayer());
            Display.moveGuide(game.getMaze());

            if (gsm.get() == GameState.PAUSED) {
                System.out.print("[PAUSED] r=resume, q=quit: ");
                String cmd = in.nextLine().trim().toLowerCase(Locale.ROOT);
                if (cmd.equals("r")) {
                    gsm.resume();
                } else if (cmd.equals("q")) {
                    Display.quittingEarly();
                    break; // back to main menu
                } else {
                    System.out.println("Invalid command.");
                }
                continue;
            }

            System.out.print("Command (n/s/e/w, p=pause, r=resume, q=quit, h=help, save): ");
            String cmd = in.nextLine().trim().toLowerCase(Locale.ROOT);
            switch (cmd) {
                case "n" -> game.attemptMove(Direction.NORTH);
                case "s" -> game.attemptMove(Direction.SOUTH);
                case "e" -> game.attemptMove(Direction.EAST);
                case "w" -> game.attemptMove(Direction.WEST);
                case "p" -> gsm.pause();
                case "r" -> gsm.resume();
                case "h" -> {
                    Display.instructions();
                    waitForKey();
                }
                case "save" -> quickSave();
                case "q" -> {
                    Display.quittingEarly();
                    return; // back to main menu
                }
                default -> Display.unknownCommand();
            }
        }
        Display.gameOver();
        waitForKey();
    }

    private void quickSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(game);
            System.out.println("Saved.");
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    // ===== event handling from Game / GSM =====
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "state" -> {
                GameState s = (GameState) evt.getNewValue();
                if (s == GameState.PAUSED) {
                    Display.paused();
                } else if (s == GameState.PLAYING) {
                    // starting (or resuming) the game: start fresh
                } else if (s == GameState.GAME_OVER) {
                    Display.gameOver();
                }
            }

            case "askQuestion" -> {
                QuestionRequest req = (QuestionRequest) evt.getNewValue();
                Question q = req.question();
                Door door = req.door();

                int attempts = game.getAttemptsLeft(door);
                boolean canHint = game.canUseHint(q);
                boolean canSkip = game.canSkip();

                while (true) {
                    Display.showQuestion(q, attempts, canHint, canSkip);
                    String ans = in.nextLine().trim();

                    if (canHint && ans.equalsIgnoreCase("h")) {
                        String hint = game.useHint(q);
                        if (hint == null) {
                            System.out.println("(No hint available.)");
                        } else {
                            Display.showHint(hint, game.getHintsLeft());
                        }
                        attempts = game.getAttemptsLeft(door);
                        // loop again to answer after hint
                        continue;
                    }

                    if (canSkip && ans.equalsIgnoreCase("k")) {
                        game.skipQuestion(door);
                        Display.blocked();
                        break;
                    }

                    boolean correct = q.isCorrect(ans);
                    game.handleAnswer(door, correct);
                    if (correct) Display.correct(); else Display.wrong();
                    break;
                }
            }
            case "playerMoved" -> {
                Room newRoom = (Room) evt.getNewValue();
                System.out.printf("Moved to (%d,%d).%n", newRoom.getRow(), newRoom.getCol());
                if (game.getMaze().isAtExit()) {
                    Display.reachedExit();
                }
            }
            case "doorBlocked" -> Display.blocked();
        }
    }
}
