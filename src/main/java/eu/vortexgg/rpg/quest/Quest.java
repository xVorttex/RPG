package eu.vortexgg.rpg.quest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.vortexgg.rpg.quest.edit.menu.QuestMenu;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.JavaUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Quest implements ConfigurationSerializable {

    String id, displayName, owner;
    int requiredLevel, rewardExp;
    List<String> text = Lists.newArrayList(), needQuests = Lists.newArrayList();
    List<ItemStack> rewards = Lists.newArrayList(), requiredItems = Lists.newArrayList();
    QuestMenu questMenu = new QuestMenu(this);

    public Quest(Map<String, Object> map) {
        id = (String) map.get("id");
        owner = (String) map.get("questerId");
        displayName = (String) map.get("displayName");
        requiredLevel = (int) map.get("requiredLevel");
        rewardExp = (int) map.get("rewardExp");

        text.addAll(BukkitUtil.color(JavaUtil.createList(map.get("text"), String.class)));
        rewards.addAll(JavaUtil.createList(map.get("rewards"), ItemStack.class));
        requiredItems.addAll(JavaUtil.createList(map.get("requiredItems"), ItemStack.class));
        needQuests.addAll(JavaUtil.createList(map.get("needQuests"), String.class));
    }

    public boolean isGood(Player p) {
        PlayerInventory inv = p.getInventory();
        List<ItemStack> leftRequiredItems = Lists.newArrayList(requiredItems);
        for (ItemStack content : inv.getContents())
            if (content != null)
                for (int i = 0; i < leftRequiredItems.size(); i++) {
                    ItemStack item = leftRequiredItems.get(i);
                    if (content.isSimilar(item) && BukkitUtil.countItems(inv, content) >= item.getAmount())
                        leftRequiredItems.remove(i);
                }
        return leftRequiredItems.isEmpty();
    }

    public List<String> getProgressText(Player p) {
        List<String> text = Lists.newArrayList();
        if (hasRequiredItems()) {
            text.add("??7?????? ?????????? ????????????????:");
            PlayerInventory inv = p.getInventory();
            for (ItemStack item : requiredItems) {
                int need = item.getAmount(), progress = 0;
                for (ItemStack content : inv.getContents())
                    if (content != null && content.isSimilar(item))
                        progress += content.getAmount();
                text.add((progress >= need ? "??a" : "??c") + " ??? ??3" + BukkitUtil.getName(item) + " ??7[??fx" + progress + "??8/??f" + need + "??7]");
            }
        }
        return text;
    }

    public void complete(Player p) {
        UUID uuid = p.getUniqueId();
        QuestManager.get().getTakenQuests().get(uuid).remove(id);
        QuestManager.get().getCompletedQuests().get(uuid).add(id);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1.5F);
        p.sendMessage("??6??l???????????? ??8??? ??f??????????????????????! ???? ??a?????????????????? ??f?????????? - " + displayName + "??f.");
        if (hasRewards()) {
            for (ItemStack reward : rewards) {
                BukkitUtil.giveOrDrop(p, reward);
            }
        }
        if (hasRequiredItems()) {
            PlayerInventory inv = p.getInventory();
            for (ItemStack item : requiredItems) {
                inv.remove(item);
            }
        }
        if (rewardExp != 0)
            VPlayer.get(uuid).addExp(rewardExp);
    }

    public void take(Player p) {
        p.sendMessage("??6??l???????????? ??8??? ??f?????? ?????? ?????????????? ?????????? ?????????? - " + displayName + "??f.");
        QuestManager.get().getTakenQuests().get(p.getUniqueId()).add(id);
    }

    public boolean hasAccess(Player p) {
        return VPlayer.get(p.getUniqueId()).getLevel() == requiredLevel && QuestManager.get().getCompletedQuests().get(p.getUniqueId()).stream().filter(id -> needQuests.contains(id)).collect(Collectors.toList()).size() == needQuests.size();
    }

    public boolean hasRewards() {
        return !rewards.isEmpty();
    }

    public boolean hasNeedQuests() {
        return !needQuests.isEmpty();
    }

    public boolean hasRequiredItems() {
        return !requiredItems.isEmpty();
    }

    public boolean hasText() {
        return !text.isEmpty();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        map.put("displayName", displayName);
        map.put("questerId", owner);
        map.put("rewardExp", rewardExp);
        map.put("requiredLevel", requiredLevel);
        map.put("text", text);
        map.put("rewards", rewards);
        map.put("requiredItems", requiredItems);
        return map;
    }
}
