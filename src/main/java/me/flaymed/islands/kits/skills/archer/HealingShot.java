package me.flaymed.islands.kits.skills.archer;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import me.flaymed.islands.util.IslandsParticleGenerator;
import net.jafama.FastMath;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class HealingShot extends Ability implements Passive, Cooldown {
    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    public String getName() {
        return "Healing Shot";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOW);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getPlayer() != player) return;
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        List<LivingEntity> entities = player.getWorld().getLivingEntities();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1));
        player.sendMessage(getUsedMessage());
        for (LivingEntity entity : entities) {
            if (!(entity instanceof Player)) continue;
            Player player1 = (Player) entity;
            Player player2 = player;
            if (distance(player1.getLocation().getX(), player1.getLocation().getZ(), player2.getLocation().getX(), player2.getLocation().getZ()) > 5.0) continue;
            if (!getKitPlayer().isAlly(player1)) continue;
            player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1));
            IslandsParticleGenerator.particleOverPlayer(player1);
        }
    }

    private double distance(double x1, double z1, double x2, double z2) {
        // Calculating distance
        return FastMath.sqrt(FastMath.pow2(x2 - x1) + FastMath.pow2(z2 - z1));
    }

}
