package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.miner.OreScan;
import me.flaymed.islands.kits.skills.miner.QuickMine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Miner extends Kit {

    public Miner() {
        super("Miner", OreScan.class, QuickMine.class);
    }

    @Override
    public String getPermission() {
        return "islands.miner";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STONE_PICKAXE);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new ItemStack(Material.STONE_PICKAXE);
    }
}
