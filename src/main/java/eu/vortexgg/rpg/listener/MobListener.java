package eu.vortexgg.rpg.listener;

import eu.vortexgg.rpg.RPG;
import org.bukkit.event.Listener;

public class MobListener implements Listener {

    private final RPG plugin;

    public MobListener(RPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


}
