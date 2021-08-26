package eu.vortexgg.rpg.quest.quester;

import com.google.common.collect.Lists;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.TaskUtil;
import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import eu.vortexgg.rpg.util.menu.item.type.UpdatableMenuItem;
import eu.vortexgg.rpg.util.menu.item.type.UpdatableMenuItem.Updater;
import eu.vortexgg.rpg.util.menu.type.PaginatedMenu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class QuesterMenu extends PaginatedMenu<Quest> {

    public static final MenuItem COMPLETED_GLASS_ITEM = new MenuItem(new VItemStack(Material.STAINED_GLASS_PANE, "&aВыполнено!", (short) 5));
    public static final MenuItem ALL_COMPLETED = new MenuItem(new VItemStack(Material.REDSTONE_BLOCK, "&aПоздравляем!", "&fБлагодарю за службу|&fВы &aвыполнили &fвсе мои квесты! &fПриходите позже!"));

    Quester quester;
    Player player;
    List<String> taken;
    Comparator<Quest> comparator = Comparator.comparing(quest -> quest.hasAccess(player));

    public QuesterMenu(Quester quester, Player player) {
        super(player, quester.getDisplayName(), 1);
        this.player = player;
        this.quester = quester;
        this.startSlot = 2;
        this.endSlot = 5;
        this.nextSlot = 8;
        this.backSlot = 0;
    }

    @Override
    public void refresh() {
        values = QuestManager.get().getAvalaibleQuestsFromQuester(player, quester);
        if (values.isEmpty()) {
            setItem(4, ALL_COMPLETED);
            setItem(0, null);
            setItem(8, null);
            return;
        }

        values.sort(comparator);
        taken = QuestManager.get().getTakenQuests().get(player.getUniqueId());

        super.refresh();
    }

    @Override
    public void fill(int slot, int index) {
        Quest quest = values.get(index);
        boolean isGood = quest.isGood(player), hasAccess = quest.hasAccess(player), isTaken = taken.contains(quest.getId());
        UpdatableMenuItem item = new UpdatableMenuItem(new VItemStack(hasAccess ? Material.BOOK_AND_QUILL : Material.BOOK, quest.getDisplayName()));
        List<String> lore = Lists.newArrayList();

        if (quest.hasText()) {
            for (String text : quest.getText())
                lore.add(ChatColor.STRIP_COLOR_PATTERN.matcher(text).replaceAll("§8"));
            lore.add("");
        }

        lore.add("&7Требуемый уровень: &f" + quest.getRequiredLevel());
        if (quest.hasNeedQuests()) {
            lore.add("&7Необходимые квесты:");
            Map<String, Quest> quests = QuestManager.get().getQuests();
            for (String id : quest.getNeedQuests()) {
                lore.add(" &8• &f" + ChatColor.stripColor(quests.get(id).getDisplayName()));
            }
        }

        lore.add("");
        lore.addAll(quest.getProgressText(player));
        lore.add("");

        lore.add("§7Награда за выполнение");
        if (quest.getRewardExp() > 0)
            lore.add(" §8• §a" + quest.getRewardExp() + "EXP");
        if (quest.hasRewards()) {
            for (ItemStack reward : quest.getRewards()) {
                lore.add(" §8• §2" + BukkitUtil.getName(reward) + " §fx" + reward.getAmount());
            }
        }
        lore.add("");

        if (isTaken) {
            item.setGlow(true);
            if (isGood) {
                item.addListener((type, inv, sl, pl) -> {
                    quest.complete(player);
                    inv.setItem(slot, COMPLETED_GLASS_ITEM);
                    TaskUtil.later(() -> {
                        inv.setItem(slot, null);
                        update();
                    }, 20);
                });
            }
        } else if (hasAccess) {
            item.addListener((type, inv, sl, pl) -> {
                quest.take(player);
                update();
            });
        }

        final String last = hasAccess
                ? (!isTaken ? "§7Нажмите для поручения квеста."
                : (isGood ? "§7Нажмите для §aвыполнения §7квеста" : "§сТребования не выполнены!"))
                : "§cУ вас нет доступа к этому квесту.";

        lore.add("§f▸ " + last);

        item.setRunnable(new Updater() {
            boolean turn;

            @Override
            public void update(Player p, UpdatableMenuItem item, Menu menu, int slot) {
                final String prefix = ((turn = !turn) ? "§e▸ " : "§f▹ ");

                List<String> list = item.getMeta().getLore();
                list.set(list.size() - 1, prefix + last);

                menu.setItem(slot, item.setLore(list));
            }

        });

        item.setLore(lore);

        super.setItem(slot, item);
        super.fill(slot, index);
    }

}
