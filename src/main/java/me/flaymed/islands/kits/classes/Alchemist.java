package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.alchemist.Rush;
import me.flaymed.islands.kits.skills.alchemist.PeaceKeeper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Alchemist extends Kit {

    public Alchemist() {
        super("Alchemist", Rush.class);
    }

    @Override
    public String getPermission() {
        return "islands.alchemist";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BLAZE_POWDER, 1);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new ItemStack(Material.BLAZE_POWDER, 1);
    }
}
