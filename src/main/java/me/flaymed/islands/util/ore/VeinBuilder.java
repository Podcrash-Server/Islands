package me.flaymed.islands.util.ore;

import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class VeinBuilder {
    //between 0-1
    double oreChance;
    double continueChance;
    Set<Vector> allowedDirections;
    int tries;

    public VeinBuilder() {
        this.allowedDirections = new HashSet<>(Arrays.asList(
                new Vector(0, 1, 0),
                new Vector(0, -1, 0),
                new Vector(1, 0, 0),
                new Vector(-1, 0, 0),
                new Vector(0, 0, 1),
                new Vector(0, 0, -1))
        );
        this.oreChance = 0.5;
        this.continueChance = 0.5;
    }

    public VeinBuilder setAllowedDirections(Set<Vector> allowedDirections) {
        this.allowedDirections = allowedDirections;
        return this;
    }

    public VeinBuilder addAllowedDirection(Vector vector) {
        this.allowedDirections.add(vector);
        return this;
    }

    /**
     * Chance that the ore will spawn
     * @param oreChance must be between 0 inclusive and 1 exclusive
     */
    public VeinBuilder setOreChance(double oreChance) {
        this.oreChance = oreChance;
        return this;
    }

    /**
     * Chance that there will be more of the ore within the stone
     * @param continueChance must be between 0 inclusive and 1 exclusive
     */
    public VeinBuilder setContinueChance(double continueChance) {
        this.continueChance = continueChance;
        return this;
    }

    public VeinBuilder setTries(int tries) {
        this.tries = tries;
        return this;
    }
}