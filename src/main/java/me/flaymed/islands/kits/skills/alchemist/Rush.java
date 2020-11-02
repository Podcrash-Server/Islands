package me.flaymed.islands.kits.skills.alchemist;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.gamecore.kits.abilitytype.Drop;
import net.jafama.FastMath;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rush extends Ability implements Cooldown, Drop {
    @Override
    public float getCooldown() {
        return 30;
    }

    @Override
    public String getName() {
        return "Rush";
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public Material getItemType() {
        return Material.IRON_SWORD;
    }

    @Override
    public void doAbility() {
        getKitPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
        getKitPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 0));
    }

    private double distance(double x1, double z1, double x2, double z2) {
        // Calculating distance
        return FastMath.sqrt(FastMath.pow2(x2 - x1) + FastMath.pow2(z2 - z1));
    }

}
