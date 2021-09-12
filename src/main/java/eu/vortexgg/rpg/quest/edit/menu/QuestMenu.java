package eu.vortexgg.rpg.quest.edit.menu;

import com.google.common.collect.Lists;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.quest.quester.Quester;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.Callback;
import eu.vortexgg.rpg.util.TaskUtil;
import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import eu.vortexgg.rpg.util.menu.type.PaginatedMenu;
import eu.vortexgg.rpg.util.sign.SignManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestMenu extends Menu {

    Quest quest;
    Menu rewardMenu, reqItemsMenu, questerMenu;

    public QuestMenu(Quest quest) {
        super(null, "Квест Едитор", 5);
        this.quest = quest;
        for(int i = 0; i < 9; i++) {
            setItem(i, DEFAULT_BACKGROUND_ITEM);
        }
        for(int i = 36; i < 45; i++) {
            setItem(i, DEFAULT_BACKGROUND_ITEM);
        }
        rewardMenu = new QuestEditorMenu(quest.getRewards(), (items) -> quest.setRewards(items), this);
        reqItemsMenu = new QuestEditorMenu(quest.getRequiredItems(), (items) -> quest.setRequiredItems(items), this);
        questerMenu = new QuestQuesterSelectorMenu(quest, this);
        setItem(21, createRequiredItemsEditorItem(this));
        setItem(22, createNameEditorItem(this, quest));
        setItem(23, createRewardEditorItem(this));
        setItem(31, createQuesterSelectorItem(this, quest));
    }

    @Override
    public void update() {
        setItem(13, createQuestInfoItem(quest));
        super.update();
    }

    public static MenuItem createRequiredItemsEditorItem(QuestMenu parent) {
        MenuItem item = new MenuItem(new VItemStack(Material.FLOWER_POT_ITEM, "&fИзменить нужные предметы квеста"));
        List<String> lore = Lists.newArrayList();
        lore.add("&7Нажмите для изменения предметов.");
        lore.add(" &7которые нужно принести. ");
        lore.add("&7Предметы сохраняться при ");
        lore.add(" &7закрытии инвентаря изменения. ");
        item.setLore(lore);

        item.addListener((type, m, slot, p) -> parent.reqItemsMenu.open(p));
        return item;
    }

    public static MenuItem createQuesterSelectorItem(QuestMenu parent, Quest quest) {
        MenuItem item = new MenuItem(new VItemStack(Material.TOTEM, "&fИзменить квестовщика"));
        List<String> lore = Lists.newArrayList();
        lore.add("&7Нажмите для изменения квестовщика.");
        item.setLore(lore);

        item.addListener((type, m, slot, p) -> parent.questerMenu.open(p));
        return item;
    }

    public static MenuItem createQuestInfoItem(Quest quest) {
        MenuItem item = new MenuItem(new VItemStack(Material.END_CRYSTAL, "&3Квест &f" + quest.getDisplayName()));
        List<String> lore = Lists.newArrayList();
        lore.add("&7ID: &f" + quest.getId());
        lore.add("&7Квестер: &f" + quest.getOwner());
        lore.add("");

        lore.add("&7Награды (" + quest.getRewards().size() + " шт.)");
        for (ItemStack reward : quest.getRewards())
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

    public static MenuItem createRewardEditorItem(QuestMenu parent) {
        MenuItem item = new MenuItem(new VItemStack(Material.NETHER_STAR, "&fИзменить награды квеста"));
        List<String> lore = Lists.newArrayList();
        lore.add("&7Нажмите для изменения наград.");
        lore.add(" &7Предметы сохраняться при ");
        lore.add("&7закрытии инвентаря изменения. ");
        item.setLore(lore);

        item.addListener((type, menu, slot, p) -> parent.rewardMenu.open(p));

        return item;
    }

    public static MenuItem createNameEditorItem(QuestMenu parent, Quest quest) {
        MenuItem item = new MenuItem(new VItemStack(Material.SIGN, "&fИзменить название квеста"));
        List<String> lore = Lists.newArrayList();
        lore.add("&7Нажмите для изменения названия,");
        lore.add(" &7квеста. (display name)");
        item.setLore(lore);

        item.addListener((type, menu, slot, p) -> SignManager.get().open(p, (player, lines) -> {
            quest.setDisplayName(BukkitUtil.color(lines[0]));
            parent.update();
            TaskUtil.sync(() -> parent.open(p));
        }, "", "Напишите новое имя", "Используйте знак &", "для изменения цвета"));

        return item;
    }

    public static class QuestEditorMenu extends Menu {
	
	List<ItemStack> fill;
	
        public QuestEditorMenu(List<ItemStack> fill, Callback<List<ItemStack>> callback, QuestMenu parent) {
            super(null, "Квест Эдитор > Изменение предметов", 6);
            setMenuListener((p, inv, menu) -> {
                List<ItemStack> items = Lists.newArrayList();
                for (ItemStack content : inv.getContents()) {
                    if (content != null && content.getType() != Material.AIR) {
                        items.add(content);
                    }
                }
                this.fill = items;
                callback.callback(items);
                parent.update();
                p.sendMessage("§aПредметы были успешно сохранены!");
                TaskUtil.sync(() -> parent.open(p));
            });
        }
        
        @Override
        public void update() {
            if (fill != null) {
                int slot = 0;
                for (ItemStack item : fill) {
                    setItem(slot, new MenuItem(item).setCancel(false));
                    slot++;
                }
            }
            super.update();
        }
        
    }

    public static class QuestQuesterSelectorMenu extends PaginatedMenu<Quester> {

        Quest quest;
        QuestMenu parent;

        public QuestQuesterSelectorMenu(Quest quest, QuestMenu parent) {
            super(null, "Квест Эдитор > Изменение квестовщика", 6);
            this.quest = quest;
            this.parent = parent;
            startSlot = 0;
            endSlot = 44;
            backSlot = 45;
            nextSlot = 53;
            setMenuListener((p, inv, menu) -> {
                parent.update();
                TaskUtil.sync(() -> parent.open(p));
            });
        }

        @Override
        public void refresh() {
            values = Lists.newArrayList(QuestManager.get().getQuesters().values());
            super.refresh();
        }

        @Override
        public void fill(int slot, int index) {
            Quester quester = values.get(index);
            MenuItem item = new MenuItem(new VItemStack(Material.TOTEM, quester.getDisplayName()));
            List<String> lore = Lists.newArrayList();
            boolean uses = quester.getId().equals(quest.getOwner());
            if (uses) {
                lore.add("&aИспользуется в данный момент");
                lore.add("");
                lore.add("&7Нажмите для отмены использования");
            } else {
                lore.add("&7Нажмите для установления");
            }

            item.addListener((type, menu, sl, p) -> {
                quest.setOwner(uses ? null : quester.getId());
                p.sendMessage("Вы установили §7" + quester.getId() + " §fдля §a" + quest.getDisplayName() + "§f.");
                p.closeInventory();
            });

            item.setLore(lore);
            super.setItem(slot, item);
            super.fill(slot, index);
        }
    }
}
