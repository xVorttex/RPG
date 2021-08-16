package eu.vortexgg.rpg.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import eu.vortexgg.rpg.RPG;

public class TaskUtil {

    private static final Plugin PLUGIN = RPG.get();
    private static final BukkitScheduler SCHEDULER = PLUGIN.getServer().getScheduler();
    
    public static BukkitTask async(Runnable run) {
	return SCHEDULER.runTaskAsynchronously(PLUGIN, run);
    }

    public static BukkitTask sync(Runnable run) {
	return SCHEDULER.runTask(PLUGIN, run);
    }

    public static BukkitTask later(Runnable run, int delay) {
	return SCHEDULER.runTaskLater(PLUGIN, run, delay);
    }
    
    public static BukkitTask timer(Runnable run, int delay, int repeat) {
	return SCHEDULER.runTaskTimer(PLUGIN, run, delay, repeat);
    }
    
}
