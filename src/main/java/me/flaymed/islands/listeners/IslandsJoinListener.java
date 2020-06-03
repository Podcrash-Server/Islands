package me.flaymed.islands.listeners;

import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.GameState;
import com.podcrash.api.kits.KitPlayerManager;
import com.podcrash.api.listeners.ListenerBase;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.kits.classes.Berserker;
import me.flaymed.islands.kits.classes.Medic;
import me.flaymed.islands.util.ore.OreVeinSetting;
import me.flaymed.islands.util.ore.VeinBuilder;
import me.flaymed.islands.util.ore.VeinGen;
import me.flaymed.islands.kits.classes.Archer;
import me.flaymed.islands.kits.classes.Berserker;
import me.flaymed.islands.kits.classes.Bomber;
import me.flaymed.islands.kits.classes.Medic;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class IslandsJoinListener extends ListenerBase {
    public IslandsJoinListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Archer archer = new Archer(player);
        KitPlayerManager.getInstance().addKitPlayer(archer);

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

    @EventHandler
    public void interactTest(PlayerInteractEvent e) {
        if (GameManager.getGame() != null && GameManager.getGame().getGameState() == GameState.STARTED)
            return;
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        Material ore = item.getType();
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK)
            return;
        OreVeinSetting setting = OreVeinSetting.findByOre(ore);
        if (setting == null)
            return;
        e.setCancelled(true);

        VeinGen generator = VeinGen.fromOreSetting(setting);
        generator.startLocation(e.getClickedBlock().getLocation());
        generator.generate();

        player.sendMessage("Generated " + generator.getOreGenerated() + " of " + ore);
    }
}
