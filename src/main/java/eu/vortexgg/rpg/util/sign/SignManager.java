package eu.vortexgg.rpg.util.sign;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenSignEditor;

@Getter
public class SignManager implements Listener {
    
    HashMap<String, Pair<SignCallback, Location>> editors = Maps.newHashMap();

    public SignManager(Plugin plugin) {
	ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {

	    @Override
	    public void onPacketReceiving(PacketEvent event) {
		Player p = event.getPlayer();
		if(hasEditor(p)) {
		    close(p, event.getPacket().getStringArrays().read(0));
		}
	    }

	});
	Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, this, EventPriority.HIGHEST, new EventExecutor() {

	    @Override
	    public void execute(Listener listener, Event event) throws EventException {
		editors.remove(((PlayerQuitEvent)event).getPlayer().getName());
	    }
	    
	}, plugin, true);
    }
    
    public void open(Player p, SignCallback callback, String... lines) {
	Location location = p.getLocation().add(0.0, 2.0, 0.0);
	p.sendBlockChange(location, Material.SIGN_POST, (byte)0);
	if (lines != null && lines.length == 4)
	    p.sendSignChange(location, lines);
	((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
	editors.put(p.getName(), Pair.of(callback, location));
    }
    
    public void close(Player p, String[] lines) {
	Pair<SignCallback, Location> editor = editors.remove(p.getName());
	if(editor != null) {
	    editor.getLeft().update(p, lines);
	    Location loc = editor.getRight();
	    Block block = loc.getBlock();
	    p.sendBlockChange(loc, block.getType(), block.getData());
	}
    }
    
    public boolean hasEditor(Player p) {
	return editors.containsKey(p.getName());
    }
    
    public interface SignCallback {
	void update(Player p, String[] lines);
    }
    
}
