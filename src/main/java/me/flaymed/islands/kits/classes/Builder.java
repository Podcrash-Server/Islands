package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Builder extends IslandsPlayer {

    private List<Skill> skills;

    public Builder(Player player, List<Skill> skills) {
        super(player);
        this.skills = skills;
    }

    @Override
    public int getHP() {
        return 20;
    }

    @Override
    public void equip() {
        ItemStack resistance = new ItemStack(Material.IRON_INGOT);
        ItemMeta resMeta = resistance.getItemMeta();
        resMeta.setDisplayName(ChatColor.GRAY + "Resistance");
        resistance.setItemMeta(resMeta);
        getPlayer().getInventory().setItem(0, resistance);

        ItemStack regeneration = new ItemStack(Material.GHAST_TEAR);
        ItemMeta regenMeta = regeneration.getItemMeta();
        regenMeta.setDisplayName(ChatColor.RED + "Regeneration");
        regeneration.setItemMeta(regenMeta);
        getPlayer().getInventory().setItem(1, regeneration);

        ItemStack jumpBoost = new ItemStack(Material.FEATHER);
        ItemMeta jumpMeta = jumpBoost.getItemMeta();
        jumpMeta.setDisplayName(ChatColor.GREEN + "Jump Boost");
        jumpBoost.setItemMeta(jumpMeta);
        getPlayer().getInventory().setItem(2, jumpBoost);
    }

    @Override
    public void applyEffects() {

    }
}
