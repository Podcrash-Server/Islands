package me.flaymed.islands.listeners;

import com.podcrash.api.db.TableOrganizer;
import com.podcrash.api.db.tables.DataTableType;
import com.podcrash.api.db.tables.PlayerTable;
import com.podcrash.api.mc.game.GameManager;
import com.podcrash.api.mc.listeners.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerJoin extends ListenerBase {

    public PlayerJoin (JavaPlugin plugin) {
        super(plugin);
    }

    private void putPlayerDB(UUID uuid) {
        PlayerTable players = TableOrganizer.getTable(DataTableType.PLAYERS);
        players.insert(uuid);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (GameManager.getGame().isOngoing() || GameManager.getGame().isFull()) {
            if(GameManager.getGame().contains(p))
                p.teleport(GameManager.getGame().getTeam(p).getSpawn(p));
            else GameManager.addSpectator(p);
        } else {
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            GameManager.addPlayer(p);
        }

        putPlayerDB(p.getUniqueId());

    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        //Player Leave the Game

    }

}
