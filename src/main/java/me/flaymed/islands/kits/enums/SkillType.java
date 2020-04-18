package me.flaymed.islands.kits.enums;

public enum SkillType {
    Beserker("Beserker"), Archer("Archer"),
    Assassin("Assassin"), Brawler("Brawler"),
    Builder("Builder"), Bard("Bard"),
    Miner("Miner"), Fisherman("Fisherman"),
    Global("All");

    private String name;

    SkillType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SkillType getByName(String name){
        name = name.toLowerCase();
        for(SkillType skillType : SkillType.values()) {
            if(name.contains(skillType.getName().toLowerCase())) return skillType;
        }
        return null;
    }
    @Override
    public String toString() {
        return getName();
    }

    private final static SkillType[] details = SkillType.values();
    public static SkillType[] details() {
        return details;
    }
}
