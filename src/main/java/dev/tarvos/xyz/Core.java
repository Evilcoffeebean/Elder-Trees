package dev.tarvos.xyz;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public class Core extends JavaPlugin implements Listener {

    private static Core core;
    private static final List<TreeData> cache = Lists.newArrayList();
    private BukkitRunnable populatorTask;

    public static Core getCore() {
        return core;
    }

    @Override
    public void onEnable() {
        core = this;
        getServer().getPluginManager().registerEvents(this, this);

        populatorTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (cache.isEmpty())
                    return;

                for (TreeData data : cache) {
                    if (data.getTargetBlock() == null || data.getTargetBlock().getType() == Material.AIR) {
                        data.getTargetBlock().getWorld().generateTree(data.getTargetBlock().getLocation(), data.getTreeType());
                    }
                }

                cache.clear();
                Bukkit.getLogger().log(Level.INFO, "Trees repopulated, clearing cache.");
            }
        };

        populatorTask.runTaskTimer(this, 10L, 20*10); //repopulate the trees every 10 seconds for example
    }

    @Override
    public void onDisable() {
        cache.clear();
        getServer().getScheduler().cancelTasks(this);
    }

    //timber test
    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        final TreeData treeData = new TreeData(e.getBlock());

        if (!treeData.isValid())
            return;
        if (!treeData.hasAxe(e.getPlayer()))
            return;

        treeData.executeTimber();
        if (!cache.contains(treeData))
            cache.add(treeData);
    }
}
