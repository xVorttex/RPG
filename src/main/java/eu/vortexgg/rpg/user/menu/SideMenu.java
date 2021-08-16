package eu.vortexgg.rpg.user.menu;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import eu.vortexgg.rpg.side.Side;
import eu.vortexgg.rpg.user.VPlayer;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;

public class SideMenu extends Menu {

    private final VPlayer vplayer;
    private final Player player;
    
    public SideMenu(VPlayer vplayer, Player player) {
	super(player, "Выбор силы", 3);
	this.vplayer = vplayer;
	this.player = player;
    }

    @Override
    public void update() {
	for(Side side : Side.values()) {
	    MenuItem item = side.getItem();
	    if(item == null)
		continue;
	    item = item.clone();
	    
	    ArrayList<String> lore = Lists.newArrayList();
	    lore.add("");
	    
	}
	super.update();
    }
    
}
