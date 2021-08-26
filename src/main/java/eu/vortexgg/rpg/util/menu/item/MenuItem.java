package eu.vortexgg.rpg.util.menu.item;

import com.google.common.collect.Lists;
import eu.vortexgg.rpg.util.BukkitUtil;
import eu.vortexgg.rpg.util.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem implements Cloneable {

    List<ItemListener> listeners = Lists.newArrayList();
    boolean glow;
    ItemMeta meta;
    ItemStack itemstack;
    boolean cancel = true;

    public MenuItem(ItemStack itemstack) {
        this.itemstack = itemstack;
        meta = itemstack.getItemMeta();
    }

    public MenuItem(Material material) {
        itemstack = new ItemStack(material);
        meta = itemstack.getItemMeta();
    }

    public ItemStack getItemStack() {
        itemstack.setItemMeta(meta);
        return (glow ? BukkitUtil.addGlow(itemstack) : itemstack);
    }

    public MenuItem addListener(ItemListener l) {
        listeners.add(l);
        return this;
    }

    public MenuItem setName(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemstack.setItemMeta(meta);
        return this;
    }

    public MenuItem setType(Material name) {
        itemstack.setType(name);
        return this;
    }

    public MenuItem setDurability(short dur) {
        itemstack.setDurability(dur);
        return this;
    }

    public MenuItem setColor(DyeColor color) {
        itemstack.setDurability(color.getDyeData());
        return this;
    }

    public MenuItem setAmount(int amount) {
        itemstack.setAmount(amount);
        return this;
    }

    public MenuItem setLore(List<String> lore) {
        meta.setLore(BukkitUtil.color(lore));
        itemstack.setItemMeta(meta);
        return this;
    }

    public MenuItem setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }

    public MenuItem setGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    @Override
    public MenuItem clone() {
        return new MenuItem(listeners, glow, meta, getItemStack(), cancel);
    }

    public interface ItemListener {
        void onClick(ClickType paramClickType, Menu menu, int slot, Player paramPlayer);
    }

}
