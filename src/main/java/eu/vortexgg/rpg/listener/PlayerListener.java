package eu.vortexgg.rpg.listener;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    public PlayerListener(RPG plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        VPlayer vp = new VPlayer(id, p.getName());
        TaskUtil.async(vp::load);
        vp.register();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        VPlayer vp = VPlayer.getPlayers().remove(p.getUniqueId());
        TaskUtil.async(() -> {
            vp.save();
            vp.unregister();
        });
    }

}
