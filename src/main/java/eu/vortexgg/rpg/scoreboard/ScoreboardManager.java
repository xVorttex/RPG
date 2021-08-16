package eu.vortexgg.rpg.scoreboard;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import eu.vortexgg.rpg.RPG;
import lombok.Getter;

@Getter
public class ScoreboardManager {

    @Getter
    private static final ConcurrentHashMap<String, VScoreboard> boards = new ConcurrentHashMap<>();
    private final ScoreboardThread thread;

    public ScoreboardManager(RPG plugin) {
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
