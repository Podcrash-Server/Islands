package me.flaymed.islands.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandsInventoryItem {

    private Material material;
    private String name;
    private List<String> lore;
    private ItemStack item;
    private int slotID;

    private IslandsInventoryItem(Material material, String name, List<String> lore, int slotID) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.item = setUpItem();
        this.slotID = slotID;
    }

    private IslandsInventoryItem(Potion potion, String name, List<String> lore, int slotID) {
        this.name = name;
        this.lore = lore;
        this.item = setUpItem(potion);
        this.slotID = slotID;
    }

    public static IslandsInventoryItem BERSERKER_AXE_SELECT = new IslandsInventoryItem(Material.STONE_AXE, "Berserker", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 10);
    public static IslandsInventoryItem BRAWLER_SWORD_SELECT = new IslandsInventoryItem(Material.IRON_SWORD, "Brawler", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 12);
    public static IslandsInventoryItem ARCHER_BOW_SELECT = new IslandsInventoryItem(Material.BOW, "Archer", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 14);
    public static IslandsInventoryItem MEDIC_POTION_SELECT = new IslandsInventoryItem(new Potion(PotionType.INSTANT_HEAL, 2), "Medic", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 16);
    public static IslandsInventoryItem MINER_PICKAXE_SELECT = new IslandsInventoryItem(Material.DIAMOND_PICKAXE, "Miner", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 39);
    public static IslandsInventoryItem BOMBER_TNT_SELECT = new IslandsInventoryItem(Material.TNT, "Bomber", Arrays.asList(String.format("%s Test description.", ChatColor.GRAY)), 41);

    private ItemStack setUpItem() {
        ItemStack item = new ItemStack(this.getMaterial());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(String.format("%s%s", ChatColor.GREEN, this.getName()));
        itemMeta.setLore(this.getLore());
        item.setItemMeta(itemMeta);

        return item;
    }

    private ItemStack setUpItem(Potion potion) {
        ItemStack item = potion.toItemStack(1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(String.format("%s%s", ChatColor.GREEN, this.getName()));
        itemMeta.setLore(this.getLore());
        item.setItemMeta(itemMeta);

        return item;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlotID() {
        return slotID;
    }
}
