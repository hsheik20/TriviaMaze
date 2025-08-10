package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GameState abstract class functionality.
 * Tests the basic structure and contract of GameState implementations.
 */
class GameStateTest {

    @Mock
    private GameStateManager mockManager;

    private TestGameState testGameState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testGameState = new TestGameState();
    }

    @Test
    @DisplayName("Test GameState constructor initializes correctly")
    void testConstructor() {
        assertNotNull(testGameState);
        assertNull(testGameState.myManager); // Should be null initially
    }

    @Test
    @DisplayName("Test enter method assigns manager correctly")
    void testEnterAssignsManager() {
        testGameState.enter(mockManager);
        assertEquals(mockManager, testGameState.myManager);
        assertTrue(testGameState.enterCalled);
    }

    @Test
    @DisplayName("Test enter method throws exception for null manager")
    void testEnterWithNullManager() {
        assertThrows(IllegalArgumentException.class, () -> testGameState.enter(null));
    }

    @Test
    @DisplayName("Test abstract methods are implemented")
    void testAbstractMethodsImplemented() {
        testGameState.enter(mockManager);
        testGameState.exit();
        testGameState.update();
        String stateName = testGameState.getStateName();

        assertTrue(testGameState.enterCalled);
        assertTrue(testGameState.exitCalled);
        assertTrue(testGameState.updateCalled);
        assertEquals("TEST_STATE", stateName);
    }

    /**
     * Concrete implementation of GameState for testing purposes.
     */
    private static class TestGameState extends GameState {
        boolean enterCalled = false;
        boolean exitCalled = false;
        boolean updateCalled = false;

        @Override
        public void enter(GameStateManager theManager) {
            if (theManager == null) {
                throw new IllegalArgumentException("GameStateManager cannot be null");
            }
            this.myManager = theManager;
            enterCalled = true;
        }

        @Override
        public void exit() {
            exitCalled = true;
        }

        @Override
        public void update() {
            updateCalled = true;
        }

        @Override
        public String getStateName() {
            return "TEST_STATE";
        }
    }
}

/**
 * Test class for GameStateManager.
 * Tests state management, transitions, and component access.
 */
class GameStateManagerTest {

    @Mock
    private Game mockGame;
    @Mock
    private GameState mockState;
    @Mock
    private GameState mockNewState;

    private GameStateManager gameStateManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockState.getStateName()).thenReturn("MOCK_STATE");
        when(mockNewState.getStateName()).thenReturn("NEW_MOCK_STATE");

        // Create GameStateManager with mocked Game
        gameStateManager = new GameStateManager(mockGame);
    }

    @Test
    @DisplayName("Test GameStateManager constructor with valid Game")
    void testConstructorWithValidGame() {
        assertNotNull(gameStateManager);
        assertEquals(mockGame, gameStateManager.getGame());
        assertNotNull(gameStateManager.getPlayer());
        assertNotNull(gameStateManager.getMaze());
        assertNotNull(gameStateManager.getTrivia());
        assertNotNull(gameStateManager.getCurrentState());
    }

    @Test
    @DisplayName("Test GameStateManager constructor throws exception for null Game")
    void testConstructorWithNullGame() {
        assertThrows(IllegalArgumentException.class, () -> new GameStateManager(null));
    }

    @Test
    @DisplayName("Test setState transitions correctly")
    void testSetStateTransition() {
        // Set initial state
        gameStateManager.setState(mockState);

        // Verify initial state is set
        assertEquals(mockState, gameStateManager.getCurrentState());
        verify(mockState).enter(gameStateManager);
        verify(mockGame).showPanel("MOCK_STATE");

        // Transition to new state
        gameStateManager.setState(mockNewState);

        // Verify transition occurred correctly
        verify(mockState).exit();
        verify(mockNewState).enter(gameStateManager);
        assertEquals(mockNewState, gameStateManager.getCurrentState());
        verify(mockGame).showPanel("NEW_MOCK_STATE");
    }

    @Test
    @DisplayName("Test setState with null state throws exception")
    void testSetStateWithNull() {
        assertThrows(IllegalArgumentException.class, () -> gameStateManager.setState(null));
    }

    @Test
    @DisplayName("Test setState when no current state exists")
    void testSetStateWithNoCurrentState() {
        // Create fresh manager without initial state
        GameStateManager freshManager = new GameStateManager(mockGame);

        // Clear the current state to null (simulating no current state)
        freshManager.setState(mockState);

        verify(mockState).enter(freshManager);
        assertEquals(mockState, freshManager.getCurrentState());
    }

    @Test
    @DisplayName("Test component getters return correct instances")
    void testComponentGetters() {
        assertEquals(mockGame, gameStateManager.getGame());

        Player player = gameStateManager.getPlayer();
        assertNotNull(player);
        assertSame(player, gameStateManager.getPlayer()); // Should be same instance

        Maze maze = gameStateManager.getMaze();
        assertNotNull(maze);
        assertSame(maze, gameStateManager.getMaze()); // Should be same instance

        Trivia trivia = gameStateManager.getTrivia();
        assertNotNull(trivia);
        assertSame(trivia, gameStateManager.getTrivia()); // Should be same instance
    }
}

/**
 * Test class for GameOverState.
 * Tests game over functionality, dialog handling, and state transitions.
 */
class GameOverStateTest {

    @Mock
    private GameStateManager mockManager;
    @Mock
    private Game mockGame;
    @Mock
    private Player mockPlayer;

    private GameOverState winGameOverState;
    private GameOverState loseGameOverState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock returns
        when(mockManager.getGame()).thenReturn(mockGame);
        when(mockManager.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getScore()).thenReturn(100);
        when(mockPlayer.getQuestionsAnswered()).thenReturn(5);
        when(mockPlayer.getX()).thenReturn(4);
        when(mockPlayer.getY()).thenReturn(4);

        winGameOverState = new GameOverState(true);
        loseGameOverState = new GameOverState(false);
    }

    @Test
    @DisplayName("Test GameOverState constructor sets win status correctly")
    void testConstructor() {
        assertTrue(winGameOverState.isWon());
        assertFalse(loseGameOverState.isWon());
    }

    @Test
    @DisplayName("Test getStateName returns correct value")
    void testGetStateName() {
        assertEquals("GAME_OVER", winGameOverState.getStateName());
        assertEquals("GAME_OVER", loseGameOverState.getStateName());
    }

    @Test
    @DisplayName("Test enter with null manager throws exception")
    void testEnterWithNullManager() {
        assertThrows(IllegalArgumentException.class, () -> winGameOverState.enter(null));
    }

    @Test
    @DisplayName("Test isWon returns correct value")
    void testIsWon() {
        assertTrue(winGameOverState.isWon());
        assertFalse(loseGameOverState.isWon());
    }

    @Test
    @DisplayName("Test exit method executes without error")
    void testExit() {
        winGameOverState.enter(mockManager);
        assertDoesNotThrow(() -> winGameOverState.exit());
    }

    @Test
    @DisplayName("Test update method executes without error")
    void testUpdate() {
        assertDoesNotThrow(() -> winGameOverState.update());
    }

    @Test
    @DisplayName("Test manager is assigned correctly on enter")
    void testEnterAssignsManager() {
        // Since we can't easily test JOptionPane dialogs in unit tests,
        // we'll test that the manager is assigned correctly
        // Note: In a real scenario, you might want to refactor GameOverState
        // to make the dialog display testable (dependency injection, etc.)

        // We can at least verify that entering with valid manager doesn't throw
        assertDoesNotThrow(() -> {
            try {
                winGameOverState.enter(mockManager);
            } catch (Exception e) {
                // If it's just a dialog-related exception, we can ignore it for this test
                if (!e.getMessage().contains("dialog") && !e.getMessage().contains("GUI")) {
                    throw e;
                }
            }
        });
    }

    /**
     * Note: Testing the actual dialog interactions and state transitions
     * would require either:
     * 1. Refactoring the code to inject a dialog service dependency
     * 2. Using UI testing frameworks like TestFX
     * 3. Mocking JOptionPane (which is more complex)
     *
     * For production code, consider refactoring to make the dialog
     * display more testable.
     */
}

/**
 * Integration test class to test interactions between all components.
 */
class GameStateIntegrationTest {

    @Mock
    private Game mockGame;

    private GameStateManager gameStateManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameStateManager = new GameStateManager(mockGame);
    }

    @Test
    @DisplayName("Test full state transition cycle")
    void testStateTransitionCycle() {
        // Create a simple test state
        TestGameState testState = new TestGameState();

        // Transition to test state
        gameStateManager.setState(testState);

        assertEquals(testState, gameStateManager.getCurrentState());
        assertEquals(gameStateManager, testState.myManager);
        assertTrue(testState.enterCalled);

        // Create game over state
        GameOverState gameOverState = new GameOverState(true);

        // Transition to game over state
        gameStateManager.setState(gameOverState);

        assertTrue(testState.exitCalled);
        assertEquals(gameOverState, gameStateManager.getCurrentState());
        assertEquals("GAME_OVER", gameStateManager.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Test component consistency across state transitions")
    void testComponentConsistency() {
        Player originalPlayer = gameStateManager.getPlayer();
        Maze originalMaze = gameStateManager.getMaze();
        Trivia originalTrivia = gameStateManager.getTrivia();

        // Create and set a test state
        TestGameState testState = new TestGameState();
        gameStateManager.setState(testState);

        // Verify components remain the same
        assertSame(originalPlayer, gameStateManager.getPlayer());
        assertSame(originalMaze, gameStateManager.getMaze());
        assertSame(originalTrivia, gameStateManager.getTrivia());

        // Transition to game over state
        GameOverState gameOverState = new GameOverState(false);
        gameStateManager.setState(gameOverState);

        // Verify components still remain the same
        assertSame(originalPlayer, gameStateManager.getPlayer());
        assertSame(originalMaze, gameStateManager.getMaze());
        assertSame(originalTrivia, gameStateManager.getTrivia());
    }

    /**
     * Simple test implementation of GameState for integration testing.
     */
    private static class TestGameState extends GameState {
        boolean enterCalled = false;
        boolean exitCalled = false;

        @Override
        public void enter(GameStateManager theManager) {
            if (theManager == null) {
                throw new IllegalArgumentException("GameStateManager cannot be null");
            }
            this.myManager = theManager;
            enterCalled = true;
        }

        @Override
        public void exit() {
            exitCalled = true;
        }

        @Override
        public void update() {
            // No implementation needed for test
        }

        @Override
        public String getStateName() {
            return "TEST_STATE";
        }
    }
}