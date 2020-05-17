package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import org.bukkit.entity.Arrow;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;

public class QuickShot extends Instant implements ICooldown {
    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        if (!onCooldown()) {

            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                getPlayer().launchProjectile(Arrow.class);
                getPlayer().sendMessage(getUsedMessage());
                setLastUsed(System.currentTimeMillis());
            }

        } else {
            getPlayer().sendMessage(getCooldownMessage());
        }
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
