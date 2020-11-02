package me.flaymed.islands.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.flaymed.islands.Islands;
import me.flaymed.islands.util.CompassTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class CompassTrackerListener extends ListenerBase {

    HashMap<UUID, Integer> compassMap;

    public CompassTrackerListener(JavaPlugin plugin) {
        super(plugin);
        this.compassMap = new HashMap<>();
    }

    @EventHandler
    public void holdCompass(PlayerItemHeldEvent e) {
        int switchSlot = e.getNewSlot();
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack stack = inventory.getItem(switchSlot);
        if (stack == null)
            return;
        if (stack.getType() != Material.COMPASS) {
            if (this.compassMap.containsKey(player.getUniqueId())) {
                int taskid = this.compassMap.get(player.getUniqueId());
                Bukkit.getScheduler().cancelTask(taskid);
                sendTitle(player, writeTitle(""));
                this.compassMap.remove(player.getUniqueId());
            }
            return;
        }

        int taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Islands.getInstance(), () -> {
            CompassTracker.CompassResult result = CompassTracker.findCompassResult(player);
            WrappedChatComponent component = writeTitle("Closest Player: " + result.getPlayer().getName() + " Distance: " + result.getDistance());
            sendTitle(player, component);
            player.setCompassTarget(result.getLocation().toLocation(player.getWorld()));
        }, 10, 0);

        this.compassMap.put(player.getUniqueId(), taskid);
    }

    private WrappedChatComponent writeTitle(String string) {
        return WrappedChatComponent.fromJson("{\"text\":\"" + string + "\"}");
    }

    private void sendTitle(Player p, WrappedChatComponent iChatBaseComponent) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.CHAT);
        packet.getChatComponents().write(0, iChatBaseComponent);
        packet.getBytes().write(0, (byte) 2);
        try {
            manager.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
