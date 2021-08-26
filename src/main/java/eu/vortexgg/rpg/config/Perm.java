package eu.vortexgg.rpg.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
@AllArgsConstructor
public enum Perm {

    ADMIN("rpg.admin"),
    BUILDER("rpg.builder");

    final String permission;

    public boolean hasPermission(CommandSender p) {
        boolean hasPermission = p.hasPermission(permission);
        if (!hasPermission)
            p.sendMessage("§3§lПрава §8• §7У вас недостаточно прав!");
        return hasPermission;
    }

}
