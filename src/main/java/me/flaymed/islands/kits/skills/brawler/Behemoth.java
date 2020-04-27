package me.flaymed.islands.kits.skills.brawler;

import com.podcrash.api.damage.Cause;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.kits.skilltypes.Passive;
import com.podcrash.api.plugin.PodcrashSpigot;
import org.bukkit.event.EventHandler;

public class Behemoth extends Passive {
    private final double lessKnockbackPercentage = 0.05;
    private final double lessDamagePercentage = 0.05;
    private final double moreKnockbackPercentage = 0.05;
    public Behemoth() {
    }

    @Override
    public String getName() {
        return "Behemoth";
    }

    @EventHandler
    public void hit(DamageApplyEvent e) {
        if (e.getCause() != Cause.MELEE && e.getCause() != Cause.MELEESKILL) return;
        e.setModified(true);
        if (e.getAttacker() == getPlayer()) {
            double moreKBMultiplier = 1 + moreKnockbackPercentage;
            e.setVelocityModifierX(moreKBMultiplier);
            e.setVelocityModifierY(moreKBMultiplier);
            e.setVelocityModifierZ(moreKBMultiplier);
            PodcrashSpigot.debugLog(" I AM A BIG BAD BEHEMOTH>");
        } else if (e.getVictim() == getPlayer()) { //this check might not be needed
            double lessKBMultiplier = 1 - lessKnockbackPercentage;
            e.setVelocityModifierX(lessKBMultiplier);
            e.setVelocityModifierY(lessKBMultiplier);
            e.setVelocityModifierZ(lessKBMultiplier);

            double lessDamageMultiplier = 1 - lessDamagePercentage;
            e.setDamage(lessDamageMultiplier * e.getDamage());
        }
    }
}
