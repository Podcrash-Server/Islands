package me.flaymed.islands.kits;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Skill {

    private String name;
    private String desc;
    private int coolDown;
    private int dmgReduction;
    private int dmgAddition;
    private List<ItemStack> startingItems;

    public Skill(String name, String desc, int coolDown) {
        this.name = name;
        this.desc = desc;
        this.coolDown = coolDown;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getDmgReduction() {
        return dmgReduction;
    }

    public void setDmgReduction(int dmgReduction) {
        this.dmgReduction = dmgReduction;
    }

    public int getDmgAddition() {
        return dmgAddition;
    }

    public void setDmgAddition(int dmgAddition) {
        this.dmgAddition = dmgAddition;
    }

    public List<ItemStack> getStartingItems() {
        return startingItems;
    }

    public void addStartingItem(ItemStack item) {
        this.startingItems.add(item);
    }

    public void removeStartingItem(ItemStack item) {
        this.startingItems.remove(item);
    }


}
