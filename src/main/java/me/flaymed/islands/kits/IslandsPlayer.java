package me.flaymed.islands.kits;

import com.google.gson.JsonObject;
import com.podcrash.api.kits.KitPlayer;
import com.podcrash.api.kits.Skill;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class IslandsPlayer extends KitPlayer {
    private final String name;

    public IslandsPlayer(Player player, String name, Class<? extends Skill>... skills) {
        super(player);
        this.name = name;

        setUpSkills(skills);
    }

    private void setUpSkills(Class<? extends Skill>... skills) {
        Set<Skill> skillsSet = new HashSet<>();
        for (Class<? extends Skill> skillClass : skills) {
            try {
                Skill skill = skillClass.newInstance();
                skillsSet.add(skill);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        this.skills = skillsSet;
    }

    public void addSkill(Class<? extends Skill> skill) {

        try {
            Skill newSkill = skill.newInstance();
            this.skills.add(newSkill);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JsonObject serialize() {
        return new JsonObject();
    }
}
