package me.flaymed.islands.kits.skills.berserker;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
    public void hit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager().getUniqueId() != getKitPlayer().getPlayer().getUniqueId()) return;
        if (getKitPlayer().getPlayer().getItemInHand().getType().toString().toLowerCase().contains("axe")) e.setDamage(e.getDamage() + 1);
    }
}
