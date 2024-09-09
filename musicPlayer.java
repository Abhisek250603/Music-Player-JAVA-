import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class musicPlayer {
    private static Clip clip;
    private static int currentSongIndex = 0;
    private static final String[] songs = {
            "D:\\Project\\1.wav",
            "D:\\Project\\2.wav",
            "D:\\Project\\3.wav",
            "D:\\Project\\4.wav",
            "D:\\Project\\5.wav",
            "D:\\Project\\6.wav",
            "D:\\Project\\7.wav",
            "D:\\Project\\8.wav",
            "D:\\Project\\9.wav",
            "D:\\Project\\10.wav"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Commands: play, pause, next, exit");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "play":
                    playSong(currentSongIndex);
                    break;

                case "pause":
                    pauseSong();
                    break;

                case "next":
                    nextSong();
                    break;

                case "exit":
                    stopPlayback();
                    running = false;
                    break;

                default:
                    System.out.println("Invalid command. Try 'play', 'pause', 'next', or 'exit'.");
            }
        }

        scanner.close();
    }

    private static void playSong(int index) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }

            File soundFile = new File(songs[index]);
            System.out.println("Playing: " + soundFile.getAbsolutePath());

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing audio: " + e.getMessage());
        }
    }

    private static void pauseSong() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            System.out.println("Audio paused.");
        } else if (clip != null && !clip.isRunning()) {
            clip.start();
            System.out.println("Audio resumed.");
        } else {
            System.out.println("No audio is currently playing.");
        }
    }

    private static void nextSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.length;
        playSong(currentSongIndex);
    }

    private static void stopPlayback() {
        if (clip != null) {
            clip.stop();
            clip.close();
            System.out.println("Playback stopped.");
        }
    }
}
