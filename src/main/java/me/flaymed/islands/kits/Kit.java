package me.flaymed.islands.kits;

public class Kit {

    private String name;
    private String desc;
    private int cost;

    public Kit(String name, String desc, int cost) {
        this.name = name;
        this.desc = desc;
        this.cost = cost;
    }

    public String getDesc() {
        return desc;
    }
    public String getName() { return name; }
    public int getCost() { return cost; }

}
