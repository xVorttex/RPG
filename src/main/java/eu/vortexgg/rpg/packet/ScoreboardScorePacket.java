package eu.vortexgg.rpg.packet;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore.EnumScoreboardAction;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

@Getter
public class ScoreboardScorePacket {

    static Field scoreStringField, scoreboardObjectiveNameField, scoreIntField, actionField;

    static {
        try {
            /* 1.12.2 PacketPlayOutScoreboardScore fields. */
            scoreStringField = PacketPlayOutScoreboardScore.class.getDeclaredField("a");
            scoreboardObjectiveNameField = PacketPlayOutScoreboardScore.class.getDeclaredField("b");
            scoreIntField = PacketPlayOutScoreboardScore.class.getDeclaredField("c");
            actionField = PacketPlayOutScoreboardScore.class.getDeclaredField("d");
            scoreStringField.setAccessible(true);
            scoreboardObjectiveNameField.setAccessible(true);
            scoreIntField.setAccessible(true);
            actionField.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

    public ScoreboardScorePacket(String score, String objectiveName, int scoreValue, EnumScoreboardAction action) {
        try {
            scoreStringField.set(packet, score);
            scoreboardObjectiveNameField.set(packet, objectiveName);
            scoreIntField.set(packet, scoreValue);
            actionField.set(packet, action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendToPlayer(Player bukkitPlayer) {
        ((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(packet);
    }

}
