package me.flaymed.islands.inventory;

public class KitSelectGUI {

    private static KitSelectGUI INSTANCE;

    public KitSelectGUI() {
        INSTANCE = this;
    }

    public static KitSelectGUI getInstance() {
        return INSTANCE;
    }

}
