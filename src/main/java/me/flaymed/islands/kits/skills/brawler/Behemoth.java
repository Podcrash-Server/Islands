package me.flaymed.islands.kits.skills.brawler;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Behemoth extends Ability implements Passive {
    private final double lessKnockbackPercentage = 0.05;
    private final double lessDamagePercentage = 0.05;
    private final double moreKnockbackPercentage = 0.05;

    @Override
    public String getName() {
        return "Behemoth";
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() == getKitPlayer().getPlayer()) {
            double moreKBMultiplier = 1 + moreKnockbackPercentage;
            Vector currentEntityVelocity = e.getEntity().getVelocity();
            Vector newVelocity = new Vector();
            newVelocity.setX(currentEntityVelocity.getX() * moreKBMultiplier);
            newVelocity.setY(currentEntityVelocity.getY() * moreKBMultiplier);
            newVelocity.setZ(currentEntityVelocity.getZ() * moreKBMultiplier);
            e.getEntity().setVelocity(newVelocity);

        } else if (e.getEntity() == getKitPlayer().getPlayer()) { //this check might not be needed
            double lessKBMultiplier = 1 - lessKnockbackPercentage;
            Vector currentEntityVelocity = e.getEntity().getVelocity();
            Vector newVelocity = new Vector();
            newVelocity.setX(currentEntityVelocity.getX() * lessKBMultiplier);
            newVelocity.setY(currentEntityVelocity.getY() * lessKBMultiplier);
            newVelocity.setZ(currentEntityVelocity.getZ() * lessKBMultiplier);
            e.getEntity().setVelocity(newVelocity);

            double lessDamageMultiplier = 1 - lessDamagePercentage;
            e.setDamage(lessDamageMultiplier * e.getDamage());
        }
    }
}
