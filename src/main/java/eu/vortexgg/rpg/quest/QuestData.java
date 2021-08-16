package eu.vortexgg.rpg.quest;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.util.MongoData;

public class QuestData extends MongoData {

    private MongoCollection<Document> questsCompleted;
    
    public QuestData(RPG plugin) {
	super("questsTaken", plugin);
    }

    @Override
    public void load() {
	super.load();
	questsCompleted = plugin.getData().getDatabase().getCollection("questsCompleted");

	MongoCursor<Document> iterator = collection.find().iterator();
	while (iterator.hasNext()) {
	   Document document = iterator.next(); 
	   plugin.getQuestManager().getTakenQuests().put((UUID)document.get("_id"), document.getList("quests", String.class));
	}

	MongoCursor<Document> iterator1 = questsCompleted.find().iterator();
	while (iterator1.hasNext()) {
	   Document document = iterator1.next(); 
	   plugin.getQuestManager().getCompletedQuests().put((UUID)document.get("_id"), document.getList("quests", String.class));
	}
    }
    
    @Override
    public void save() {
	for(Entry<UUID, List<String>> entry : plugin.getQuestManager().getTakenQuests().entrySet()) {
	    UUID uuid = entry.getKey();
	    collection.replaceOne(Filters.eq("_id", uuid), new Document("_id", uuid).append("quests", entry.getValue()), REPLACE_OPTIONS);
	}

	for(Entry<UUID, List<String>> entry : plugin.getQuestManager().getCompletedQuests().entrySet()) {
	    UUID uuid = entry.getKey();
	    questsCompleted.replaceOne(Filters.eq("_id", uuid), new Document("_id", uuid).append("quests", entry.getValue()), REPLACE_OPTIONS);
	}
    }

}
