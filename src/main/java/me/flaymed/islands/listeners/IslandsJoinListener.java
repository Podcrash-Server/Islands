package me.flaymed.islands.listeners;

import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.GameState;
import com.podcrash.api.kits.KitPlayerManager;
import com.podcrash.api.listeners.ListenerBase;
import me.flaymed.islands.kits.classes.Berserker;
import me.flaymed.islands.kits.classes.Medic;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class IslandsJoinListener extends ListenerBase {
    public IslandsJoinListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Medic medic = new Medic(player);
        KitPlayerManager.getInstance().addKitPlayer(medic);

        if(GameManager.getGame() != null) {
            if (GameManager.getGame().getGameState() == GameState.STARTED || GameManager.getGame().isFull()) {
                if(GameManager.getGame().isParticipating(player)) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(GameManager.getGame().getTeam(player).getSpawn(player));
                } else {
                    GameManager.addSpectator(player);
                }
            } else {
                player.setGameMode(GameMode.ADVENTURE);
                GameManager.addPlayer(player);
            }
        }
    }
}
