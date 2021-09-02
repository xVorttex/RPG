package eu.vortexgg.rpg.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import eu.vortexgg.rpg.config.Config;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

@Getter
public class DataManager {

    public static final String DATABASE_NAME = "players";
    
    Connection connection;
    MongoClient client;
    MongoDatabase database;
    static DataManager instance;

    public DataManager() {
        instance = this;
    }

    public void connect() {
        try {
            if (Config.SQL_AUTH_ENABLED) {
                connection = DriverManager.getConnection("jdbc:mysql://" + Config.SQL_HOST + ":" + Config.SQL_PORT + "/" + Config.SQL_DATABASE + "?useSSL=false", Config.SQL_USERNAME, Config.SQL_PASSWORD);
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://" + Config.SQL_HOST + ":" + Config.SQL_PORT + "/" + Config.SQL_DATABASE + "?useSSL=false");
            }

            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + "(UUID CHAR(36),NAME VARCHAR(16),SIDE VARCHAR(16),LVL INT,EXP INT,PRIMARY KEY (UUID))").executeUpdate();

            Builder builder = MongoClientSettings.builder();
            builder.applyConnectionString(new ConnectionString("mongodb://" + Config.MONGO_HOST + ":" + Config.MONGO_PORT));
            if (Config.MONGO_AUTH_ENABLED) {
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

    public static DataManager get() {
        return instance;
    }

}
