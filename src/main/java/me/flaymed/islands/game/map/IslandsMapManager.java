package me.flaymed.islands.game.map;

import me.flaymed.islands.Main;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

public class IslandsMapManager {

    private Main mstance;
    private static volatile IslandsMapManager islandsMapManager;

    private IslandsMapManager() {
        if (islandsMapManager != null) {
            throw new RuntimeException("Please use getInstance() method");
        }
        mstance = Main.getInstance();
    }

    public static IslandsMapManager getInstance() {
        if (islandsMapManager == null) {
            synchronized (IslandsMapManager.class) {
                if (islandsMapManager == null) {
                    islandsMapManager = new IslandsMapManager();
                }
            }

        }
        return islandsMapManager;
    }

    public boolean registerSpawn(Player p, Block block, Wool wool) {

        if (wool == null) return false;
        if (!wool.getColor().equals(DyeColor.BLUE) && !wool.getColor().equals(DyeColor.RED) && !wool.getColor().equals(DyeColor.YELLOW) && !wool.getColor().equals(DyeColor.GREEN)) return false;

        int index;

        switch (wool.getColor()) {
            case RED:
                index = 0;
                break;

            case BLUE:
                index = 1;
                break;

            case YELLOW:
                index = 2;
                break;

            case GREEN:
                index = 3;
                break;
        }

        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();

        //TODO: Add the spawn point into the MapManager
        return true;
    }

}
