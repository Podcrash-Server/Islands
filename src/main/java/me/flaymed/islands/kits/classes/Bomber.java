package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Bomber extends Kit {


    public Bomber(Class<? extends Ability>... abilities) {
        super("Bomber", abilities);
    }

    @Override
    public String getPermission() {
        return "islands.bomber";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.TNT, 1);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return null;
    }
}
