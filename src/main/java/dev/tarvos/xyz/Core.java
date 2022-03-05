package dev.tarvos.xyz;

import dev.tarvos.xyz.blockdata.TreeData;
import dev.tarvos.xyz.commands.AddTreeLocation;
import dev.tarvos.xyz.listeners.TreeBreakHandler;
import dev.tarvos.xyz.threads.PopulationTask;
import dev.tarvos.xyz.util.LocationStorage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Core extends JavaPlugin {

    private static Core core;
    private final Map<TreeData, Long> cache = new ConcurrentHashMap<>();
    private LocationStorage treeConfig;

    public static Core getCore() {
        return core;
    }

    @Override
    public void onEnable() {
        core = this;
        getServer().getPluginManager().registerEvents(new TreeBreakHandler(), this);
        getCommand("addtreelocation").setExecutor(new AddTreeLocation());
        treeConfig = new LocationStorage(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PopulationTask(), 0, 20*60*15); //15 minutes
    }

    @Override
    public void onDisable() {
        cache.clear();
    }
}
