package dev.tarvos.xyz.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationStorage extends FileUtil {

    final String HOME = "locations";

    public LocationStorage(JavaPlugin plugin) {
        super(new File(plugin.getDataFolder(), "trees.yml"));
    }

    public boolean hasData() {
        return get(HOME) != null;
    }

    public boolean exists(String identifier) {
        return get(HOME + "." + identifier) != null;
    }

    public void saveLocation(String identifier, Location location) {
        try {
            set(HOME + "." + identifier + ".x", location.getX(), false);
            set(HOME + "." + identifier + ".y", location.getY(), false);
            set(HOME + "." + identifier + ".z", location.getZ(), false);
            set(HOME + "." + identifier + ".world", location.getWorld().getName(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Location getLocation(String identifier) {
        double x, y, z;
        World world;

        x = getDouble(HOME + "." + identifier + ".x");
        y = getDouble(HOME + "." + identifier + ".y");
        z = getDouble(HOME + "." + identifier + ".z");
        world = Bukkit.getWorld(getString(HOME + "." + identifier + ".world"));

        return new Location(world, x, y, z);
    }

    public List<Location> getSavedLocations() {
        List<Location> result = new ArrayList<>();
        for (String key : getConfig().getConfigurationSection(HOME).getKeys(false)) {
            double x = getDouble(HOME + "." + key + ".x");
            double y = getDouble(HOME + "." + key + ".y");
            double z = getDouble(HOME + "." + key + ".z");
            World world = Bukkit.getWorld(getString(HOME + "." + key + ".world"));

            result.add(new Location(world, x, y, z));
        }

        return result;
    }
}
