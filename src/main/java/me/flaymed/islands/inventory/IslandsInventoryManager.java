package me.flaymed.islands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class IslandsInventoryManager {

    private static Inventory kitSelectInventory;

    public static void createKitSelectInventory() {
        IslandsInventoryItem berserkerAxe = new IslandsInventoryItem(Material.STONE_AXE, "Berserker", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 10);
        IslandsInventoryItem brawlerSword = new IslandsInventoryItem(Material.IRON_SWORD, "Brawler", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 12);
        IslandsInventoryItem archerBow = new IslandsInventoryItem(Material.BOW, "Archer", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 14);
        IslandsInventoryItem medicPotion = new IslandsInventoryItem(new Potion(PotionType.INSTANT_HEAL, 2), "Medic", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 16);
        IslandsInventoryItem minerPickaxe = new IslandsInventoryItem(Material.DIAMOND_PICKAXE, "Miner", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 39);
        IslandsInventoryItem bomberTnt = new IslandsInventoryItem(Material.TNT, "Bomber", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 41);

        Inventory inventory = Bukkit.createInventory(null, 6, "Select a kit");
        inventory.setItem(berserkerAxe.getSlotID(), berserkerAxe.getItem());
        inventory.setItem(brawlerSword.getSlotID(), brawlerSword.getItem());
        inventory.setItem(archerBow.getSlotID(), archerBow.getItem());
        inventory.setItem(medicPotion.getSlotID(), medicPotion.getItem());
        inventory.setItem(minerPickaxe.getSlotID(), minerPickaxe.getItem());
        inventory.setItem(bomberTnt.getSlotID(), bomberTnt.getItem());
        kitSelectInventory = inventory;
    }

    public static Inventory getKitSelectInventory() {
        return kitSelectInventory;
    }

}
