package me.flaymed.islands.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import java.util.Objects;

public class AmntMaterialData {
    private final MaterialData materialData;
    private final int amount;

    public AmntMaterialData(Material material) {
        this(new MaterialData(material));
    }
    public AmntMaterialData(MaterialData materialData) {
        this(materialData, 1);
    }

    public AmntMaterialData(Material material, int amount) {
        this(new MaterialData(material), amount);
    }
    public AmntMaterialData(MaterialData materialData, int amount) {
        this.materialData = materialData;
        this.amount = amount;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public int getAmount() {
        return amount;
    }

    public ItemStack getStack() {
        return materialData.toItemStack(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmntMaterialData)) return false;

        AmntMaterialData that = (AmntMaterialData) o;

        if (amount != that.amount) return false;
        return Objects.equals(materialData, that.materialData);
    }

    @Override
    public int hashCode() {
        int result = materialData != null ? materialData.hashCode() : 0;
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AmntMaterialData{");
        sb.append("materialData=").append(materialData);
        sb.append(", amount=").append(amount);
        sb.append(", stack=").append(getStack());
        sb.append('}');
        return sb.toString();
    }
}
