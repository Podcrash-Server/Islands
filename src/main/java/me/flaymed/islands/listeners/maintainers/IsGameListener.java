package me.flaymed.islands.listeners.maintainers;

import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.db.redis.Communicator;
import com.podcrash.api.mc.economy.Currency;
import com.podcrash.api.mc.economy.IEconomyHandler;
import com.podcrash.api.mc.effect.status.Status;
import com.podcrash.api.mc.effect.status.StatusApplier;
import com.podcrash.api.mc.events.game.GameEndEvent;
import com.podcrash.api.mc.events.game.GameMapLoadEvent;
import com.podcrash.api.mc.events.game.GameStartEvent;
import com.podcrash.api.mc.game.Game;
import com.podcrash.api.mc.game.GameManager;
import com.podcrash.api.mc.listeners.ListenerBase;
import com.podcrash.api.plugin.Pluginizer;
import com.sun.tools.javac.file.Locations;
import me.flaymed.islands.Main;
import me.flaymed.islands.game.IsGame;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.IslandsPlayerManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class IsGameListener extends ListenerBase {
    public IsGameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void mapLoad(GameMapLoadEvent e) {
        Game game = e.getGame();
        IslandsMap map = (IslandsMap) e.getMap();
        placeOres(game.getGameWorld(), map.getRedOres());
        placeOres(game.getGameWorld(), map.getBlueOres());
        placeOres(game.getGameWorld(), map.getYellowOres());
        placeOres(game.getGameWorld(), map.getGreenOres());

        stockChests(game.getGameWorld(), map.getChests());

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

    private void placeOres(World world, List<Location> locations) {

        for (Location location : locations) {
            double random = Math.random();
            //10% chance of stone
            if (random <= 0.05) {
                world.getBlockAt(location).setType(Material.STONE);
                continue;
            }

            if(random <= 0.20) {
                world.getBlockAt(location).setType(Material.COAL_ORE);
                continue;
            }

            if (random <= 0.60) {
                world.getBlockAt(location).setType(Material.IRON_ORE);
                continue;
            }

            if (random <= 0.80) {
                world.getBlockAt(location).setType(Material.GOLD_ORE);
                continue;
            }

            if (random <= 1) {
                world.getBlockAt(location).setType(Material.DIAMOND_ORE);
                continue;
            }

        }

    }

    private void stockChests(World world, List<Location> chests) {

        ItemStack[] items = {new ItemStack(Material.IRON_SWORD), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.MUSHROOM_SOUP), new ItemStack(Material.DIAMOND_LEGGINGS),
        new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.DIAMOND_AXE), new ItemStack(Material.BOW),
        new ItemStack(Material.ARROW), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS),
        new ItemStack(Material.IRON_AXE)};

        for (Location chestLoc : chests) {
            Random rand = new Random();
            Block block = world.getBlockAt(chestLoc);
            if (block.getType() == Material.CHEST) {
                Chest chest = (Chest) block;
                Inventory chestInventory = chest.getInventory();
                int itemAmount = (int) (Math.random() * ((6 - 3) + 1)) + 3;
                for (int i = 0; i < itemAmount; i++) {
                    int slot = rand.nextInt(27);
                    ItemStack item = items[rand.nextInt(items.length)];

                    if (item.equals(Material.ARROW)) {
                        //Between 10 to 32 arrows
                        int arrowAmount = (int) (Math.random() * ((32 - 10) + 1)) + 10;
                        for (int a = 0; a < arrowAmount; a++) {
                            chestInventory.setItem(slot, item);
                        }
                        continue;
                    }

                    chestInventory.setItem(slot, item);

                }
            }
        }
    }

}
