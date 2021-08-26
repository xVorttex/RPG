package eu.vortexgg.rpg.util;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;

import eu.vortexgg.rpg.RPG;
import lombok.Getter;

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
	collection = plugin.getData().getDatabase().getCollection(collectionName);
    }

}
