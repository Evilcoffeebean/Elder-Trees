package dev.tarvos.xyz;

import com.google.common.collect.Lists;
import dev.tarvos.xyz.blockdata.TreeData;
import dev.tarvos.xyz.listeners.TreeBreakHandler;
import dev.tarvos.xyz.threads.PopulationTask;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Core extends JavaPlugin {

    @Getter
    private static Core core;
    @Getter
    private final List<TreeData> cache = Lists.newArrayList();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    public void onEnable() {
        core = this;
        getServer().getPluginManager().registerEvents(new TreeBreakHandler(), this);

        executorService.schedule(new PopulationTask(), 10, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        cache.clear();
        executorService.shutdown(); // prevent new tasks being scheduled
        try {
            if(!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace(); // if for some reason it couldn't shutdown, should never happen though
            executorService.shutdownNow();
        }
    }

}
