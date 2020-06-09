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

    public IslandsInventoryItem(Material material, String name, List<String> lore, int slotID) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.item = setUpItem();
        this.slotID = slotID;
    }

    public IslandsInventoryItem(Potion potion, String name, List<String> lore, int slotID) {
        this.name = name;
        this.lore = lore;
        this.item = setUpItem(potion);
        this.slotID = slotID;
    }

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
