package me.flaymed.islands.inventory;

import com.podcrash.gamecore.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class KitSelectGUI {

    private static KitSelectGUI ksg;
    private Inventory inventory;
    private List<Kit> kits;
    private int MAX_KITS = 18;

    public KitSelectGUI(Class<? extends Kit>... kits) {
        ksg = this;

        initalizeKits(kits);
        createGUI();
    }

    private void initalizeKits(Class<? extends Kit>... kits) {

        List<Kit> newKits = new ArrayList<>();

        for (Class<? extends Kit> kit : kits) {
            try {
                newKits.add(kit.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        this.kits = newKits;

    }

    private void createGUI() {
        List<Kit> kits = getKits();
        if (kits == null || kits.size() == 0) return;

        //this incase we can inventory size to scale with the size of kits (1 empty row between a row of kits)
        //int rows = (int) (2 + (Math.ceil(kits.size()/3.0) * 2) + 1);
        int slots = 6 * 9;
        this.inventory = Bukkit.createInventory(null, slots, "Select a Kit");
        ItemStack aquaGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
        ItemMeta aquaMeta = aquaGlass.getItemMeta();
        aquaMeta.setDisplayName(" ");
        aquaGlass.setItemMeta(aquaMeta);
        ItemStack blueGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 11);
        ItemMeta blueMeta = blueGlass.getItemMeta();
        blueMeta.setDisplayName(" ");
        blueGlass.setItemMeta(blueMeta);
        ItemStack grayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta grayMeta = grayGlass.getItemMeta();
        grayMeta.setDisplayName(" ");
        grayGlass.setItemMeta(grayMeta);

        //top row
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) this.inventory.setItem(i, aquaGlass);
            else this.inventory.setItem(i, blueGlass);
        }

        //bottom row
        for (int i = 45; i < 54; i++) {
            if (i % 2 == 0) this.inventory.setItem(i, blueGlass);
            else this.inventory.setItem(i, aquaGlass);
        }

        //kits
        for (int i = 0; i < MAX_KITS; i++) {
            if (i >= kits.size()) {
                this.inventory.setItem(i + 18, grayGlass);
                continue;
            }
            Kit kit = kits.get(i);
            ItemStack item = kit.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BOLD + "" + kit.getName() + " Kit");
            meta.setLore(kit.getDescription());
            item.setItemMeta(meta);
            this.inventory.setItem(18 + i, item);
        }

    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Kit> getKits() {
        return kits;
    }

    public static KitSelectGUI getInstance() {
        return ksg;
    }
}