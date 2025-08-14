package View.audio;

import javax.sound.sampled.*;
import java.net.URL;

public final class SoundFX {
    private SoundFX() {}

    public static void play(String resourcePath) {
        try {
            URL url = SoundFX.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("SoundFX error: Resource not found: " + resourcePath);
                return;
            }
            try (AudioInputStream in = AudioSystem.getAudioInputStream(url)) {
                Clip clip = AudioSystem.getClip();
                clip.open(in);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("SoundFX error: " + e.getMessage());
        }
    }
}
