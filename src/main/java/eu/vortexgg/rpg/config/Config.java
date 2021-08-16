package eu.vortexgg.rpg.config;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.util.ConfigFile;

public interface Config {
    
    static final ConfigFile CONFIG = RPG.get().getConfig();
    
    static final String SQL_HOST = CONFIG.getString("SQL.HOST");
    static final Integer SQL_PORT = CONFIG.getInt("SQL.PORT");
    static final Boolean SQL_AUTH_ENABLED = CONFIG.getBoolean("SQL.AUTH_ENABLED");
    static final String SQL_DATABASE = CONFIG.getString("SQL.DATABASE");
    static final String SQL_USERNAME = CONFIG.getString("SQL.USERNAME");
    static final String SQL_PASSWORD = CONFIG.getString("SQL.PASSWORD");
    
    static final String MONGO_HOST = CONFIG.getString("MONGO.HOST");
    static final Integer MONGO_PORT = CONFIG.getInt("MONGO.PORT");
    static final Boolean MONGO_AUTH_ENABLED = CONFIG.getBoolean("MONGO.AUTH_ENABLED");
    static final String MONGO_DATABASE = CONFIG.getString("MONGO.DATABASE");
    static final String MONGO_USERNAME = CONFIG.getString("MONGO.USERNAME");
    static final String MONGO_PASSWORD = CONFIG.getString("MONGO.PASSWORD");
    
}
