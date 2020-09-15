package me.flaymed.islands.inventory;

import me.flaymed.islands.kits.IslandsPlayer;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;

public class KitSelectGUI {

    private static KitSelectGUI INSTANCE;
    private List<IslandsPlayer> baseKits;
    private Inventory kitSelectInventory;

    public KitSelectGUI(Class<? extends IslandsPlayer>... baseKits) {
        INSTANCE = this;

        createBaseKits(baseKits);
        createInventory();
    }

    private void createBaseKits(Class<? extends IslandsPlayer>... kits) {
        List<IslandsPlayer> kitList = new ArrayList<>();
        for (Class<? extends IslandsPlayer> kit : kits) {
            try {
                IslandsPlayer baseKit = kit.newInstance();
                kitList.add(baseKit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.baseKits = kitList;
    }

    private void createInventory() {

    }

    public static KitSelectGUI getInstance() {
        return INSTANCE;
    }

}
