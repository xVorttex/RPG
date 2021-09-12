package eu.vortexgg.rpg.user.menu;

import com.google.common.collect.Lists;
import eu.vortexgg.rpg.side.Side;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SideMenu extends Menu {

    final VPlayer vplayer;
    final Player player;

    public SideMenu(VPlayer vplayer, Player player) {
        super(player, "Выбор силы", 3);
        this.vplayer = vplayer;
        this.player = player;
    }

    @Override
    public void update() {
        for (Side side : Side.values()) {
            MenuItem item = side.getItem();
            if (item == null)
                continue;
            item = item.clone();

            ArrayList<String> lore = Lists.newArrayList();
            lore.add("");

            item.setLore(lore);
        }
        super.update();
    }

}
