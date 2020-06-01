package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.BowShotSkill;
import com.podcrash.api.kits.skilltypes.Instant;
import com.podcrash.api.listeners.GameDamagerConverterListener;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class QuickShot extends Instant implements ICooldown {
    //edit this.
    private final float damage = 6;
    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        if (onCooldown()) {
            getPlayer().sendMessage(getCooldownMessage());
            return;
        }
        if (rightClickCheck(action))
            return;
        Vector v = getPlayer().getLocation().getDirection().multiply(3);
        Arrow arrow = getPlayer().launchProjectile(Arrow.class);
        arrow.setVelocity(v);
        GameDamagerConverterListener.forceAddArrow(arrow, damage);
        getPlayer().sendMessage(getUsedMessage());
        setLastUsed(System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return "Quick Shot";
    }

    @Override
    public ItemType getItemType() {
        return ItemType.BOW;
    }
}
