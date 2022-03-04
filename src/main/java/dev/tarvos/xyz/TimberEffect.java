package dev.tarvos.xyz;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Random;

public class TimberEffect {

    private final Particle particle;
    private final int amount;
    private final Random r = new Random();

    public TimberEffect(Particle particle) {
        this(particle, 10);
    }

    public TimberEffect(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
    }

    private Vector randomCircularVector() {
        double rnd, x, z;
        rnd = r.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);

        return new Vector(x, 0, z);
    }

    public void display(Location location) {
        for (int i = 0; i < amount; i++) {
            Vector v = randomCircularVector().multiply(r.nextDouble() * 0.6d);
            v.setY(r.nextFloat() * 1.8);
            location.add(v);
            location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0);
            location.subtract(v);
        }
    }
}
