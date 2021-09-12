package eu.vortexgg.rpg.user.menu;

import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.menu.Menu;
import org.bukkit.entity.Player;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelingMenu extends Menu {

    final VPlayer vplayer;
    final Player player;

    public LevelingMenu(VPlayer vplayer, Player player) {
        super(player, "Повышение уровня", 3);
        this.vplayer = vplayer;
        this.player = player;
    }

    @Override
    public void update() {

        super.update();
    }

}
