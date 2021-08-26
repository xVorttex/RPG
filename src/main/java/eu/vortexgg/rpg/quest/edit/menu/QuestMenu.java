package eu.vortexgg.rpg.quest.edit.menu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.TaskUtil;
import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;

public class QuestMenu extends Menu {

    Quest quest;
    Menu rewardMenu, reqItemsMenu;
    
    public QuestMenu(Quest quest) {
	super(null, "Квест Едитор", 4);
	this.quest = quest;
    }

    @Override
    public void update() {
	rewardMenu = new QuestEditorMenu(quest.getRewards(), (items) -> quest.setRewards(items), quest, this);
	reqItemsMenu = new QuestEditorMenu(quest.getRequiredItems(), (items) -> quest.setRequiredItems(items), quest, this);
	setItem(13, createQuestInfoItem(quest));
	setItem(21, createRequiredItemsEditorItem(this, quest));
	setItem(22, createNameEditorItem(this, quest));
	setItem(23, createRewardEditorItem(this, quest));
	super.update();
    }
    
    public static MenuItem createRequiredItemsEditorItem(QuestMenu parent, Quest quest) {
	MenuItem item = new MenuItem(new VItemStack(Material.FLOWER_POT_ITEM, "&fИзменить нужные предметы квеста"));
	List<String> lore = Lists.newArrayList();
	lore.add("&7Нажмите для изменения предметов.");
	lore.add(" &7которые нужно принести. ");
	lore.add("&7Предметы сохраняться при ");
	lore.add(" &7закрытии инвентаря изменения. ");
	item.setLore(lore);
	
	item.addListener((type, m, slot, p) -> {
	    parent.reqItemsMenu.open(p);
	});
	return item;
    }
    
    public static MenuItem createQuestInfoItem(Quest quest) {
	MenuItem item = new MenuItem(new VItemStack(Material.END_CRYSTAL, "&3Квест &f" + quest.getDisplayName()));
	List<String> lore = Lists.newArrayList();
	lore.add("&7ID: &f" + quest.getId());
	lore.add("&7Квестер: &f" + quest.getOwner());
	lore.add("");
	
	lore.add("&7Награды (" + quest.getRewards().size() + " шт.)");
	for(ItemStack reward : quest.getRewards())
	    lore.add("&7- &f" + BukkitUtil.getName(reward));
	
	lore.add("&7Текст:");
	if (quest.hasText()) {
	    lore.addAll(quest.getText());
	} else {
	    lore.add(" &f*Отсуствует*");
	}
	
	lore.add("");
	lore.add("&7Используя кнопки ниже,");
	lore.add(" &7вы можете изменить квест.");
	lore.add("&fТекст квеста поменять можно");
	lore.add(" &fтолько в конфиге");
	
	item.setLore(lore);
	return item;
    }

    public static MenuItem createRewardEditorItem(QuestMenu parent, Quest quest) {
	MenuItem item = new MenuItem(new VItemStack(Material.NETHER_STAR, "&fИзменить награды квеста"));
	List<String> lore = Lists.newArrayList();
	lore.add("&7Нажмите для изменения наград.");
	lore.add(" &7Предметы сохраняться при ");
	lore.add("&7закрытии инвентаря изменения. ");
	item.setLore(lore);
	
	item.addListener((type, menu, slot, p) -> {
	    parent.rewardMenu.open(p);
	});

	return item;
    }

    public static MenuItem createNameEditorItem(QuestMenu parent, Quest quest) {
	MenuItem item = new MenuItem(new VItemStack(Material.SIGN, "&fИзменить название квеста"));
	List<String> lore = Lists.newArrayList();
	lore.add("&7Нажмите для изменения названия,");
	lore.add(" &7квеста. (display name)");
	item.setLore(lore);
	
	item.addListener((type, menu, slot, p) -> {
	    RPG.get().getSignManager().open(p, (player, lines) -> {
		quest.setDisplayName(BukkitUtil.color(lines[0]));
		parent.update();
		TaskUtil.sync(() -> parent.open(p));
	    }, "", "Напишите новое имя", "Используйте знак &", "для изменения цвета");
	});

	return item;
    }
}
