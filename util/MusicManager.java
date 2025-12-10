package application.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/** OOP: Encapsulation. DSA: HashMap for logical name -> file mapping */
public class MusicManager {
    private static final Map<String, String> musicFiles = new HashMap<>();
    
    // RETAIN: Tracks the current main music player (BGM)
    private static MediaPlayer currentPlayer; 

    // NEW: Tracks all short, simultaneous sound effects (SFX)
    // Using ConcurrentHashMap for safety if threads overlap, keys are logical names, value is the player
    private static final Map<String, MediaPlayer> activeSfxPlayers = new ConcurrentHashMap<>();
    
    // NEW: Define which sounds should be treated as SFX and NOT stop the BGM
    private static final Set<String> sfxTracks = new HashSet<>();


    static {
        // Logical names mapped to actual file paths
        musicFiles.put("homepage", "/resources/music/Homepage.wav");
        musicFiles.put("storyline", "/resources/music/Storyline.wav");
        musicFiles.put("gameover", "/resources/music/Fail.wav");
        musicFiles.put("match13", "/resources/music/Match.wav");
        musicFiles.put("match45", "/resources/music/Match4&5.wav");
        musicFiles.put("order", "/resources/music/Order.wav");
        musicFiles.put("success", "/resources/music/Success.wav");
        musicFiles.put("tap", "/resources/music/Tap.wav");
        musicFiles.put("unlock", "/resources/music/Newrecipeunlocked.wav");
        musicFiles.put("cafe", "/resources/homePageMusicBg.wav");
        // Add more if needed!

        // Define which tracks are SFX and should play simultaneously
        sfxTracks.add("tap");
        sfxTracks.add("success");
        sfxTracks.add("unlock");
        sfxTracks.add("gameover");
        // NOTE: "order" is NOT included here, so if you play "order", it will stop the BGM.
    }

    // Play a music by logical name ("homepage", "order", etc.)
    public static void play(String name, boolean loop, double volume) {
        String lowerName = name.toLowerCase();

        // 🛑 CRITICAL MODIFICATION: Only stop the current player if it's NOT an SFX.
        if (!sfxTracks.contains(lowerName)) {
            stop(); // Stop the current main music track
        } else {
            // Ensure short SFX like 'tap' don't loop endlessly
            loop = false;
        }

        try {
            String filePath = musicFiles.get(lowerName);
            if (filePath == null) throw new RuntimeException("Music not mapped for: " + name);
            URL resource = MusicManager.class.getResource(filePath);
            if (resource == null) throw new RuntimeException(filePath + " not found! Check path.");
            
            Media media = new Media(resource.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
            player.setVolume(volume);
            player.play();

            if (!sfxTracks.contains(lowerName)) {
                // This is the new main BGM player
                currentPlayer = player;
            } else {
                // This is a new SFX player
                activeSfxPlayers.put(lowerName + player.hashCode(), player); // Store with unique key
                player.setOnEndOfMedia(() -> {
                    player.dispose();
                    activeSfxPlayers.remove(lowerName + player.hashCode());
                });
            }
            
            // Removed the old 'players.put(name, player);' which caused conflicts
            
        } catch (Exception e) {
            System.err.println("Could not play " + name + ": " + e.getMessage());
        }
    }

    public static void stop() {
        // Stop the main BGM player
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
        // Stop all active SFX players too (for a full stop)
        activeSfxPlayers.values().forEach(p -> {
            p.stop();
            p.dispose();
        });
        activeSfxPlayers.clear();
    }
    
    // The rest of the methods remain almost the same, but simplified to only check currentPlayer
    // Since 'players' HashMap is no longer used for BGM, it has been removed from the class fields.

    public static void pause() {
        if (currentPlayer != null) currentPlayer.pause();
    }

    public static void resume() {
        if (currentPlayer != null) currentPlayer.play();
    }

    public static boolean isMusicPlaying() {
        if (currentPlayer == null) return false;
        MediaPlayer.Status status = currentPlayer.getStatus();
        return status == MediaPlayer.Status.PLAYING || status == MediaPlayer.Status.PAUSED;
    }

    public static void fadeOut(double seconds) {
        if (currentPlayer != null) {
            final MediaPlayer playerToFade = currentPlayer;
            new Thread(() -> {
                double v = playerToFade.getVolume();
                int steps = Math.max(1, (int)(seconds * 20));
                for (int i = steps; i >= 0; i--) {
                    double newVol = v * i / steps;
                    javafx.application.Platform.runLater(() -> playerToFade.setVolume(newVol));
                    try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                }
                javafx.application.Platform.runLater(MusicManager::stop); // Calls the updated stop()
            }).start();
        }
    }
}