package eu.vortexgg.rpg.config;

import org.bukkit.command.CommandSender;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter @AllArgsConstructor @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Perm {

    ADMIN("rpg.admin"),
    BUILDER("rpg.builder");
    
    String permission;
    
    public boolean hasPermission(CommandSender p) {
	boolean hasPermission = p.hasPermission(permission);
	if(!hasPermission)
	    p.sendMessage("§3§lПрава §8• §7У вас недостаточно прав!");
	return hasPermission;
    }
    
}
