package me.flaymed.islands.kits.skills.berserker;

import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class Leap extends Instant implements ICooldown {

    private int uses = 0;
    private int charges = 8;
    private int hits = 0;


    public Leap() {

    }

    @Override
    public float getCooldown() {
        return 15;
    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {

        Player player = this.getPlayer();

        if (onCooldown()) {
            this.getPlayer().sendMessage(getCooldownMessage());
            return;
        } else {
            if (charges - uses > 0) {
                Material m = player.getLocation().getBlock().getType();
                Vector v;

                if (m.equals(Material.STATIONARY_WATER) || m.equals(Material.WATER)) {
                    v = player.getLocation().getDirection().multiply(2);
                } else {
                    v = player.getLocation().getDirection().multiply(1.5);
                }

                player.setVelocity(v);
                player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 8, 1);
                uses += 1;

                player.sendMessage(String.format("%sSkill>%s You used %sBerserker Leap%s! You now have %s %s %s charges left!", ChatColor.BLUE, ChatColor.GRAY, ChatColor.GREEN, ChatColor.GRAY, ChatColor.BLUE, charges - uses, ChatColor.GRAY));
            } else {
                player.sendMessage(String.format("%sSkill>%s You have used all of your available leaps!", ChatColor.BLUE, ChatColor.GRAY));
            }
            setLastUsed(System.currentTimeMillis());
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public String getName() {
        return "Berserker Leap";
    }

    @Override
    public ItemType getItemType() {
        return ItemType.AXE;
    }

    @EventHandler
    public void playerHit(DamageApplyEvent e) {
        if (e.getAttacker() == this.getPlayer()) {
            hits += 1;

            if (hits >= 3) {
                uses -= 1;
                hits = 0;
                e.getAttacker().sendMessage(String.format("%sSkill> %sYou now have %s%s charges%s!", ChatColor.BLUE, ChatColor.GRAY, ChatColor.BLUE, charges, ChatColor.GRAY));
            }
        }
    }
}
