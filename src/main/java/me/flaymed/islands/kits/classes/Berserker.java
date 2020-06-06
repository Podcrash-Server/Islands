package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.berserker.AxeBoost;
import me.flaymed.islands.kits.skills.berserker.Leap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Berserker extends IslandsPlayer {
    public Berserker(Player player) {
        super(player, "Berserker", Leap.class, AxeBoost.class);
        ItemStack air = new ItemStack(Material.AIR);
        setDefaultHotbar(new ItemStack[] {new ItemStack(Material.STONE_AXE), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone()});

    }
}
