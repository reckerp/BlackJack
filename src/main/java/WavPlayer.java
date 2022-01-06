import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 * WavPlayer used to play sounds or music
 */
public class WavPlayer {
    public static AudioInputStream stream;
    public static Clip musicclip;
    public static Clip effectclip;

    /**
     * Starts playing the music
     * @param path
     */
    public static void startMusic(String path) {
        try { musicclip.stop(); } catch (Exception ex) {}
        try {
            File file = new File(path);

            stream = AudioSystem.getAudioInputStream(file);

            musicclip = AudioSystem.getClip();
            musicclip.open(stream);
            musicclip.start();
            musicclip.loop(99999);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Stops the music
     */
    public static void stopMusic() { musicclip.stop(); }

    /**
     * Plays sound effects
     * @param path
     */
    public static void startEffect(String path) {
        try {
            File file = new File(path);

            stream = AudioSystem.getAudioInputStream(file);

            effectclip = AudioSystem.getClip();
            effectclip.open(stream);
            effectclip.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
} 