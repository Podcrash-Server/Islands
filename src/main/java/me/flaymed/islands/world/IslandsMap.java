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
        this.entitySpawnLocations = new ArrayList<>();
        this.greenOres = new ArrayList<>();
        this.yellowOres = new ArrayList<>();
        this.blueOres = new ArrayList<>();
        this.redOres = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.world = world;
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

        //TODO: ore parse
        //TODO: bridge parse
        //TODO: chest parse
        //TODO: animal parse
        //TODO: parseTeamBorders
        //player spawn parse
        parsePlayerSpawns(worldData);

        //middle
        parseMiddle(worldData);

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

        //Red
        List<String> redSpawnPoints = worldData.getStringList("RedSpawns");
        for (String redSpawnPoint : redSpawnPoints) {
            String[] spawnPointParts = redSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam redTeam = islands.getTeamBySide(0);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            redTeam.addSpawnPointLocation(spawnLocation);
        }

        //Blue
        List<String> blueSpawnPoints = worldData.getStringList("BlueSpawns");
        for (String blueSpawnPoint : blueSpawnPoints) {
            String[] spawnPointParts = blueSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam blueTeam = islands.getTeamBySide(1);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            blueTeam.addSpawnPointLocation(spawnLocation);
        }

        //Green
        List<String> greenSpawnPoints = worldData.getStringList("GreenSpawns");
        for (String greenSpawnPoint : greenSpawnPoints) {
            String[] spawnPointParts = greenSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam greenTeam = islands.getTeamBySide(2);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            greenTeam.addSpawnPointLocation(spawnLocation);
        }

        //Yellow
        List<String> yellowSpawnPoints = worldData.getStringList("YellowSpawns");
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
        Point middlePoint = new Point();
        middlePoint.setX(Double.parseDouble(cords[0]));
        middlePoint.setY(Double.parseDouble(cords[1]));
        middlePoint.setZ(Double.parseDouble(cords[2]));
        setMiddle(middlePoint);
    }
}
