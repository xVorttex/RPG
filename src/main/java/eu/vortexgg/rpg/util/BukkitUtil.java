package eu.vortexgg.rpg.util;

import com.eatthepath.uuid.FastUUID;
import com.google.common.collect.Lists;
import com.lunarclient.bukkitapi.LunarClientAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class BukkitUtil {

    public static String formatHelp(String desc) {
        return "§e=--------- §f" + desc + " Помощь §e--------------=";
    }

    public static Player getPlayerByNameOrUUID(String string) {
        if (string == null)
            return null;
        return JavaUtil.isUUID(string) ? Bukkit.getPlayer(FastUUID.parseUUID(string)) : Bukkit.getPlayer(string);
    }

    public static boolean isLP(Player player) {
        return LunarClientAPI.getInstance().isRunningLunarClient(player);
    }

    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        if (nmsStack == null)
            return item;
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag() || nmsStack.getTag() == null) {
            tag = new NBTTagCompound();
        } else {
            tag = nmsStack.getTag();
        }
        tag.set("ench", new NBTTagList());
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static int countItems(PlayerInventory inv, ItemStack item) {
        int count = 0;
        for (ItemStack content : inv.getContents()) {
            if (content != null && item.isSimilar(content)) {
                count += content.getAmount();
            }
        }
        return count;
    }

    public static String toString(Location data) {
        return data.getWorld().getName() + "|" + data.getBlockX() + "|" + data.getBlockY() + "|" + data.getBlockZ()
                + "|" + data.getYaw() + "|" + data.getPitch();
    }

    public static void removeOneItem(Player i) {
        net.minecraft.server.v1_12_R1.ItemStack inHand = ((CraftPlayer) i).getHandle().inventory.getItemInHand();
        inHand.setCount(inHand.getCount() - 1);
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static ArrayList<String> color(String... strings) {
        ArrayList<String> list = Lists.newArrayList();
        for (String string : strings)
            list.add(ChatColor.translateAlternateColorCodes('&', string));
        return list;
    }

    public static ArrayList<String> color(List<String> strings) {
        ArrayList<String> list = Lists.newArrayList();
        for (String string : strings)
            list.add(ChatColor.translateAlternateColorCodes('&', string));
        return list;
    }

    public static String getName(ItemStack item) {
        return !item.hasItemMeta() ? WordUtils.capitalizeFully(item.getType().name().replaceAll("_", " ")) : item.getItemMeta().getDisplayName();
    }

    public static void giveOrDrop(Player p, ItemStack item) {
        PlayerInventory inv = p.getInventory();
        if (isInvFull(inv)) {
            p.getWorld().dropItem(p.getLocation(), item);
            return;
        }
        inv.addItem(item);
    }

    public static boolean isInvFull(PlayerInventory p) {
        return p.firstEmpty() == -1;
    }

    public static Location fromString(String data) {
        String[] parts = data.split("\\|");
        return new Location(Bukkit.getWorld(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]),
                Integer.valueOf(parts[3]), Float.valueOf(parts[4]), Float.valueOf(parts[5]));
    }

    public static ArrayList<Player> getLocalRecipients(Player pl) {
        Location playerLocation = pl.getLocation();
        ArrayList<Player> recipients = new ArrayList<>();
        final double squaredDistance = Math.pow(60, 2);
        for (Player recipient : pl.getWorld().getPlayers()) {
            if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance)
                continue;
            recipients.add(recipient);
        }
        return recipients;
    }

    public static boolean contains(PlayerInventory inv, ItemStack item) {
        for (ItemStack content : inv.getContents()) {
            if (content != null && content.getType() == item.getType()) {
                if (item.hasItemMeta() && content.hasItemMeta() && item.getItemMeta().equals(content.getItemMeta())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TextComponent createText(String msg, String tooltip, String command) {
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
        if (command != null)
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        if (tooltip != null)
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', tooltip)).create()));
        return message;
    }

}
