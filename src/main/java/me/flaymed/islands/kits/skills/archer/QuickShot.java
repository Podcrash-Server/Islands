package me.flaymed.islands.kits.skills.archer;

import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.api.listeners.GameDamagerConverterListener;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.List;

public class QuickShot extends Ability implements Interact, Cooldown {
    //edit this.
    private final float damage = 6;

    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    public String getName() {
        return "Quick Shot";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);
    }

    @Override
    public void doAbility() {
        Player player = getKitPlayer().getPlayer();
        Vector v = player.getLocation().getDirection().multiply(3);
        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setVelocity(v);
        GameDamagerConverterListener.forceAddArrow(arrow, damage);
    }
}
