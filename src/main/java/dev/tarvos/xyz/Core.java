package dev.tarvos.xyz;

import dev.tarvos.xyz.blockdata.TreeData;
import dev.tarvos.xyz.database.DatabaseConnection;
import dev.tarvos.xyz.listeners.TreeBreakHandler;
import dev.tarvos.xyz.threads.PopulationTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Core extends JavaPlugin {

    @Getter
    private static Core core;
    @Getter
    private final Map<TreeData, Long> cache = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private String db, dbUser, dbPwd, dbPort, host;
    @Getter
    private DatabaseConnection databaseConnection;

    @Override
    public void onEnable() {
        core = this;
        setDb("tarvos");
        setDbUser("nyvil-pc");
        setDbPwd("&]zGD-9{x!9Gzfyg");
        setDbPort("29017");
        setHost("185.216.176.189");


        getServer().getPluginManager().registerEvents(new TreeBreakHandler(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PopulationTask(), 0, 20 * 5); //debug: repeat every 5 seconds

        //databaseConnection = new DatabaseConnection();

    }

    @Override
    public void onDisable() {
        cache.clear();
    }
}
