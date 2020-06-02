package me.flaymed.islands.listeners;

import com.podcrash.api.events.game.GameStartEvent;
import com.podcrash.api.game.Game;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.listeners.ListenerBase;
import com.podcrash.api.world.BlockUtil;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;


public class IslandsPlayerRestrict extends ListenerBase {
    private final Map<UUID, Set<Vector>> first4Communism = new HashMap<>();
    public IslandsPlayerRestrict(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void gameStart(GameStartEvent e) {
        first4Communism.clear();
    }
    @EventHandler
    public void disableAnvil(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ANVIL)
            e.setCancelled(true);
    }

    @EventHandler
    public void disableBlockPlace(BlockPlaceEvent e) {
        IslandsGame game = (IslandsGame) GameManager.getGame();
        if (game.getStage() == GameStage.FALLEN) return;
        boolean cancel = false;
        Material test = e.getBlockReplacedState().getData().getItemType();
        if (test == Material.WATER || test == Material.STATIONARY_WATER) {
            cancel = true;
        } else if (waterUnderneath(e.getBlockPlaced().getLocation())) {
            cancel = true;
        }

        if (cancel) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("Islands> You are not allowed to place blocks above water during the " + game.getStage().toString() + " stage!");
        }
    }

    @EventHandler
    public void anticommunism(BlockPlaceEvent e) {
        Set<Material> privatized = new HashSet<>(Arrays.asList(Material.CHEST, Material.FURNACE));
        if (!privatized.contains(e.getBlockPlaced().getType()))
            return;
        UUID key = e.getPlayer().getUniqueId();
        Set<Vector> locs;
        if ((locs = first4Communism.get(key)) == null) {
            locs = new HashSet<>();
            first4Communism.put(key, locs);
        }
        if (locs.size() >= 4)
            return;
        locs.add(e.getBlockPlaced().getLocation().toVector());
    }

    @EventHandler
    public void disableRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Set<Material> privatized = new HashSet<>(Arrays.asList(Material.CHEST, Material.FURNACE));
        Block block = e.getClickedBlock();
        if (!privatized.contains(block.getType()))
            return;

        Vector vector = block.getLocation().toVector();
        for (Map.Entry<UUID, Set<Vector>> entry : first4Communism.entrySet()) {
            if (entry.getValue().contains(vector)) {
                if (e.getPlayer().getUniqueId() == entry.getKey())
                    return;
                e.setCancelled(true);
                e.getPlayer().sendMessage("Islands> You are not allowed to interact with this block, since it belongs to " + Bukkit.getPlayer(entry.getKey()).getName());
                break;
            }
        }
    }
    public boolean waterUnderneath(Location loc) {
        loc.subtract(0, 1, 0);
        Block block = loc.getBlock();
        while (block.getType() == Material.AIR) {
            if (loc.getY() < 0)
                return true;
            loc.subtract(0, 1, 0);
            block = loc.getBlock();
        }
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }
}
