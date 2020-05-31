package me.flaymed.islands;

import com.comphenix.protocol.ProtocolLibrary;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.command.OreSettingCommand;
import me.flaymed.islands.command.DropBridgeCommand;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.listeners.IslandsGameListener;
import me.flaymed.islands.listeners.IslandsJoinListener;
import me.flaymed.islands.listeners.KitApplyListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;

        PodcrashSpigot spigot =  (PodcrashSpigot) Bukkit.getPluginManager().getPlugin("PodcrashAPI");
        spigot.registerCommand(new OreSettingCommand());
        IslandsGame game = new IslandsGame(GameManager.getCurrentID(), Long.toString(System.currentTimeMillis()));
        GameManager.createGame(game);

        registerListeners();
        PodcrashSpigot.getInstance().registerCommand(new DropBridgeCommand());
    }

    public void registerListeners() {
        new IslandsJoinListener(this);
        new KitApplyListener(this);
        new IslandsGameListener(this);
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
