package eu.vortexgg.rpg.quest;

import java.util.List;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.util.FlatFile;

public class QuestFile extends FlatFile {

    public QuestFile(RPG plugin) {
	super("quests", plugin);
    }

    @Override
    public void load() {
	if (config.contains("quests")) {
            List<Quest> list = (List<Quest>) config.get("quests");
            for (Quest quest : list) {
        	plugin.getQuestManager().getQuests().put(quest.getId(), quest);
            }
	}
    }

    @Override
    public void save() {
	config.set("quests", plugin.getQuestManager().getQuests().values());
	super.save();
    }

}
