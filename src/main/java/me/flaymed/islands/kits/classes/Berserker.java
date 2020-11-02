package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.berserker.AxeBoost;
import me.flaymed.islands.kits.skills.berserker.Leap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Berserker extends Kit {

    public Berserker() {
        super("Berserker", AxeBoost.class, Leap.class);
    }

    @Override
    public String getPermission() {
        return "islands.berserker";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STONE_AXE, 1);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new ItemStack(Material.STONE_AXE, 1);
    }
}
