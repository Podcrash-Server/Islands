package me.flaymed.islands.listeners;

import com.podcrash.gamecore.kits.KitPlayerManager;
import me.flaymed.islands.Islands;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.classes.LobbyKit;
import me.flaymed.islands.util.ore.OreVeinSetting;
import me.flaymed.islands.util.ore.VeinGen;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class IslandsJoinListener extends ListenerBase {
    public IslandsJoinListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void join(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        IslandsGame game = Islands.getInstance().getGame();
        if (game != null) {
            if (game.isFull()) {
                e.disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full!");
                return;
            }
            if (game.getStage() == GameStage.PREPARE) {
                if (KitPlayerManager.getPlayers().contains(player)) {
                    player.setGameMode(GameMode.SURVIVAL);
                    //Spawn Player on one of their team's spawns
                } else {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game is already in progress!");
                }

                return;
            }

            if (game.getStage() == GameStage.LOBBY) {
                player.setGameMode(GameMode.ADVENTURE);
                IslandsPlayer islandsPlayer = new IslandsPlayer(player);
                islandsPlayer.selectKit(new LobbyKit());
                islandsPlayer.equip();

                return;
            }

            if (game.getStage() == GameStage.FALLEN) e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game is already in progress!");

        }
    }

    //idek what this is
    @EventHandler
    public void interactTest(PlayerInteractEvent e) {
        if (Islands.getInstance().getGame() != null && Islands.getInstance().getGame().getStage() != GameStage.LOBBY) return;
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        Material ore = item.getType();
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;
        OreVeinSetting setting = OreVeinSetting.findByOre(ore);
        if (setting == null) return;
        e.setCancelled(true);

        VeinGen generator = VeinGen.fromOreSetting(setting);
        generator.startLocation(e.getClickedBlock().getLocation());
        generator.generate();

        player.sendMessage("Generated " + generator.getOreGenerated() + " of " + ore);
    }
}
