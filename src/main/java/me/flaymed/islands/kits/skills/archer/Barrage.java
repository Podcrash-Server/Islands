package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.kits.skilltypes.Passive;
import com.podcrash.api.listeners.GameDamagerConverterListener;
import com.podcrash.api.sound.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Barrage extends Passive {

    private final double PI2 = Math.PI * 2;
    private int numArrows = 4;

    @Override
    public String getName() {
        return "Barrage";
    }

    @EventHandler
    public void archerShoot(EntityShootBowEvent e) {
        float charge = e.getForce();
        Arrow arrow = (Arrow) e.getProjectile();

        if(charge <= 0.1F) return;
        int total = (int) (((charge * numArrows) + 0.5F)/2);
        Location location = arrow.getLocation();
        World world = location.getWorld();
        Vector vector = arrow.getVelocity();
        float speed  = (float) vector.length();
        vector = vector.clone().normalize();
        final double increment = Math.PI/total;
        for(double i = 0; i <= PI2; i += increment) {
            double x = 0.05 * Math.sin(i);
            double y = 0.04 * Math.sin(i);
            double z = 0.05 * Math.cos(i);
            Vector vector1 = new Vector(x, z, y);
            Arrow arrow1 = world.spawnArrow(location, vector.clone().add(vector1).normalize(), speed, 0);
            arrow1.setShooter(getPlayer());
            GameDamagerConverterListener.forceAddArrow(arrow1, 0.9F);
            SoundPlayer.sendSound(getPlayer().getLocation(), "random.bow", 0.12F, 70);

        }
    }

}
