package me.flaymed.islands;

import com.comphenix.protocol.ProtocolLibrary;
import com.podcrash.gamecore.utils.MathUtil;
import me.flaymed.islands.commands.OreSettingCommand;
import me.flaymed.islands.commands.DropBridgeCommand;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.inventory.KitSelectGUI;
import me.flaymed.islands.kits.classes.*;
import me.flaymed.islands.listeners.*;
import me.flaymed.islands.teams.IslandsTeam;
import me.flaymed.islands.teams.IslandsTeamSide;
import me.flaymed.islands.tracker.CoordinateTracker;
import me.flaymed.islands.world.IslandsMap;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.List;

public class Islands extends JavaPlugin {
    private static Islands INSTANCE;
    private List<IslandsTeam> teams;
    private IslandsGame game;
    private Inventory teamSelectInventory;
    private CoordinateTracker coordinateTracker;
    private static Config config;

    @Override
    public void onEnable() {
        INSTANCE = this;
        game = new IslandsGame();
        config = new Config();
        coordinateTracker = new CoordinateTracker(this);
        registerCommands();
        registerListeners();

        //This is done in the Islands class not in game class to ensure the inventories exist before players join...
        //selectMap();
        createEssentials();

        //TODO: CREATE ISLANDS MAPS

    }

    public void registerListeners() {
        new IslandsJoinListener(this);
        new IslandsGameListener(this);
        new IslandsPlayerRestrict(this);
        new CompassTrackerListener(this);
        new SoupListener(this);
    }

    private void createEssentials() {
        //Method is very overloaded because well it has to be really....
        Inventory teamSelect = Bukkit.createInventory(null, 9, "Select A Team");

        ItemStack redWool = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
        ItemMeta redMeta = redWool.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Red Team");
        redWool.setItemMeta(redMeta);

        ItemStack blueWool = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData());
        ItemMeta blueMeta = blueWool.getItemMeta();
        blueMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team");
        blueWool.setItemMeta(blueMeta);

        ItemStack greenWool = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
        ItemMeta greenMeta = greenWool.getItemMeta();
        greenMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Green Team");
        greenWool.setItemMeta(greenMeta);

        ItemStack yellowWool = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData());
        ItemMeta yellowMeta = yellowWool.getItemMeta();
        yellowMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Yellow Team");
        yellowWool.setItemMeta(yellowMeta);

        teamSelect.setItem(0, redWool);
        teamSelect.setItem(1, blueWool);
        teamSelect.setItem(2, greenWool);
        teamSelect.setItem(3, yellowWool);

        this.teamSelectInventory = teamSelect;
        KitSelectGUI kitSelectGUI = new KitSelectGUI(Alchemist.class, Archer.class, Berserker.class, Brawler.class, Bomber.class, Miner.class);

        IslandsTeamSide redSide = new IslandsTeamSide("Red Team", ChatColor.RED, 0);
        IslandsTeam redTeam = new IslandsTeam(redWool, ChatColor.RED);
        redTeam.setSide(redSide);

        IslandsTeamSide blueSide = new IslandsTeamSide("Blue Team", ChatColor.BLUE, 1);
        IslandsTeam blueTeam = new IslandsTeam(blueWool, ChatColor.BLUE);
        blueTeam.setSide(blueSide);

        IslandsTeamSide greenSide = new IslandsTeamSide("Green Team", ChatColor.GREEN, 2);
        IslandsTeam greenTeam = new IslandsTeam(greenWool, ChatColor.GREEN);
        greenTeam.setSide(greenSide);

        IslandsTeamSide yellowSide = new IslandsTeamSide("Yellow Team", ChatColor.YELLOW, 3);
        IslandsTeam yellowTeam = new IslandsTeam(yellowWool, ChatColor.YELLOW);
        yellowTeam.setSide(yellowSide);

        this.teams = Arrays.asList(redTeam, blueTeam, greenTeam, yellowTeam);
    }


    private void registerCommands() {
        getCommand("ore").setExecutor(new OreSettingCommand());
        getCommand("bridges").setExecutor(new DropBridgeCommand());
    }

    private void selectMap() {
        String worldName = config.getWorldNames().get(MathUtil.randomInt(config.getWorldNames().size()));
        World islandsWorld = Bukkit.createWorld(new WorldCreator(worldName));
        IslandsMap isMap = new IslandsMap(islandsWorld);
        game.setIslandsMap(isMap);
        game.setGameWorldName(islandsWorld);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelAllTasks();
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

    public IslandsGame getGame() {
        return game; //no npe check because you'll never need to get game instance before plugin is enabled.
    }

    public List<IslandsTeam> getTeams() {
        if (teams == null) createEssentials();
        return teams;
    }

    public IslandsTeam getTeamBySide(int side) {
        for (IslandsTeam team : getTeams()) {
            if (team == null) return null;
            if (team.getSide().side() == side) return team;
        }
        return null;
    }

    public Inventory getTeamSelectInventory() {
        if (teamSelectInventory == null) createEssentials();
        return teamSelectInventory;
    }

    public static Config getIsConfig() {
        return config;
    }

    public static Islands getInstance() {
        return INSTANCE;
    }

    public CoordinateTracker getCoordinateTracker() {
        return coordinateTracker;
    }
}
