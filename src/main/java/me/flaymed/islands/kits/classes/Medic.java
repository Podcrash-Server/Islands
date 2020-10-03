package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.Kit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import java.util.List;

public class Medic extends Kit {

    public Medic(Class<? extends Ability>... abilities) {
        super("Medic", abilities);
    }

    @Override
    public String getPermission() {
        return "islands.medic";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new Potion(PotionType.INSTANT_HEAL, 2).toItemStack(1);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new Potion(PotionType.INSTANT_HEAL, 2).toItemStack(1);
    }
}
