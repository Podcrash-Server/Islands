package me.flaymed.islands.kits.skills.miner;

import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.IConstruct;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;

public class QuickMine extends Instant implements ICooldown, IConstruct {

    public QuickMine() {

    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        Player player = event.getPlayer();
        if(!onCooldown()) {
            if (player == this.getPlayer()) {
                if (player.getItemInHand().equals(Material.IRON_INGOT)) {
                    StatusApplier.getOrNew(player).applyStatus(Status.HASTE, 10, 3, true);
                    player.getInventory().remove(Material.IRON_INGOT);
                    setLastUsed(System.currentTimeMillis());
                    player.sendMessage(String.format("%sSkill> %sYou used: Quick mine!", ChatColor.BLUE, ChatColor.GRAY));
                }
            }
        } else this.getPlayer().sendMessage(getCooldownMessage());

    }

    @Override
    public String getName() {
        return "QuickMine";
    }

    @Override
    public ItemType getItemType() {
        return ItemType.NULL;
    }

    @Override
    public float getCooldown() {
        return 60;
    }

    @Override
    public void afterConstruction() {
        StatusApplier.getOrNew(getPlayer()).applyStatus(Status.HASTE, Integer.MAX_VALUE, 2, true);
    }
}
