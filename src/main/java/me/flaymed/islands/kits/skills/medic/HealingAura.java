package me.flaymed.islands.kits.skills.medic;

import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Drop;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import java.util.List;

public class HealingAura extends Drop implements ICooldown {
    @Override
    public float getCooldown() {
        return 30;
    }

    @Override
    public boolean drop(PlayerDropItemEvent e) {
        if (e.getPlayer() != getPlayer()) return false;
        if (onCooldown()) {
            getPlayer().sendMessage(getCooldownMessage());
            return false;
        }
        this.setLastUsed(System.currentTimeMillis());

        List<LivingEntity> entities = getPlayer().getWorld().getLivingEntities();

        StatusApplier.getOrNew(getPlayer()).applyStatus(Status.REGENERATION, 5, 1, true);

        for (LivingEntity entity : entities) {
            if (entity instanceof Player) {
                Player player1 = (Player) entity;
                Player player2 = getPlayer();

                if (distance(player1.getLocation().getBlockX(), player1.getLocation().getBlockZ(), player2.getLocation().getBlockX(), player2.getLocation().getBlockZ()) <= 5.0) {

                    if (isAlly(player1)) StatusApplier.getOrNew(player1).applyStatus(Status.REGENERATION, 5, 1, true);
                }


            }
        }

        getPlayer().sendMessage(getUsedMessage());

        return true;

    }

    @Override
    public String getName() {
        return "Healing Aura";
    }

    @Override
    public ItemType getItemType() {
        return ItemType.NULL;
    }

    private double distance(int x1, int z1, int x2, int z2) {
        // Calculating distance
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
    }

}
