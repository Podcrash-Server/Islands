package me.flaymed.islands.kits.skills.berserker;

import com.podcrash.api.damage.Cause;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class AxeBoost extends Ability implements Passive {

    @Override
    public String getName() {
        return "Axe Boost";
    }

    //No item needed
    @Override
    public ItemStack getItem() {
        return null;
    }

    @EventHandler
    public void hit(DamageApplyEvent e) {
        if (e.getCause() != Cause.MELEE && e.getCause() != Cause.MELEESKILL) return;
        if (e.getAttacker().getUniqueId() != getKitPlayer().getPlayer().getUniqueId()) return;
        e.setModified(true);
        if (getKitPlayer().getPlayer().getItemInHand().getType().toString().toLowerCase().contains("axe")) e.setDamage(e.getDamage() + 1);
    }
}
