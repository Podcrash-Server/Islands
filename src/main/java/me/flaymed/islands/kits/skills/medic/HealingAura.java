package me.flaymed.islands.kits.skills.medic;

import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Drop;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashSet;
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

        Entity[] entities = getCloseByEntities(getPlayer().getLocation(), 6);

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player effectedPlayer = (Player) entity;
                StatusApplier.getOrNew(effectedPlayer).applyStatus(Status.REGENERATION, 5, 1, true);
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

    public Entity[] getCloseByEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius
                            && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

}
