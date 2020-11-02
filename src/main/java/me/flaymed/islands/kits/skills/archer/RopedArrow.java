package me.flaymed.islands.kits.skills.archer;

import com.podcrash.gamecore.kits.abilitytype.BowShotAbility;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class RopedArrow extends BowShotAbility {

    @Override
    public String getName() {
        return "Roped Arrow";
    }

    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    protected void shotArrow(Arrow arrow, float force) {

    }

    @Override
    protected void shotEntity(EntityDamageByEntityEvent event, Player shooter, LivingEntity victim, Arrow arrow, float force) {
        Vector curVelocity = victim.getVelocity();
        victim.setVelocity(curVelocity.setY(0.5));
    }

    @Override
    protected void shotGround(Player shooter, Location location, Arrow arrow, float force) {
        boost(arrow.getLocation(), force, arrow.getVelocity());
    }

    private void boost(Location endpoint, float force, Vector arrowvelocity) {
        Player player = getKitPlayer().getPlayer();
        player.sendMessage(getUsedMessage());
        Location playerLoc = player.getLocation();
        force = (3 * force + 1f) / 4f;
        Vector vector = endpoint.toVector().subtract(playerLoc.toVector()).normalize().multiply(force);
        double multiplier = 0.8D;
        vector.setY(vector.getY() + 0.2d * multiplier);
        Vector playerV = player.getLocation().getDirection();
        if (playerV.getY() < 0) playerV.setY(0);
        vector.setY(playerV.getY() + vector.getY());
        double yMax = 0.5d + 0.52d * multiplier;
        if (vector.getY() > yMax) vector.setY(yMax);
        player.setVelocity(vector.multiply(0.3d + 1.2 * multiplier));
        player.setFallDistance(-1.5f);
    }
}
