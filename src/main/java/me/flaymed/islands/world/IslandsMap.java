package me.flaymed.islands.world;

import me.flaymed.islands.Islands;
import me.flaymed.islands.bridges.data.BridgePoint;
import me.flaymed.islands.location.IDPoint2Point;
import me.flaymed.islands.location.Point;
import me.flaymed.islands.teams.IslandsTeam;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class IslandsMap {
    private List<Point> redBorder;
    private List<Point> blueBorder;
    private List<Point> greenBorder;
    private List<Point> yellowBorder;
    private List<Location> entitySpawnLocations;
    private List<Point> greenOres;
    private List<Point> yellowOres;
    private List<Point> blueOres;
    private List<Point> redOres;
    private List<Point> chests;
    private List<BridgePoint> bridgeData;
    private Point middle;
    private String bridgeType;
    private List<IDPoint2Point> bridges;
    private World world;

    public IslandsMap(World world) {
        super();
        this.redBorder = new ArrayList<>();
        this.blueBorder = new ArrayList<>();
        this.greenBorder = new ArrayList<>();
        this.yellowBorder = new ArrayList<>();
        this.entitySpawnLocations = new ArrayList<>();
        this.greenOres = new ArrayList<>();
        this.yellowOres = new ArrayList<>();
        this.blueOres = new ArrayList<>();
        this.redOres = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.world = world;

        fillData();
    }

    public List<Location> getEntitySpawnLocations() {
        return entitySpawnLocations;
    }

    public List<Point> getGreenOres() {
        return greenOres;
    }

    public void setGreenOres(List<Point> greenOres) {
        this.greenOres = greenOres;
    }

    public List<Point> getYellowOres() {
        return yellowOres;
    }

    public void setYellowOres(List<Point> yellowOres) {
        this.yellowOres = yellowOres;
    }

    public List<Point> getBlueOres() {
        return blueOres;
    }

    public void setBlueOres(List<Point> blueOres) {
        this.blueOres = blueOres;
    }

    public List<Point> getRedOres() {
        return redOres;
    }

    public void setRedOres(List<Point> redOres) {
        this.redOres = redOres;
    }

    public List<Point> getChests() {
        return chests;
    }

    public void setChests(List<Point> chests) {
        this.chests = chests;
    }

    public Point getMiddle() {
        return middle;
    }

    public void setMiddle(Point middle) {
        this.middle = middle;
    }

    public List<IDPoint2Point> getBridges() {
        return bridges;
    }

    public void setBridges(List<IDPoint2Point> bridges) {
        this.bridges = bridges;
    }

    public List<BridgePoint> getBridgeData() {
        return bridgeData;
    }

    public void setBridgeData(List<BridgePoint> bridgeData) {
        this.bridgeData = bridgeData;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }

    private void fillData() {
        ConfigurationSection worldData = Islands.getIsConfig().getWorldData(world.getName());

        String bridgeType = worldData.getString("bridgetype");
        setBridgeType(bridgeType);
        Islands.getInstance().getGame().setBridgeType(bridgeType);

        //TODO: parseWorldBorder
        parseTeamBorder(worldData);
        //TODO: ore parse
        //TODO: bridge parse

        parseChestLocations(worldData);
        parseAnimalSpawns(worldData);

        parsePlayerSpawns(worldData);
        parseMiddle(worldData);

    }

    private void parseTeamBorder(ConfigurationSection worldData) {
        List<String> redBorderPoints = worldData.getStringList("RedSpawns");
        List<String> blueBorderPoints = worldData.getStringList("BlueSpawns");
        List<String> greenBorderPoints = worldData.getStringList("GreenSpawns");
        List<String> yellowBorderPoints = worldData.getStringList("YellowSpawns");

        //Red
        for (String redBorderPoint : redBorderPoints) {
            String[] borderPointParts = redBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            redBorder.add(borderPoint);
        }

        //Blue
        for (String blueBorderPoint : blueBorderPoints) {
            String[] borderPointParts = blueBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            blueBorder.add(borderPoint);
        }

        //Green
        for (String greenBorderPoint : greenBorderPoints) {
            String[] borderPointParts = greenBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            greenBorder.add(borderPoint);
        }

        //Yellow
        for (String yellowBorderPoint : yellowBorderPoints) {
            String[] borderPointParts = yellowBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            yellowBorder.add(borderPoint);
        }

    }

    private void parseChestLocations(ConfigurationSection worldData) {
        List<String> chestSpawnPoints = worldData.getStringList("Chests");
        List<Point> chestPoints = new ArrayList<>();
        for (String chestSpawnPoint : chestSpawnPoints) {
            String[] chestPointParts = chestSpawnPoint.split(",");
            Point chestPoint = new Point(Double.parseDouble(chestPointParts[0]), Double.parseDouble(chestPointParts[1]), Double.parseDouble(chestPointParts[2]));
            chestPoints.add(chestPoint);
        }
        setChests(chestPoints);
    }

    private void parseAnimalSpawns(ConfigurationSection worldData) {
        List<String> entitySpawnPoints = worldData.getStringList("EntitySpawns");
        for (String entitySpawnPoint : entitySpawnPoints) {
            String[] spawnPointParts = entitySpawnPoint.split(",");
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            this.entitySpawnLocations.add(spawnLocation);
        }

    }

    private void parsePlayerSpawns(ConfigurationSection worldData) {
        Islands islands = Islands.getInstance();
        List<String> redSpawnPoints = worldData.getStringList("RedSpawns");
        List<String> blueSpawnPoints = worldData.getStringList("BlueSpawns");
        List<String> greenSpawnPoints = worldData.getStringList("GreenSpawns");
        List<String> yellowSpawnPoints = worldData.getStringList("YellowSpawns");

        //Red
        for (String redSpawnPoint : redSpawnPoints) {
            String[] spawnPointParts = redSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam redTeam = islands.getTeamBySide(0);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            redTeam.addSpawnPointLocation(spawnLocation);
        }

        //Blue
        for (String blueSpawnPoint : blueSpawnPoints) {
            String[] spawnPointParts = blueSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam blueTeam = islands.getTeamBySide(1);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            blueTeam.addSpawnPointLocation(spawnLocation);
        }

        //Green
        for (String greenSpawnPoint : greenSpawnPoints) {
            String[] spawnPointParts = greenSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam greenTeam = islands.getTeamBySide(2);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            greenTeam.addSpawnPointLocation(spawnLocation);
        }

        //Yellow
        for (String yellowSpawnPoint : yellowSpawnPoints) {
            String[] spawnPointParts = yellowSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam yellowTeam = islands.getTeamBySide(3);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            yellowTeam.addSpawnPointLocation(spawnLocation);
        }
    }

    private void parseMiddle(ConfigurationSection worldData) {
        String middleData = worldData.get("middle").toString();
        String[] cords = middleData.split(",");
        Point middlePoint = new Point(Double.parseDouble(cords[0]), Double.parseDouble(cords[1]), Double.parseDouble(cords[2]));
        setMiddle(middlePoint);
    }
}
