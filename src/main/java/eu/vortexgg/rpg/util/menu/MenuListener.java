package eu.vortexgg.rpg.util.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface MenuListener {
    void onClose(Player p, Inventory inv, Menu menu);
}