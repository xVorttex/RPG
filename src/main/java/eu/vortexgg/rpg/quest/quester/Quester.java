package eu.vortexgg.rpg.quest.quester;

import com.eatthepath.uuid.FastUUID;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Quester implements ConfigurationSerializable {

    String id, displayName;
    UUID npc;

    public Quester(Map<String, Object> map) {
        id = (String) map.get("id");
        displayName = (String) map.get("displayName");
        npc = FastUUID.parseUUID((String) map.get("npc"));
    }

    public void spawnNPC(Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(this.npc);

        if (npc == null) {
            npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, displayName);
        }

        npc.setName(displayName);
        npc.teleport(location, TeleportCause.PLUGIN);
        npc.setBukkitEntityType(EntityType.VILLAGER);
        npc.setProtected(true);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        map.put("displayName", displayName);
        map.put("npc", FastUUID.toString(npc));
        return map;
    }

}
