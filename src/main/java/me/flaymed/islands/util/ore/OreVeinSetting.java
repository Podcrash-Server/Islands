package me.flaymed.islands.util.ore;

import org.bukkit.Material;

public enum OreVeinSetting {
    STONE(0, 1, 10, 0.5, Material.STONE),
    GRAVEL(2, 6, 10, 0.7, Material.GRAVEL),
    COAL(8, 16, 10, 0.8, Material.COAL_ORE),
    IRON(4, 10, 10, 0.875, Material.IRON_ORE),
    GOLD(4, 10, 10, 0.95, Material.GOLD_ORE),
    DIAMOND(3, 7, 10, 1, Material.DIAMOND_ORE);

    int min, max, tries;
    Material ore;
    double continueChance;

    OreVeinSetting(int min, int max, int tries, double continueChance, Material ore) {
        this.min = min;
        this.max = max;
        this.tries = tries;
        this.continueChance = continueChance;
        this.ore = ore;
    }

    private final static OreVeinSetting[] details = OreVeinSetting.values();
    public static OreVeinSetting[] details() {
        return details;
    }

    public static OreVeinSetting findByOre(Material ore) {
        for (OreVeinSetting setting : details()) {
            if (setting.ore.equals(ore))
                return setting;
        }
        return null;
    }

    public static OreVeinSetting findByName(String name) {
        for (OreVeinSetting setting : details()) {
            if (setting.name().equals(name))
                return setting;
        }
        return null;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getTries() {
        return tries;
    }

    public Material getOre() {
        return ore;
    }

    public double getContinueChance() {
        return continueChance;
    }

    //delete these methodsvvv
    public void setMin(int min) {
        this.min = min;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public void setContinueChance(double chance) {
        this.continueChance = chance;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OreVeinSetting{");
        sb.append("name=").append(name());
        sb.append(", min=").append(min);
        sb.append(", max=").append(max);
        sb.append(", ore=").append(ore);
        sb.append('}');
        return sb.toString();
    }
}
