package eu.vortexgg.rpg.quest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.data.DataManager;
import eu.vortexgg.rpg.util.MongoData;
import org.bson.Document;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class QuestData extends MongoData {

    MongoCollection<Document> questsCompleted;

    public QuestData(RPG plugin) {
        super("questsTaken", plugin);
    }

    @Override
    public void load() {
        super.load();
        questsCompleted = DataManager.get().getDatabase().getCollection("questsCompleted");

        for (Document document : collection.find()) {
            QuestManager.get().getTakenQuests().put((UUID) document.get("_id"), document.getList("quests", String.class));
        }

        for (Document document : questsCompleted.find()) {
            QuestManager.get().getCompletedQuests().put((UUID) document.get("_id"), document.getList("quests", String.class));
        }
    }

    @Override
    public void save() {
        for (Entry<UUID, List<String>> entry : QuestManager.get().getTakenQuests().entrySet()) {
            UUID uuid = entry.getKey();
            collection.replaceOne(Filters.eq("_id", uuid), new Document("_id", uuid).append("quests", entry.getValue()), REPLACE_OPTIONS);
        }

        for (Entry<UUID, List<String>> entry : QuestManager.get().getCompletedQuests().entrySet()) {
            UUID uuid = entry.getKey();
            questsCompleted.replaceOne(Filters.eq("_id", uuid), new Document("_id", uuid).append("quests", entry.getValue()), REPLACE_OPTIONS);
        }
    }

}
