package eu.vortexgg.rpg.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.quest.quester.Quester;
import eu.vortexgg.rpg.quest.quester.QuesterMenu;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestListener implements Listener {

    RPG plugin;
    QuestManager questManager;
    
    public QuestListener(RPG plugin, QuestManager questManager) {
	this.plugin = plugin;
	this.questManager = questManager;
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRight(NPCRightClickEvent e) {
	NPC npc = e.getNPC();
	for (Quester quester : questManager.getQuesters().values()) {
	    if (npc != null && npc.getUniqueId().equals(quester.getNpc())) {
		new QuesterMenu(quester, e.getClicker()).open();
		break;
	    }
	}
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent e) {
	Entity entity = e.getEntity();
	if(entity instanceof Player)
	    questManager.notify((Player)entity);
    }

}
