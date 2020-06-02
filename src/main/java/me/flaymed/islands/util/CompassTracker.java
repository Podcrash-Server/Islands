package me.flaymed.islands.util;

import com.podcrash.api.game.Game;
import com.podcrash.api.game.GameManager;
import net.jafama.FastMath;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public final class CompassTracker {
    public static class CompassResult {
        private final Player player;
        private final double distance;
        private final Vector location;

        public CompassResult(@Nonnull Player player, double distance, Vector location) {
            this.player = player;
            this.distance = distance;
            this.location = location;
        }

        public Player getPlayer() {
            return player;
        }

        public double getDistance() {
            return distance;
        }

        public Vector getLocation() {
            return location;
        }
    }
    public static CompassResult findCompassResult(Player compassHolder) {
        Game game = GameManager.getGame();
        if (game == null)
            return new CompassResult(compassHolder, 0, compassHolder.getLocation().toVector());
        Player currPlayer = compassHolder;
        double dist = Double.MAX_VALUE;
        Location compassLoc = compassHolder.getLocation();
        for (Player player : game.getBukkitPlayers()) {
            if (game.isOnSameTeam(player, compassHolder))
                continue;
            Location location = player.getLocation();
            double check = location.distanceSquared(compassLoc);
            if (check < dist) {
                dist = check;
                currPlayer = player;
            }
        }

        return new CompassResult(currPlayer, FastMath.sqrt(dist), currPlayer.getLocation().toVector());
    }
}
