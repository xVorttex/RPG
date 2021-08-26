package eu.vortexgg.rpg.side;

import com.google.common.collect.Lists;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum Side {
    NONE(null),
    MAGE(new MenuItem(new VItemStack(Material.CHORUS_FLOWER, "&5&lМагия")).setLore(Lists.newArrayList("", "")).addListener((type, p, slot, menu) -> {

    })),
    WARRIOR(new MenuItem(new VItemStack(Material.DIAMOND_SWORD, "&c&lСила")).setLore(Lists.newArrayList("", "")).addListener((type, p, slot, menu) -> {

    }));

    final MenuItem item;

    public int getSideOnline() {
        int online = 0;
        for (VPlayer vp : VPlayer.getPlayers().values())
            if (vp.getSide() == this)
                online++;
        return online;
    }

}
