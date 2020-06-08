package me.flaymed.islands;

import com.comphenix.protocol.ProtocolLibrary;
import com.podcrash.api.game.GameManager;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.listeners.InventoryListener;
import me.flaymed.islands.listeners.IslandsJoinListener;
import me.flaymed.islands.listeners.KitApplyListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;

        IslandsGame game = new IslandsGame(GameManager.getCurrentID(), Long.toString(System.currentTimeMillis()));
        GameManager.createGame(game);

        registerListeners();
    }

    public void registerListeners() {
        new InventoryListener(this);
        new IslandsJoinListener(this);
        new KitApplyListener(this);
    }
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelAllTasks();
        GameManager.destroyCurrentGame();
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
