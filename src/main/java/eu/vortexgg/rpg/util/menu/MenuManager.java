package eu.vortexgg.rpg.util.menu;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import eu.vortexgg.rpg.util.menu.item.MenuItem;
import eu.vortexgg.rpg.util.menu.item.MenuItem.ItemListener;

public class MenuManager implements Listener {

    static Map<String, Menu> tracker = Maps.newHashMap();

    public static void track(String name, Menu m) {
	tracker.put(name, m);
    }

    public MenuManager(Plugin plugin) {
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
	Player p = (Player)e.getPlayer();
	Menu menu = tracker.remove(p.getName());
	if (menu != null) {
	    if(menu.getMenuListener() != null)
		menu.getMenuListener().onClose(p, e.getInventory(), menu);
	    if(menu.getTask() != null && menu.getOwner().equals(p))
		menu.getTask().cancel();
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
	Player p = (Player) e.getWhoClicked();
	Menu menu = tracker.get(p.getName());
	if (menu != null) {
	    MenuItem item = menu.getItems().get(e.getRawSlot());
	    if (item != null) {
		if(item.isCancel())
		    e.setCancelled(true);
		for (ItemListener l : item.getListeners())
		    l.onClick(e.getClick(), menu, e.getSlot(), p);
	    }
	}
    }
}
