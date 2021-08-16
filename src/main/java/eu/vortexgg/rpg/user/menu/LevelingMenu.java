package eu.vortexgg.rpg.user.menu;

import org.bukkit.entity.Player;

import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.menu.Menu;

public class LevelingMenu extends Menu {

    private final VPlayer vplayer;
    private final Player player;
    
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
