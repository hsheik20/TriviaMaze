package View.audio;

import javax.sound.sampled.*;
import java.net.URL;

public final class SoundFX {
    private SoundFX() {}

    // single long-lived loop clip for background music
    private static Clip loopClip;

    /** Play a short, one-shot sound. */
    public static void play(String resourcePath) {
        try {
            URL url = SoundFX.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("SoundFX error: Resource not found: " + resourcePath);
                return;
            }
            AudioInputStream in = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            // auto-close when finished to avoid holding onto lines
            clip.addLineListener(ev -> {
                if (ev.getType() == LineEvent.Type.STOP) {
                    try { clip.close(); } catch (Exception ignored) {}
                }
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("SoundFX error: " + e.getMessage());
        }
    }

    /** Start looping background music (stops any previous loop). */
    public static void loop(String resourcePath) {
        stopLoop();
        try {
            URL url = SoundFX.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("SoundFX error: Resource not found: " + resourcePath);
                return;
            }
            AudioInputStream in = AudioSystem.getAudioInputStream(url);
            loopClip = AudioSystem.getClip();
            loopClip.open(in);
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
            loopClip.start();
        } catch (Exception e) {
            System.err.println("SoundFX loop error: " + e.getMessage());
            stopLoop();
        }
    }

    /** Stop current loop if any. */
    public static void stopLoop() {
        if (loopClip != null) {
            try { loopClip.stop(); } catch (Exception ignored) {}
            try { loopClip.close(); } catch (Exception ignored) {}
            loopClip = null;
        }
    }

    /** Is background music currently running? */
    public static boolean isLooping() {
        return loopClip != null && loopClip.isRunning();
    }
}
