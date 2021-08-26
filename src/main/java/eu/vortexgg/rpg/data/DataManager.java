package eu.vortexgg.rpg.data;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import eu.vortexgg.rpg.config.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataManager {

    Connection connection;
    MongoClient client;
    MongoDatabase database;

    public void connect() {
	try {
	    if(Config.SQL_AUTH_ENABLED) {
		connection = DriverManager.getConnection("jdbc:mysql://" + Config.SQL_HOST + ":" + Config.SQL_PORT + "/" + Config.SQL_DATABASE + "?useSSL=false", Config.SQL_USERNAME, Config.SQL_PASSWORD);
	    } else {
		connection = DriverManager.getConnection("jdbc:mysql://" + Config.SQL_HOST + ":" + Config.SQL_PORT + "/" + Config.SQL_DATABASE + "?useSSL=false");
	    }

	    connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (UUID CHAR(36),NAME VARCHAR(16),SIDE VARCHAR(12),PRIMARY KEY (UUID))").executeUpdate();

	    Builder builder = MongoClientSettings.builder();
	    builder.applyConnectionString(new ConnectionString("mongodb://" + Config.MONGO_HOST + ":" + Config.MONGO_PORT));
	    if(Config.MONGO_AUTH_ENABLED) {
		builder.credential(MongoCredential.createCredential(Config.MONGO_USERNAME, Config.MONGO_DATABASE, Config.MONGO_PASSWORD.toCharArray()));
	    }

	    client = MongoClients.create(builder.build());
	    database = client.getDatabase("rpg");

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void disconnect() {
	try {
	    connection.close();
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
