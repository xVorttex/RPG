package eu.vortexgg.rpg.util.menu.type;

import eu.vortexgg.rpg.util.VItemStack;
import eu.vortexgg.rpg.util.menu.Menu;
import eu.vortexgg.rpg.util.menu.item.MenuItem;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PaginatedMenu<T> extends Menu {

    TIntIntMap slotToIndex = new TIntIntHashMap(10, 0.5F, 0, -1);
    int startSlot, endSlot, nextSlot, backSlot, page, maxPages;
    boolean hasNextPage;
    List<T> values;

    public PaginatedMenu(Player owner, String title, int height) {
        super(owner, title, height);
    }

    @Override
    public void update() {
        updatables.clear();
        items.clear();
        slotToIndex.clear();
        setItem(nextSlot, getBackItem(this));
        setItem(backSlot, getNextItem(this));
        refresh();
        super.update();
    }

    public void refresh() {
        int slots = endSlot - startSlot, startIndex = page * slots, slot = startSlot, end = endSlot, size = values.size();
        maxPages = size % slots;

        if (page > 0) {
            startIndex -= page * 2 - 1;
        }

        for (int index = startIndex; slot < end && index < size; index++, slot++) {
            fill(slot, index);
        }

        if (slot == end) {
            hasNextPage = startIndex + slotToIndex.size() != size;
        } else {
            hasNextPage = false;
        }
    }

    public void fill(int slot, int index) {
        slotToIndex.put(slot, index);
    }

    public static MenuItem getBackItem(PaginatedMenu<?> menu) {
        MenuItem back = new MenuItem(new VItemStack(Material.ARROW, "§3Предыдущая страница §7[" + (menu.page + 1) + "/" + (menu.maxPages + 1) + "]"));
        back.addListener((type, m, slot, p) -> {
            if (menu.page > 0) {
                menu.page--;
                menu.update();
            }
        });
        return back;
    }

    public static MenuItem getNextItem(PaginatedMenu<?> menu) {
        MenuItem back = new MenuItem(new VItemStack(Material.ARROW, "§3Следущая страница §7[" + (menu.page + 1) + "/" + (menu.maxPages + 1) + "]"));
        back.addListener((type, m, slot, p) -> {
            if (menu.hasNextPage) {
                menu.page++;
                menu.update();
            }
        });
        return back;
    }

}
