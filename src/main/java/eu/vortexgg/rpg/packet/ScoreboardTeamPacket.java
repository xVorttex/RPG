package eu.vortexgg.rpg.packet;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase.EnumNameTagVisibility;

@Getter
public class ScoreboardTeamPacket {

    private static Field nameField, displayNameField, prefixField, suffixField, playersListField, typeField, optionsField, nametagVisibilityField;
    static {
	try {
	    /* 1.12.2 PacketPlayOutScoreboardTeam fields. */
	    nameField = PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
	    displayNameField = PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
	    prefixField = PacketPlayOutScoreboardTeam.class.getDeclaredField("c");
	    suffixField = PacketPlayOutScoreboardTeam.class.getDeclaredField("d");
	    playersListField = PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
	    typeField = PacketPlayOutScoreboardTeam.class.getDeclaredField("i");
	    optionsField = PacketPlayOutScoreboardTeam.class.getDeclaredField("j");
	    nametagVisibilityField = PacketPlayOutScoreboardTeam.class.getDeclaredField("e");
	    nameField.setAccessible(true);
	    displayNameField.setAccessible(true);
	    prefixField.setAccessible(true);
	    suffixField.setAccessible(true);
	    playersListField.setAccessible(true);
	    typeField.setAccessible(true);
	    optionsField.setAccessible(true);
	    nametagVisibilityField.setAccessible(true);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

    public ScoreboardTeamPacket(String name, String prefix, String suffix, Collection<String> players, int i) {
	try {
	    nameField.set(packet, name);
	    typeField.set(packet, i);
	    if (i == 0 || i == 2) {
		displayNameField.set(packet, name);
		prefixField.set(packet, prefix);
		suffixField.set(packet, suffix);
		optionsField.set(packet, 3);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	if (i == 0)
	    addAll(players);
    }

    public ScoreboardTeamPacket(String name, String prefix, String suffix, Collection<String> players, int i,
	    EnumNameTagVisibility visibility) {
	try {
	    nameField.set(packet, name);
	    nametagVisibilityField.set(packet, visibility.e);
	    typeField.set(packet, i);
	    if (i == 0 || i == 2) {
		displayNameField.set(packet, name);
		prefixField.set(packet, prefix);
		suffixField.set(packet, suffix);
		optionsField.set(packet, 3);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	if (i == 0)
	    addAll(players);
    }

    public ScoreboardTeamPacket(String name, Collection<String> players, int i, EnumNameTagVisibility visibility) {
	try {
	    optionsField.set(packet, 3);
	    nameField.set(packet, name);
	    typeField.set(packet, i);
	    nametagVisibilityField.set(packet, visibility.e);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	addAll(players);
    }

    public ScoreboardTeamPacket(String name, Collection<String> players, int i) {
	try {
	    optionsField.set(packet, 3);
	    nameField.set(packet, name);
	    typeField.set(packet, i);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	addAll(players);
    }

    private void addAll(Collection<String> collection) {
	if (collection != null) {
	    try {
		((Collection<String>) playersListField.get(packet)).addAll(collection);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }
    
    public void sendToPlayer(Player bukkitPlayer) {
	((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(packet);
    }
}
