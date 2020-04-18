package me.flaymed.islands.kits.enums;

public enum ItemType {
    SWORD("SWORD"),
    AXE("AXE"),
    BOW("BOW"),
    PICKAXE("PICKAXE"),
    ROD("FISHINGROD"),
    NULL("NULL");

    private String name;

    private final static ItemType[] details = ItemType.values();

    public static ItemType[] details() {
        return details;
    }

    ItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
