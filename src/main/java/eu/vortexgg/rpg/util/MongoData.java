package eu.vortexgg.rpg.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.data.DataManager;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoData {

    public static final ReplaceOptions REPLACE_OPTIONS = new ReplaceOptions().upsert(true);

    protected final RPG plugin;
    protected final String collectionName;
    protected MongoCollection<Document> collection;

    public MongoData(String collectionName, RPG plugin) {
        this.plugin = plugin;
        this.collectionName = collectionName;
    }

    public void save() {
    }

    public void load() {
        collection = DataManager.get().getDatabase().getCollection(collectionName);
    }

}
