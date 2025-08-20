//package View;
//
//import Model.Direction;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.util.function.Consumer;
//
//public class MazeControls extends KeyAdapter {
//    private final Consumer<Direction> onMove;
//    private final Runnable onPause, onNew, onSave, onQuit;
//    private boolean enabled = true;
//
//    public MazeControls(Consumer<Direction> onMove,
//                        Runnable onPause,
//                        Runnable onNew,
//                        Runnable onSave,
//                        Runnable onQuit) {
//        this.onMove = onMove;
//        this.onPause = onPause;
//        this.onNew   = onNew;
//        this.onSave  = onSave;
//        this.onQuit  = onQuit;
//    }
//
//    public void setEnabled(boolean enabled) { this.enabled = enabled; }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        if (!enabled) return;
//
//        switch (e.getKeyCode()) {
//            // Movement (arrows only)
//            case KeyEvent.VK_UP    -> onMove.accept(Direction.NORTH);
//            case KeyEvent.VK_DOWN  -> onMove.accept(Direction.SOUTH);
//            case KeyEvent.VK_LEFT  -> onMove.accept(Direction.WEST);
//            case KeyEvent.VK_RIGHT -> onMove.accept(Direction.EAST);
//
//            // Game control keys
//            case KeyEvent.VK_P -> { if (onPause != null) onPause.run(); }
//            case KeyEvent.VK_N -> { if (onNew   != null) onNew.run(); }
//            case KeyEvent.VK_S -> { if (onSave  != null) onSave.run(); }
//            case KeyEvent.VK_Q -> { if (onQuit  != null) onQuit.run(); }
//            default -> {}
//        }
//    }
//}
