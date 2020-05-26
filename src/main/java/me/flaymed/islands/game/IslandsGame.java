package me.flaymed.islands.game;

import com.google.common.reflect.ClassPath;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.game.*;
import com.podcrash.api.game.objects.ItemObjective;
import com.podcrash.api.game.objects.WinObjective;
import me.flaymed.islands.annotations.BridgeType;
import me.flaymed.islands.bridges.maker.BridgeGenerator;
import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.game.scoreboard.IslandsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class IslandsGame extends Game {
    private GameStage stage;
    private BridgeGenerator bridgeGenerator;
    private String bridgeType;

    public IslandsGame(int id, String name) {
        super(id, name, GameType.DOM);
        this.board = new IslandsScoreboard(id);
        setStage(GameStage.PREPARE);
    }

    @Override
    public int getAbsoluteMinPlayers() {
        return 6;
    }

    @Override
    public void leaveCheck() {

    }

    @Override
    public Class<? extends com.podcrash.api.db.pojos.map.GameMap> getMapClass() {
        return IslandsMap.class;
    }

    @Override
    public TeamSettings getTeamSettings() {
        return new TeamSettings.Builder()
            .setTeamColors(TeamEnum.RED, TeamEnum.BLUE)
            .setMax(12)
            .setMin(6)
            .build();
    }

    @Override
    public String getMode() {
        return "Islands";
    }

    public GameStage getStage() {
        return stage;
    }

    @Override
    public String getPresentableResult() {
        return null;
    }

    public void setStage(GameStage stage) {
        this.stage = stage;
        Bukkit.getPluginManager().callEvent(new GameStageEvent(this, this.stage));
    }
    @Override
    public List<WinObjective> getWinObjectives() {
        return Collections.emptyList();
    }

    @Override
    public List<ItemObjective> getItemObjectives() {
        return Collections.emptyList();
    }

    public void consumeChest(Consumer<Chest> blockConsumer) {
        List<Point> chestPoints = ((IslandsMap) this.getMap()).getChests();
        for (Point point : chestPoints) {
            Block block = getGameWorld().getBlockAt((int) point.getX(), (int) point.getY(), (int) point.getZ());
            if (!(block instanceof Chest))
                throw new IllegalStateException("the block found is not a chest! " + point);
            blockConsumer.accept((Chest) block);
        }
    }

    public void consumeOre(Consumer<Block> blockConsumer) {
        List<Point> orePoints = ((IslandsMap) this.getMap()).getChests();
        final Set<Material> ores = new HashSet<>(Arrays.asList(Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.GOLD_ORE));
        for (Point point : orePoints) {
            Block block = getGameWorld().getBlockAt((int) point.getX(), (int) point.getY(), (int) point.getZ());
            Material possOre = block.getType();
            if (!ores.contains(possOre))
                continue;
            blockConsumer.accept(block);
        }
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
        try {
            makeBridgeGenerator(bridgeType);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void makeBridgeGenerator(String bridgeType) throws IOException, ClassNotFoundException {
        String path = "me.flaymed.islands.bridges.maker";
        ClassPath cp = ClassPath.from(BridgeGenerator.class.getClassLoader());
        Set<ClassPath.ClassInfo> bridgeClassSet = cp.getTopLevelClasses(path);

        for (ClassPath.ClassInfo bridgeInfo : bridgeClassSet) {
            Class<?> bridgeClass = Class.forName(bridgeInfo.getName());
            if (!bridgeClass.isAnnotationPresent(BridgeType.class))
                continue;
            BridgeType annotation = bridgeClass.getAnnotation(BridgeType.class);
            boolean isSameType = annotation.type().equalsIgnoreCase(bridgeType);
            if (!isSameType)
                continue;
            this.bridgeGenerator = (BridgeGenerator) emptyConstructor(bridgeClass);
        }
    }

    public BridgeGenerator getBridgeGenerator() {
        return bridgeGenerator;
    }

    private static <T> T emptyConstructor(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        }catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
