package eu.vortexgg.rpg.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.eatthepath.uuid.FastUUID;
import com.google.common.collect.Maps;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.side.Side;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VPlayer {

    @Getter
    private static final HashMap<UUID, VPlayer> players = Maps.newHashMap();

    UUID uuid;
    String name;
    Side side = Side.NONE;
    int level, exp;
    Player player;
    
    public VPlayer(UUID uuid, String name) {
	this.uuid = uuid;
	this.name = name;
    }

    public int addExp(int exp) {
	Player player = getPlayer();
	if(player != null) {
	    player.sendMessage("+ §a§l" + exp + " EXP");
	}
	return this.exp += exp;
    }
    
    public void upgrade() {
	level++;
    }
    
    public Player getPlayer() {
	if(player == null)
	    return player = Bukkit.getPlayer(uuid);
	return player;
    }
    
    public void load() {
	try {
	    Connection connection = RPG.get().getData().getConnection();
	    ResultSet rs = connection.prepareStatement("SELECT * FROM players WHERE UUID = '" + FastUUID.toString(uuid) + "'").executeQuery();
	    if (rs.next()) {
		side = Side.valueOf(rs.getString("side"));
		level = rs.getInt("level");
		exp = rs.getInt("exp");
	    } else {
		connection.prepareStatement("INSERT INTO players(UUID, NAME, SIDE, LVL, EXP) VALUE('" + FastUUID.toString(uuid) + "', '" + name + "', '" + side.name() + "', '" + level + "', '" + exp + "')").executeUpdate();
	    }
	    rs.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void save() {
	try {
	    RPG.get().getData().getConnection().prepareStatement("UPDATE players SET NAME='" + name + "', SIDE='" + side.name() + "', LVL='" + level + "', EXP='" + exp + "' WHERE UUID = '" + FastUUID.toString(uuid) + "'").executeUpdate();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void register() {
	RPG.get().getQuestManager().register(uuid);
	players.put(uuid, this);
    }

    public void unregister() {
	players.remove(uuid);
    }
    
    public static VPlayer get(UUID uuid) {
	return players.get(uuid);
    }

}
