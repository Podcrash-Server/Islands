package me.flaymed.islands;

import com.comphenix.protocol.ProtocolManager;
import com.podcrash.api.mc.events.TickEvent;
import com.podcrash.api.plugin.Pluginizer;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.commands.*;
import me.flaymed.islands.game.IsGame;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.podcrash.api.mc.game.GameManager;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static volatile Main instance;
    public static final String CHANNEL_NAME = "Islands";
    private ProtocolManager protocolManager;
    private BukkitTask tickTask;
    public Logger log = this.getLogger();
    //Mapping configuration files
    private File mapConfig;
    private FileConfiguration mapConfiguration;
    // permissions HashMap
    private Map<UUID, PermissionAttachment> playerPermissions = new HashMap<>();
    //configurators
    private ExecutorService executor = Executors.newFixedThreadPool(8);


    private CompletableFuture<Void> setUp() {
        return CompletableFuture.runAsync(() -> {
            final PluginManager pman = Bukkit.getPluginManager();
            tickTask = Bukkit.getScheduler().runTaskTimer(instance, () -> pman.callEvent(new TickEvent()), 1L, 1L);

        }, executor);
    }

    @Override
    public void onEnable() {
        log.info("[GameManage] Making game...");
        PodcrashSpigot spigot = Pluginizer.getSpigotPlugin();
        spigot.registerConfigurator("kits");

        IsGame game = new IsGame(GameManager.getCurrentID(), Long.toString(System.currentTimeMillis()));
        GameManager.createGame(game);
        log.info("Created game " + game.getName());

    }

    @Override
    public void onDisable() {
        GameManager.destroyCurrentGame();
    }

    private CompletableFuture<Void> registerCommands() {
        return CompletableFuture.runAsync(() -> {
            getCommand("disguise").setExecutor(new DisguiseCommand());
            getCommand("end").setExecutor(new EndCommand());
            getCommand("leave").setExecutor(new LeaveCommand());


        }, executor);
    }


    public static Main getInstance() {
        return instance;
    }

}
