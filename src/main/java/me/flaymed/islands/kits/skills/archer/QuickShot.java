package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.BowShotSkill;
import com.podcrash.api.kits.skilltypes.Instant;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class QuickShot extends BowShotSkill implements ICooldown {
    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        if (!onCooldown()) {

            if (rightClickCheck(action)) return;
            Vector v = getPlayer().getLocation().getDirection().multiply(3);
            getPlayer().launchProjectile(Arrow.class).setVelocity(v);
            getPlayer().sendMessage(getUsedMessage());
            setLastUsed(System.currentTimeMillis());

        } else {
            getPlayer().sendMessage(getCooldownMessage());
        }
    }

    @Override
    protected void shotArrow(Arrow arrow, float force) {

    }

    @Override
    protected void shotEntity(DamageApplyEvent event, Player shooter, LivingEntity victim, Arrow arrow, float force) {

    }

    @Override
    protected void shotGround(Player shooter, Location location, Arrow arrow, float force) {

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
