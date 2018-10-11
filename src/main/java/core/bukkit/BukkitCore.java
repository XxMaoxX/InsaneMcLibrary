package core.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCore extends JavaPlugin {

    private static JavaPlugin instance = new BukkitCore();

    public static JavaPlugin getInstance() {
        return instance;
    }
}
