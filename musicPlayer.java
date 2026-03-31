import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class musicPlayer {
    private static Clip clip;
    private static int currentSongIndex = 0;
    private static final String MUSIC_FOLDER = "D:\\Projects\\Java Music Player\\public";
    private static String[] songs = loadSongs();

    private static String[] loadSongs() {
        File folder = new File(MUSIC_FOLDER);
        File[] wavFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
        if (wavFiles == null || wavFiles.length == 0) {
            System.out.println("No .wav files found in: " + MUSIC_FOLDER);
            return new String[0];
        }
        Arrays.sort(wavFiles);
        return Arrays.stream(wavFiles).map(File::getAbsolutePath).toArray(String[]::new);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        if (songs.length == 0) {
            System.out.println("No songs found. Exiting.");
            return;
        }
        System.out.println("Found " + songs.length + " song(s).");
        System.out.println("Menu:\nplay\npause\nnext\nexit");

        while (running) {
            System.out.println("Enter your command: ");
            System.out.print("Commands: play, pause, next, exit: ");
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
            // Stop the current song if it is playing
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }

            // Load the new song file
            File soundFile = new File(songs[index]);
            System.out.println("Playing: " + soundFile.getAbsolutePath());

            // Get an audio input stream from the sound file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a clip resource and open the audio input stream
            clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Start playing the song
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing audio: " + e.getMessage());
        }
    }

    private static void pauseSong() {
        // Pause the song if it is currently playing
        if (clip != null && clip.isRunning()) {
            clip.stop();
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
