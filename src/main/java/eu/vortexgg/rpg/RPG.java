package eu.vortexgg.rpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.vortexgg.rpg.command.QuestCommands;
import eu.vortexgg.rpg.data.DataManager;
import eu.vortexgg.rpg.listener.PlayerListener;
import eu.vortexgg.rpg.nametag.NametagManager;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.scoreboard.ScoreboardManager;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.ConfigFile;
import eu.vortexgg.rpg.util.menu.MenuManager;
import eu.vortexgg.rpg.util.sign.SignManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RPG extends JavaPlugin {

    DataManager data;
    ConfigFile config;
    QuestManager questManager;
    SignManager signManager;
    ScoreboardManager scoreboardManager;
    static RPG instance;

    @Override 
    public void onEnable() {
	instance = this;
	
	config = new ConfigFile("config.yml", this);

	registerManagers();
	registerListeners();
	registerCommands();
        
	for(Player p : Bukkit.getOnlinePlayers()) {
	    VPlayer vp = new VPlayer(p.getUniqueId(), p.getName());
	    vp.load();
	    vp.register();
	}
	
    }
    
    @Override
    public void onDisable() {
	questManager.getQuestFile().save();
	questManager.getQuesterFile().save();
	questManager.getQuestPlayerData().save();
	
	scoreboardManager.stop();
	NametagManager.getCachedTeams().clear();
	
	for(VPlayer p : VPlayer.getPlayers().values()) {
	    p.save();
	    p.unregister();
	}

	data.disconnect(); 
    }
    
    private void registerManagers() {
	data = new DataManager();
	data.connect();
	
	scoreboardManager = new ScoreboardManager(this);
	questManager = new QuestManager(this);
	signManager = new SignManager(this);
	new MenuManager(this);
	new NametagManager();
    }
    
    private void registerCommands() {
	new QuestCommands(this);
    }
    
    public void registerListeners() {
	new PlayerListener(this);
    }
    
    public static RPG get() {
	return instance;
    }
    
}
