package me.flaymed.islands.kits.skills.archer;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.KitPlayer;
import com.podcrash.gamecore.kits.KitPlayerManager;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Barrage extends Ability implements Passive {

    private final double PI2 = Math.PI * 2;
    private int numArrows = 4;

    @Override
    public String getName() {
        return "Barrage";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOW);
    }

    @EventHandler
    public void archerShoot(EntityShootBowEvent e) {
        float charge = e.getForce();
        Arrow arrow = (Arrow) e.getProjectile();
        if (e.getEntity() instanceof Player) {

            Player player = (Player) e.getEntity();
            KitPlayer kitPlayer = KitPlayerManager.getKitPlayerFromPlayer(player);

            if (kitPlayer == null) return;
            if (getKitPlayer() != kitPlayer) return;

            //TODO: use a normal vector
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
                arrow1.setShooter(player);
                player.playSound(player.getLocation(), "random.bow", 0.12F, 1);

            }
        }

    }

}
