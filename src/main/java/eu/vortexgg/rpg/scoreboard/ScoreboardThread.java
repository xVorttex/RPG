package eu.vortexgg.rpg.scoreboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreboardThread extends Thread {

    private boolean running;
    
    public ScoreboardThread() {
	super("Scoreboard-Thread");
	setDaemon(true);
    }

    @Override
    public void run() {
	while (running) {
	    try {
		for (VScoreboard scoreboard : ScoreboardManager.getBoards().values()) {
		    scoreboard.update();
		}
		sleep(100);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }
}
