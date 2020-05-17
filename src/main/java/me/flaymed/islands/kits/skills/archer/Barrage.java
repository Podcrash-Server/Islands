package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.kits.skilltypes.Passive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Barrage extends Passive {
    @Override
    public String getName() {
        return "Barrage";
    }

    @EventHandler
    public void archerShoot(EntityShootBowEvent e) {
        if (e.getForce() == 1.0) {
            Entity arrow = e.getProjectile();
            Random rand = new Random();
            int arrowX = (int) arrow.getLocation().getX();
            int arrowY = (int) arrow.getLocation().getY();
            int arrowZ = (int) arrow.getLocation().getZ();
            int offset = 3;

            for (int i = 0; i < 4; i++) {

                arrow.getWorld().spawnArrow(new Location(arrow.getWorld(), arrowX + rand.nextInt(offset + offset) - offset, arrowY + rand.nextInt(offset + 0) - 0, arrowZ + rand.nextInt(offset + offset) - offset), arrow.getVelocity(), 3, 3).setShooter(e.getEntity());
            }

            getPlayer().getInventory().remove(new ItemStack(Material.ARROW, 1));
            getPlayer().updateInventory();

        }
    }

}
