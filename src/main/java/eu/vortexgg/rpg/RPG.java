package eu.vortexgg.rpg;

import eu.vortexgg.rpg.command.QuestCommands;
import eu.vortexgg.rpg.data.DataManager;
import eu.vortexgg.rpg.listener.PlayerListener;
import eu.vortexgg.rpg.listener.QuestListener;
import eu.vortexgg.rpg.nametag.NametagManager;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.quest.quester.Quester;
import eu.vortexgg.rpg.scoreboard.ScoreboardManager;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.ConfigFile;
import eu.vortexgg.rpg.util.menu.MenuManager;
import eu.vortexgg.rpg.util.sign.SignManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class RPG extends JavaPlugin {

    @Getter
    ConfigFile config;

    DataManager data;
    QuestManager questManager;
    SignManager signManager;
    ScoreboardManager scoreboardManager;

    static RPG instance;

    @Override
    public void onEnable() {
        instance = this;

        config = new ConfigFile("config.yml", this);

        Arrays.asList(Quest.class, Quester.class).forEach(ConfigurationSerialization::registerClass);

        registerManagers();
        registerListeners();
        registerCommands();

        for (Player p : Bukkit.getOnlinePlayers()) {
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

        for (VPlayer p : VPlayer.getPlayers().values()) {
            p.save();
            p.unregister();
        }

        data.disconnect();
    }

    private void registerManagers() {
        data = new DataManager();
        data.connect();

        scoreboardManager = new ScoreboardManager();
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
        new QuestListener(this, questManager);
    }

    public static RPG get() {
        return instance;
    }

}
