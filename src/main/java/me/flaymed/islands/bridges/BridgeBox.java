package me.flaymed.islands.bridges;

import com.podcrash.api.location.BoundingBox;
import com.podcrash.api.location.Coordinate;
import org.bukkit.util.Vector;

public final class BridgeBox extends BoundingBox {
    public BridgeBox(Coordinate min, Coordinate max) {
        super(min, max);
    }

    public BridgeBox(Vector min, Vector max) {
        super(min, max);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
