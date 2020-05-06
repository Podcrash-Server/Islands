package me.flaymed.islands.kits.skills.berserker;

import com.podcrash.api.damage.Cause;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.skilltypes.Passive;
import org.bukkit.event.EventHandler;

public class AxeBoost extends Passive {

    public AxeBoost() {

    }

    @Override
    public String getName() {
        return "Axe Boost";
    }

    @EventHandler
    public void hit(DamageApplyEvent e) {
        if (e.getCause() != Cause.MELEE && e.getCause() != Cause.MELEESKILL) return;
        e.setModified(true);

        if (e.getAttacker() == getPlayer()) {
            if (getPlayer().getItemInHand().getType().toString().toLowerCase().contains("axe")) {
                e.setDamage(e.getDamage() + 1);
            }
        }

    }
}
