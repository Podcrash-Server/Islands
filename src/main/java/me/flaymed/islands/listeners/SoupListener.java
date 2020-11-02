package me.flaymed.islands.listeners;

import com.packetwrapper.abstractpackets.WrapperPlayServerWorldEvent;
import me.flaymed.islands.Islands;
import me.flaymed.islands.util.IslandsParticleGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoupListener extends ListenerBase {
    public SoupListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void click(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        ItemStack item = e.getItem();
        Player p = e.getPlayer();
        Action action = e.getAction();
        if (action == Action.PHYSICAL) return;
        if (item.getType() != Material.MUSHROOM_SOUP) return;

        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 3));
        Location location = p.getEyeLocation();
        p.playSound(location, "random.eat", 0.85F, 2);
        WrapperPlayServerWorldEvent red = IslandsParticleGenerator.createBlockEffect(location.toVector(), Material.RED_MUSHROOM.getId());
        WrapperPlayServerWorldEvent brown = IslandsParticleGenerator.createBlockEffect(location.toVector(), Material.BROWN_MUSHROOM.getId());

        for(Player player : p.getWorld().getPlayers()) {
            red.sendPacket(player);
            brown.sendPacket(player);
        }
        removeItemFromHand(p);
        p.updateInventory();
    }

    private void removeItemFromHand(Player player) {
        ItemStack item = player.getItemInHand();
        int slot = player.getInventory().getHeldItemSlot();
        int amnt = item.getAmount();
        if(amnt > 1) {
            item.setAmount(amnt - 1);
        }else {
            PlayerInventory inventory = player.getInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Islands.getInstance(), () -> inventory.clear(slot), 1);
        }
        player.updateInventory();
    }
}
