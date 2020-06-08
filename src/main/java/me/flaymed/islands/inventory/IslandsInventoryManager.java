package me.flaymed.islands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class IslandsInventoryManager {

    private static Inventory kitSelectInventory;

    public static void createKitSelectInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6, "Select a kit");
        inventory.setItem(IslandsInventoryItem.BERSERKER_AXE_SELECT.getSlotID(), IslandsInventoryItem.BERSERKER_AXE_SELECT.getItem());
        inventory.setItem(IslandsInventoryItem.BRAWLER_SWORD_SELECT.getSlotID(), IslandsInventoryItem.BRAWLER_SWORD_SELECT.getItem());
        inventory.setItem(IslandsInventoryItem.ARCHER_BOW_SELECT.getSlotID(), IslandsInventoryItem.ARCHER_BOW_SELECT.getItem());
        inventory.setItem(IslandsInventoryItem.MEDIC_POTION_SELECT.getSlotID(), IslandsInventoryItem.MEDIC_POTION_SELECT.getItem());
        inventory.setItem(IslandsInventoryItem.MINER_PICKAXE_SELECT.getSlotID(), IslandsInventoryItem.MINER_PICKAXE_SELECT.getItem());
        inventory.setItem(IslandsInventoryItem.BOMBER_TNT_SELECT.getSlotID(), IslandsInventoryItem.BOMBER_TNT_SELECT.getItem());
        kitSelectInventory = inventory;
    }

    public static Inventory getKitSelectInventory() {
        return kitSelectInventory;
    }

}
