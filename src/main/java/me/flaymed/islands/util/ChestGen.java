package me.flaymed.islands.util;

import me.flaymed.islands.Islands;
import me.flaymed.islands.inventory.AmntMaterialData;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.*;

public final class ChestGen {
    private static AmntMaterialData[] validItems = {
        new AmntMaterialData(Material.DIAMOND_HELMET), new AmntMaterialData(Material.DIAMOND_CHESTPLATE), new AmntMaterialData(Material.DIAMOND_LEGGINGS), new AmntMaterialData(Material.DIAMOND_BOOTS),
        new AmntMaterialData(Material.IRON_HELMET), new AmntMaterialData(Material.IRON_CHESTPLATE), new AmntMaterialData(Material.IRON_LEGGINGS), new AmntMaterialData(Material.IRON_BOOTS),
        new AmntMaterialData(Material.IRON_SWORD), new AmntMaterialData(Material.IRON_AXE),
        new AmntMaterialData(Material.BOW),
        new AmntMaterialData(Material.ARROW, 16), new AmntMaterialData(Material.ARROW, 8),
        new AmntMaterialData(Material.MUSHROOM_SOUP)
    };

    public static void generateItem(Chest chest) {
        Random random = new Random();
        final int NUM_ITEMS = 4;
        List<AmntMaterialData> dataSet = new ArrayList<>(Arrays.asList(validItems));
        Inventory inventory = chest.getBlockInventory();
        int size = 0;
        while (size < NUM_ITEMS) {
            int pick = random.nextInt(dataSet.size());
            AmntMaterialData data = dataSet.get(pick);
            Islands.getInstance().getLogger().info("putting  a " + data);
            dataSet.remove(pick);
            inventory.addItem(data.getStack());
            size++;
        }
    }
}
