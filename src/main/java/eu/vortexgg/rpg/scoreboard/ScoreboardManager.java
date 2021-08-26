package eu.vortexgg.rpg.scoreboard;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ScoreboardManager {

    @Getter
    static final ConcurrentHashMap<String, VScoreboard> boards = new ConcurrentHashMap<>();

    final ScoreboardThread thread;

    public ScoreboardManager() {
        thread = new ScoreboardThread();
        thread.setRunning(true);
        thread.start();
    }

    public static void getScores(List<String> lines, Player player) {
        if (!lines.isEmpty()) {
            lines.add(0, "");
            lines.add(" ");
        }
    }

    public void stop() {
        thread.setRunning(false);
    }

}
