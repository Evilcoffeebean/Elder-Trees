package dev.tarvos.xyz.listeners;

import dev.tarvos.xyz.Core;
import dev.tarvos.xyz.blockdata.TreeData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Nyvil, 04/03/2022 at 18:35
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

public class TreeBreakHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        final TreeData treeData = new TreeData(e.getBlock());

        if (!treeData.isValid()) return;
        if (!treeData.hasAxe(e.getPlayer())) return;

        treeData.executeTimber();
        if (!Core.getCore().getCache().contains(treeData))
            Core.getCore().getCache().add(treeData);
    }

}
