package eu.vortexgg.rpg.listener;

import org.bukkit.event.Listener;

import eu.vortexgg.rpg.RPG;

public class MobListener implements Listener {

    private final RPG plugin;

    public MobListener(RPG plugin) {
	this.plugin = plugin;
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


}
