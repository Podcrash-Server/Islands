package me.flaymed.islands.kits;

import me.flaymed.islands.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class IslandsPlayerManager {

    private static volatile IslandsPlayerManager ipm;
    private JavaPlugin plugin = Main.getInstance();
    private HashMap<String, IslandsPlayer> islandsPlayers = new HashMap<>();

    public static IslandsPlayerManager getInstance() {
        return ipm;
    }

    public void addIslandsPlayer(IslandsPlayer iplayer) {
        if (iplayer == null) return;
        IslandsPlayer oldPlayer = getIslandsPlayer(iplayer.getPlayer());
        removeIslandsPlayer(oldPlayer);

        islandsPlayers.putIfAbsent(iplayer.getPlayer().getName(), iplayer);

    }

    public void removeIslandsPlayer(IslandsPlayer iplayer) {
        Main.getInstance().log.info(iplayer + "");
        if (iplayer == null || !islandsPlayers.containsKey(iplayer.getPlayer().getName())) return;
        Main.getInstance().getLogger().info(String.format("%s Unregistering.", iplayer.getPlayer().getName()));

        islandsPlayers.remove(iplayer.getPlayer().getName());
    }

    public IslandsPlayer getIslandsPlayer(Player player) {
        return islandsPlayers.getOrDefault(player.getName(), null);
    }
}
