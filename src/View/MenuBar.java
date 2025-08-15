package View;

import javax.swing.*;
import java.util.function.Consumer;

public class MenuBar extends JMenuBar {
    private Runnable onNew, onSave, onLoad, onExit;
    private Runnable onEasy, onNormal, onHard;

    public MenuBar() {
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New");
        JMenuItem save = new JMenuItem("Quick Save");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem exit = new JMenuItem("Exit");
        newGame.addActionListener(e -> fire(onNew));
        save.addActionListener(e -> fire(onSave));
        load.addActionListener(e -> fire(onLoad));
        exit.addActionListener(e -> fire(onExit));
        game.add(newGame); game.add(save); game.add(load); game.addSeparator(); game.add(exit);

        JMenu diff = new JMenu("Difficulty");
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem normal = new JMenuItem("Normal");
        JMenuItem hard = new JMenuItem("Hard");
        easy.addActionListener(e -> fire(onEasy));
        normal.addActionListener(e -> fire(onNormal));
        hard.addActionListener(e -> fire(onHard));
        diff.add(easy); diff.add(normal); diff.add(hard);

        add(game); add(diff);
    }
    private void fire(Runnable r){ if(r!=null) r.run(); }

    public void onNew(Runnable r){ onNew=r; }
    public void onSave(Runnable r){ onSave=r; }
    public void onLoad(Runnable r){ onLoad=r; }
    public void onExit(Runnable r){ onExit=r; }
    public void onDifficultyEasy(Runnable r){ onEasy=r; }
    public void onDifficultyNormal(Runnable r){ onNormal=r; }
    public void onDifficultyHard(Runnable r){ onHard=r; }
}
