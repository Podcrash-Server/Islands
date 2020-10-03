package me.flaymed.islands.inventory;

import com.podcrash.api.kits.KitPlayer;
import me.flaymed.islands.kits.IslandsPlayer;
import org.bukkit.Bukkit;
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
        List<IslandsPlayer> kits = getBaseKits();
        this.kitSelectInventory = Bukkit.createInventory(null, 45, "Kit Menu");

        //just for now to test kit selection
        //TODO: Pretty this up
        for (int i = 0; i < kits.size(); i++) {
            KitPlayer kit = kits.get(i);
            this.kitSelectInventory.setItem(i, kit.getInventory().getItem(0));
        }

    }

    public List<IslandsPlayer> getBaseKits() {
        return baseKits;
    }

    public static KitSelectGUI getInstance() {
        return INSTANCE;
    }

}
