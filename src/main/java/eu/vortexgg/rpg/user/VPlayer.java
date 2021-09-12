package eu.vortexgg.rpg.user;

import com.eatthepath.uuid.FastUUID;
import com.google.common.collect.Maps;
import eu.vortexgg.rpg.data.DataManager;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.side.Side;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;
import static eu.vortexgg.rpg.data.DataManager.DATABASE_NAME;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class VPlayer {

    @Getter
    static final HashMap<UUID, VPlayer> players = Maps.newHashMap();

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
        if (player != null) {
            player.sendMessage("+ §a§l" + exp + " EXP");
        }
        return this.exp += exp;
    }

    public void upgrade() {
        level++;
    }

    public Player getPlayer() {
        if (player == null)
            return player = Bukkit.getPlayer(uuid);
        return player;
    }

    public void load() {
        try {
            String identifier = FastUUID.toString(uuid);
            Connection connection = DataManager.get().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE UUID = '" + identifier + "'").executeQuery();
            if (rs.next()) {
                side = Side.valueOf(rs.getString("side"));
                level = rs.getInt("lvl");
                exp = rs.getInt("exp");
            } else {
                connection.prepareStatement("INSERT INTO " + DATABASE_NAME + "(UUID, NAME, SIDE, LVL, EXP) VALUE(" + identifier + ", " + name + ", " + side.name() + ", " + level + ", " + exp + ")").executeUpdate();
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            DataManager.get().getConnection().prepareStatement("UPDATE " + DATABASE_NAME + " SET NAME='" + name + "', SIDE='" + side.name() + "', LVL='" + level + "', EXP='" + exp + "' WHERE UUID = '" + FastUUID.toString(uuid) + "'").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register() {
        QuestManager.get().register(uuid);
        players.put(uuid, this);
    }

    public void unregister() {
        players.remove(uuid);
    }

    public static VPlayer get(UUID uuid) {
        return players.get(uuid);
    }

}
