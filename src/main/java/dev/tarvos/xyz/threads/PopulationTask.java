package dev.tarvos.xyz.threads;

import dev.tarvos.xyz.Core;
import dev.tarvos.xyz.blockdata.TreeData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Created by Nyvil, 04/03/2022 at 18:21
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

public class PopulationTask implements Runnable {

    @Override
    public void run() {
        if (Core.getCore().getCache().isEmpty())
            return;

        for(Map.Entry<TreeData, Long> entry : Core.getCore().getCache().entrySet()) {
            if(System.currentTimeMillis() >= entry.getValue()) {
                if (entry.getKey().getTargetBlock().getType() == Material.AIR) {
                    entry.getKey().getTargetBlock().getWorld().generateTree(entry.getKey().getTargetBlock().getLocation(), entry.getKey().getTreeType());
                    Core.getCore().getCache().remove(entry.getKey());
                }
            }
        }
    }

}
