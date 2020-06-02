package me.flaymed.islands.listeners;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.podcrash.api.listeners.ListenerBase;
import com.podcrash.api.time.TimeHandler;
import com.podcrash.api.time.resources.TimeResource;
import com.podcrash.api.util.TitleSender;
import me.flaymed.islands.util.CompassTracker;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CompassTrackerListener extends ListenerBase {
    public CompassTrackerListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void holdCompass(PlayerItemHeldEvent e) {
        int switchSlot = e.getNewSlot();
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack stack = inventory.getItem(switchSlot);
        if (stack == null)
            return;
        if (stack.getType() != Material.COMPASS)
            return;
        TimeHandler.repeatedTimeAsync(10, 0, new TimeResource() {
            @Override
            public void task() {
                CompassTracker.CompassResult result = CompassTracker.findCompassResult(player);
                WrappedChatComponent component = TitleSender.writeTitle("Closest Player: " + result.getPlayer().getName() + " Distance: " + result.getDistance());
                TitleSender.sendTitle(player, component);
                player.setCompassTarget(result.getLocation().toLocation(player.getWorld()));
            }

            @Override
            public boolean cancel() {
                return player.getItemInHand().getType() != Material.COMPASS;
            }

            @Override
            public void cleanup() {
                TitleSender.sendTitle(player, TitleSender.emptyTitle());
            }
        });
    }
}
