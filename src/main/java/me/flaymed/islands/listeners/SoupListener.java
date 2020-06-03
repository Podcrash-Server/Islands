package me.flaymed.islands.listeners;

import com.packetwrapper.abstractpackets.WrapperPlayServerWorldEvent;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.listeners.ListenerBase;
import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.sound.SoundPlayer;
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

        StatusApplier.getOrNew(p).applyStatus(Status.REGENERATION, 4, 1, true, true);
        Location location = p.getEyeLocation();
        SoundPlayer.sendSound(location, "random.eat", 0.85F, 63, null);
        WrapperPlayServerWorldEvent red = ParticleGenerator.createBlockEffect(location, Material.RED_MUSHROOM.getId());
        WrapperPlayServerWorldEvent brown = ParticleGenerator.createBlockEffect(location, Material.BROWN_MUSHROOM.getId());

        for(Player v : p.getWorld().getPlayers()) ParticleGenerator.generate(v, red, brown);
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(PodcrashSpigot.getInstance(), () -> inventory.clear(slot), 1);
        }
        player.updateInventory();
    }
}
