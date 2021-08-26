package eu.vortexgg.rpg.quest.quester;

import java.util.List;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.util.FlatFile;

public class QuesterFile extends FlatFile {

    public QuesterFile(RPG plugin) {
	super("questers", plugin);
    }

    @Override
    public void load() {
	if (config.contains("questers")) {
            List<Quester> list = (List<Quester>) config.get("questers");
            for (Quester quester : list) {
        	plugin.getQuestManager().getQuesters().put(quester.getId(), quester);
            }
	}
    }

    @Override
    public void save() {
	config.set("questers", plugin.getQuestManager().getQuesters().values());
	super.save();
    }

}
