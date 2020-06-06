package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.brawler.Behemoth;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Brawler extends IslandsPlayer {
    public Brawler(Player player) {
        super(player, "Brawler", Behemoth.class);
        ItemStack air = new ItemStack(Material.AIR);
        setDefaultHotbar(new ItemStack[] {new ItemStack(Material.IRON_SWORD), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone()});

    }
}
