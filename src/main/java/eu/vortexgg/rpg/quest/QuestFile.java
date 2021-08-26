package eu.vortexgg.rpg.quest;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.util.FlatFile;
import eu.vortexgg.rpg.util.JavaUtil;

import java.util.ArrayList;
import java.util.List;

public class QuestFile extends FlatFile {

    public QuestFile(RPG plugin) {
        super("quests", plugin);
    }

    @Override
    public void load() {
        if (config.contains("quests")) {
            List<Quest> list = JavaUtil.createList(config.get("quests"), Quest.class);
            for (Quest quest : list) {
                QuestManager.get().getQuests().put(quest.getId(), quest);
            }
        }
    }

    @Override
    public void save() {
        config.set("quests", new ArrayList<>(QuestManager.get().getQuests().values()));
        super.save();
    }

}
