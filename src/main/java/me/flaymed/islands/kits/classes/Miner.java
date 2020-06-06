package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.miner.OreScan;
import me.flaymed.islands.kits.skills.miner.QuickMine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Miner extends IslandsPlayer {
    public Miner(Player player) {
        super(player, "Miner", OreScan.class, QuickMine.class);
        ItemStack air = new ItemStack(Material.AIR);
        setDefaultHotbar(new ItemStack[] {new ItemStack(Material.IRON_PICKAXE), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone()});

    }

}
