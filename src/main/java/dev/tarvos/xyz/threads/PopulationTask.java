package dev.tarvos.xyz.threads;

import dev.tarvos.xyz.Core;
import dev.tarvos.xyz.blockdata.TreeData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * Created by Nyvil, 04/03/2022 at 18:21
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

public class PopulationTask extends BukkitRunnable {

    @Override
    public void run() {
        if (Core.getCore().getCache().isEmpty()) {
            return;
        }

        for (TreeData data : Core.getCore().getCache()) {
            if (data.getTargetBlock() == null || data.getTargetBlock().getType() == Material.AIR) {
                data.getTargetBlock().getWorld().generateTree(data.getTargetBlock().getLocation(), data.getTreeType());
            }
        }

        Core.getCore().getCache().clear();
        Bukkit.getLogger().log(Level.INFO, "Trees repopulated, clearing cache.");
    }

}
