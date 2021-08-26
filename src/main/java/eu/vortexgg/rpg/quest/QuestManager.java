package eu.vortexgg.rpg.quest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.quest.quester.Quester;
import eu.vortexgg.rpg.quest.quester.QuesterFile;
import eu.vortexgg.rpg.util.FlatFile;
import eu.vortexgg.rpg.util.MongoData;
import eu.vortexgg.rpg.util.TaskUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class QuestManager {

    Map<String, Quester> questers = Maps.newHashMap();
    Map<String, Quest> quests = Maps.newHashMap();
    Map<UUID, List<String>> takenQuests = Maps.newHashMap(), completedQuests = Maps.newHashMap();
    MongoData questPlayerData;
    FlatFile questFile, questerFile;
    RPG plugin;
    static QuestManager instance;

    public QuestManager(RPG plugin) {
        this.plugin = plugin;
        questFile = new QuestFile(plugin);
        questPlayerData = new QuestData(plugin);
        questerFile = new QuesterFile(plugin);

        TaskUtil.async(() -> {
            questFile.load();
            questPlayerData.load();
            questerFile.load();
        });

        instance = this;
    }

    public List<Quest> getQuestsOfQuester(Quester quester) {
        List<Quest> quests = Lists.newArrayList();
        for (Quest quest : this.quests.values())
            if (quest.getOwner().equals(quester.getId()))
                quests.add(quest);
        return quests;
    }

    public Quester getQuester(Quest quest) {
        for (Quester quester : this.questers.values())
            if (quest.getOwner().equals(quester.getId()))
                return quester;
        return null;
    }

    public Quest getNextQuestFromQuester(Player p, Quester quester) {
        List<String> compQuests = completedQuests.get(p.getUniqueId());
        for (Quest quest : getQuestsOfQuester(quester))
            if (!compQuests.contains(quest.getId()))
                return quest;
        return null;
    }

    public List<Quest> getTakenQuests(Player p) {
        List<Quest> quests = Lists.newArrayList();
        for (String id : takenQuests.get(p.getUniqueId())) {
            Quest quest = this.quests.get(id);
            if (quest != null)
                quests.add(quest);
        }
        return quests;
    }

    public List<Quest> getAvalaibleQuestsFromQuester(Player p, Quester quester) {
        List<Quest> quests = Lists.newArrayList();
        List<String> compQuests = completedQuests.get(p.getUniqueId());
        for (Quest quest : getQuestsOfQuester(quester))
            if (!compQuests.contains(quest.getId()))
                quests.add(quest);
        return quests;
    }

    public void notify(Player player) {
        for (Quest quest : getTakenQuests(player)) {
            if (quest.isGood(player)) {
                player.sendMessage("§6§lКвесты §8• §fВы можете выполнить квест " + quest.getDisplayName() + " §fу " + getQuester(quest).getDisplayName() + "§f. Подойдите к НПС чтобы завершить квест.");
            }
        }
    }

    public void register(UUID uuid) {
        takenQuests.putIfAbsent(uuid, Lists.newArrayList());
        completedQuests.putIfAbsent(uuid, Lists.newArrayList());
    }

    public static QuestManager get() {
        return instance;
    }

}
