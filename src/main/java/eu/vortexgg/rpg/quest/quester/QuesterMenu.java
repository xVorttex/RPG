package eu.vortexgg.rpg.quest.quester;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.TaskUtil;
import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import eu.vortexgg.rpg.util.menu.item.type.UpdatableMenuItem;
import eu.vortexgg.rpg.util.menu.item.type.UpdatableMenuItem.Updater;
import gnu.trove.map.hash.TIntIntHashMap;
import net.md_5.bungee.api.ChatColor;

public class QuesterMenu extends Menu {

    public static final MenuItem COMPLETED_GLASS_ITEM = new MenuItem(new VItemStack(Material.STAINED_GLASS_PANE, "&aВыполнено!", (short) 5));
    private static final MenuItem AIR = new MenuItem(Material.AIR);

    private final Quester quester;
    private final Player player;

    private final TIntIntHashMap slotToIndex = new TIntIntHashMap(10, 0.5F, 0, -1);
    private int page, maxPages;
    private boolean hasNextPage;

    private List<Quest> quests;

    public QuesterMenu(Quester quester, Player player) {
	super(player, quester.getDisplayName(), 1);
	this.player = player;
	this.quester = quester;
    }

    @Override
    public void open() {
	updateQuests();
	open();
    }

    public void updateQuests() {
	quests = RPG.get().getQuestManager().getAvalaibleQuestsFromQuester(player, quester);
	quests.sort(Comparator.comparing(quest -> ((Quest)quest).hasAccess(player)));

	if (quests.isEmpty()) {
	    setItem(4, new MenuItem(new VItemStack(Material.REDSTONE_BLOCK, "&aПоздравляем!", "&fБлагодарю за службу|&fВы &aвыполнили &fвсе мои квесты! &fПриходите позже!")));
	    return;
	}
	
	int startSlot = 2, endSlot = 7, slots = 5, startIndex = page * slots; // 5 = how much slots will be used in a row
	maxPages = quests.size() % slots;
	
	// Clearing inventory
	for (int i = 0; i < 9; i++)
	    setItem(i, AIR);
	
	if (page > 0) {
	    startIndex -= page * 2 - 1;
	}

	slotToIndex.clear();
	
	List<String> taken = RPG.get().getQuestManager().getTakenQuests().get(player.getUniqueId());
	
	for (int i = startIndex; startSlot < endSlot && i < quests.size(); i++, startSlot++) {
	    setQuestItem(startSlot, taken, i);
	}
	
	if (startSlot == endSlot) {
	    hasNextPage = startIndex + slotToIndex.size() != quests.size();
	} else {
	    hasNextPage = false;
	}
	
	setItem(0, getBackItem(this));
	setItem(8, getNextItem(this));
    }

    private void setQuestItem(int slot, List<String> taken, int index) {
	Quest quest = quests.get(index);
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
	    HashMap<String, Quest> quests = RPG.get().getQuestManager().getQuests();
	    for (String id : quest.getNeedQuests()) {
		lore.add(" &8• &f" + ChatColor.stripColor(quests.get(id).getDisplayName()));
	    }
	    lore.add("");
	}

	lore.addAll(quest.getProgressText(player));
	lore.add("");

	lore.add("§7Награда за выполнение");
	if(quest.getRewardExp() > 0)
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
			inv.setItem(slot, AIR);
			updateQuests();
		    }, 20);
		});
	    }
	} else if (hasAccess) {
	    item.addListener((type, inv, sl, pl) -> {
		quest.take(player);
		updateQuests();
	    });
	}

	final String last = hasAccess
		? (!isTaken ? "§7Нажмите для поручения квеста."
		: (isGood ? "§7Нажмите для §aвыполнения §7квеста" : "§сТребования не выполнены!"))
		: "§cУ вас нет доступа к этому квесту.";

	lore.add("§7▸ " + last);

	item.setRunnable(new Updater() {
	    boolean turn;

	    @Override
	    public void update(Player p, UpdatableMenuItem item, Menu menu, int slot) {
		List<String> list = item.getMeta().getLore();
		list.set(list.size() - 1, ((turn = !turn) ? "§e▸ " : "§f▹ ") + last);
		menu.setItem(slot, item.setLore(list));
	    }
	    
	});

	item.setLore(lore);

	setItem(slot, item);
	slotToIndex.put(slot, index);
    }

    public static MenuItem getBackItem(QuesterMenu menu) {
	MenuItem back = new MenuItem(new VItemStack(Material.ARROW, "§3Предыдущая страница §7[" + (menu.page + 1) + "/" + menu.maxPages + "]"));
	back.addListener((type, m, slot, p) -> {
	    if (menu.page > 0) {
		menu.page--;
		menu.updateQuests();
	    }
	});
	return back;
    }

    public static MenuItem getNextItem(QuesterMenu menu) {
	MenuItem back = new MenuItem(new VItemStack(Material.ARROW, "§3Следущая страница §7[" + (menu.page + 1) + "/" + menu.maxPages + "]"));
	back.addListener((type, m, slot, p) -> {
	    if (menu.hasNextPage) {
		menu.page++;
		menu.updateQuests();
	    }
	});
	return back;
    }

}
