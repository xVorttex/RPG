package eu.vortexgg.rpg.nametag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.lunarclient.bukkitapi.LunarClientAPI;

import eu.vortexgg.rpg.packet.ScoreboardTeamPacket;
import eu.vortexgg.rpg.util.BukkitUtil;
import lombok.Getter;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase.EnumNameTagVisibility;

public class NametagManager {

    @Getter
    static final List<Nametag> cachedTeams = Lists.newArrayList();
    static int teamCreateIndex = 1;

    public static void reloadPlayer(Player toRefresh) {
	for (Player pl : Bukkit.getOnlinePlayers())
	    reloadPlayerInternal(toRefresh, pl);
    }

    public static void reloadPlayer(Player toRefresh, Player refreshFor) {
	if (refreshFor == null) {
	    for (Player pl : Bukkit.getOnlinePlayers())
		reloadPlayerInternal(toRefresh, pl);
	    return;
	}
	reloadPlayerInternal(toRefresh, refreshFor);
    }

    private static void reloadPlayerInternal(Player toRefresh, Player refreshFor) {
	boolean canSeeFriendlyInvisibles = !((CraftPlayer) toRefresh).getHandle().isInvisible() || refreshFor.hasPermission("zelium.invis");
	new ScoreboardTeamPacket(getNametag(toRefresh, refreshFor).getName(), Arrays.asList(toRefresh.getName()),
		canSeeFriendlyInvisibles ? 2 /* Can see invis skins */ : 0,
		canSeeFriendlyInvisibles ? EnumNameTagVisibility.ALWAYS : EnumNameTagVisibility.NEVER)
		.sendToPlayer(refreshFor);
    }

    public static void initiatePlayer(Player player) {
	for (Nametag teamInfo : cachedTeams)
	    teamInfo.getPacket().sendToPlayer(player);
    }

    private static Nametag getOrCreate(String prefix, String suffix) {
	for (Nametag teamInfo : cachedTeams)
	    if (teamInfo.getPrefix().equals(prefix) && teamInfo.getSuffix().equals(suffix))
		return teamInfo;
	
	Nametag newTeam = new Nametag(String.valueOf(teamCreateIndex++), prefix, suffix);
	cachedTeams.add(newTeam);
	ScoreboardTeamPacket addPacket = newTeam.getPacket();
	for (Player player : Bukkit.getOnlinePlayers()) {
	    addPacket.sendToPlayer(player);
	}
	
	return newTeam;
    }

    private static Nametag getNametag(Player toRefresh, Player refreshFor) {
	if (BukkitUtil.isLP(refreshFor)) {
	    ArrayList<String> nametag = Lists.newArrayList();
	    LunarClientAPI.getInstance().resetNametag(toRefresh, refreshFor);
	    LunarClientAPI.getInstance().overrideNametag(toRefresh, nametag, refreshFor);
	    return getOrCreate("", "");
	}

	return getOrCreate("", "");
    }

}
