package eu.vortexgg.rpg.quest.edit.menu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.util.Callback;
import eu.vortexgg.rpg.util.TaskUtil;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;

public class QuestEditorMenu extends Menu {

    public QuestEditorMenu(List<ItemStack> fill, Callback<List<ItemStack>> callback, Quest quest, QuestMenu parent) {
	super(null, "Квест Эдитор > Изменение предметов", 6);
	if(fill != null) {
	    int slot = 0;
	    for(ItemStack item : fill) {
		setItem(slot, new MenuItem(item).setCancel(false));
		slot++;
	    }
	}
	setMenuListener((p, inv, menu) -> {
	    List<ItemStack> items = Lists.newArrayList();
	    for(ItemStack content : inv.getContents()) {
		if(content != null && content.getType() != Material.AIR) {
		    items.add(content);
		}
	    }
	    callback.callback(items);
	    parent.update();
	    p.sendMessage("§aПредметы были успешно сохранены!");
	    TaskUtil.sync(() -> parent.open(p));
	});
    }

}
