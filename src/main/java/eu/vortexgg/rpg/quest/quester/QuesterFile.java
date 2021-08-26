package eu.vortexgg.rpg.quest.quester;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.util.FlatFile;
import eu.vortexgg.rpg.util.JavaUtil;

import java.util.ArrayList;
import java.util.List;

public class QuesterFile extends FlatFile {

    public QuesterFile(RPG plugin) {
        super("questers", plugin);
    }

    @Override
    public void load() {
        if (config.contains("questers")) {
            List<Quester> list = JavaUtil.createList(config.get("questers"), Quester.class);
            for (Quester quester : list) {
                QuestManager.get().getQuesters().put(quester.getId(), quester);
            }
        }
    }

    @Override
    public void save() {
        config.set("questers", new ArrayList<>(QuestManager.get().getQuesters().values()));
        super.save();
    }

}
