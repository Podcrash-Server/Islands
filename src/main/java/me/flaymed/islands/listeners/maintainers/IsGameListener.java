package me.flaymed.islands.listeners.maintainers;

import com.podcrash.api.db.redis.Communicator;
import com.podcrash.api.mc.economy.Currency;
import com.podcrash.api.mc.economy.IEconomyHandler;
import com.podcrash.api.mc.effect.status.Status;
import com.podcrash.api.mc.effect.status.StatusApplier;
import com.podcrash.api.mc.events.game.GameEndEvent;
import com.podcrash.api.mc.events.game.GameStartEvent;
import com.podcrash.api.mc.game.Game;
import com.podcrash.api.mc.game.GameManager;
import com.podcrash.api.mc.listeners.ListenerBase;
import com.podcrash.api.plugin.Pluginizer;
import me.flaymed.islands.Main;
import me.flaymed.islands.game.IsGame;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.IslandsPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class IsGameListener extends ListenerBase {
    public IsGameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onStart(GameStartEvent e) {
        Game game = e.getGame();
        game.broadcast(game.toString());
        Main.getInstance().getLogger().info("game is " + game);
        if (e.getGame().getPlayerCount() < 1) {
            Main.instance.getLogger().info(String.format("Can't start game %d, not enough players!", game.getId()));
        }
        String startingMsg = String.format("Game %d is starting up with map %s", e.getGame().getId(), e.getGame().getMapName());
        for(Player p : e.getGame().getBukkitPlayers()) p.sendMessage(startingMsg);

        game.sendColorTab(false);

        game.broadcast(e.getMessage());

        for(Player p: game.getBukkitPlayers()) {
            IslandsPlayer player = IslandsPlayerManager.getInstance().getIslandsPlayer(p);
            StatusApplier.getOrNew(p).removeStatus(Status.values());
        }
    }


    @EventHandler
    public void onEnd(GameEndEvent e) {
        double payout = 500;

        Communicator.publishLobby(Communicator.getCode() + " close");
        IsGame game1 = new IsGame(GameManager.getCurrentID(), Long.toString(System.currentTimeMillis()));
        IEconomyHandler handler = Pluginizer.getSpigotPlugin().getEconomyHandler();
        for(Player player : e.getGame().getBukkitPlayers()) {
            if(GameManager.isSpectating(player)) break;
            handler.pay(player, payout);
            player.sendMessage(String.format("%s%sYou earned %s %s!", Currency.GOLD.getFormatting(), ChatColor.BOLD, payout, Currency.GOLD.getName()));
        }


        GameManager.destroyCurrentGame();
        GameManager.createGame(game1);

        for(Player player : Bukkit.getOnlinePlayers()) {
            GameManager.addPlayer(player);
            StatusApplier.getOrNew(player).removeStatus(Status.values());
        }

    }
}
