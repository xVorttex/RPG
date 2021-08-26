package eu.vortexgg.rpg.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionType;

public class VItemStack extends ItemStack {

    public VItemStack(Material m, String name, String desc, Object... datas) {
        this(m, 1, name, desc, datas);
    }

    public VItemStack(PotionType pt, int level, boolean extended_duration, boolean splash, int amount, String name) {
        this(Material.POTION, amount, name, null);
    }

    public VItemStack(Material m, int amount, String name, String desc, Object... datas) {
        super(m, amount);
        ItemMeta im = getItemMeta();
        if (name != null)
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (desc != null && !desc.isEmpty())
            im.setLore(BukkitUtil.color(desc.split("\\|")));
        setItemMeta(im);
        if (datas == null || datas.length == 0)
            return;
        for (int i = 0; i < datas.length; i++) {
            Object data = datas[i];
            if (data instanceof Color) {
                try {
                    LeatherArmorMeta lam = (LeatherArmorMeta) im;
                    lam.setColor((Color) data);
                    setItemMeta(lam);
                } catch (Exception ignored) {
                }
            } else if (data instanceof Enchantment && datas[i + 1] instanceof Integer) {
                addUnsafeEnchantment((Enchantment) data, (Integer) datas[i + 1]);
                i++;
            } else if (data instanceof Integer) {
                setAmount((Integer) data);
            } else if (data instanceof Short) {
                setDurability((Short) data);
            }
        }
    }

    public VItemStack(Material m, int amount, String name) {
        this(m, amount, name, "", (Object[]) null);
    }

    public VItemStack(Material m, String name) {
        this(m, name, "");
    }

    public VItemStack(Material m) {
        this(m, (String) null);
    }

    public VItemStack(Material m, String name, String desc) {
        this(m, name, desc, (Object[]) null);
    }

    public VItemStack(Material m, String name, Object... objects) {
        this(m, name, "", objects);
    }
}
