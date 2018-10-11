package core.data.redis;


import core.utils.BukkitConfig;
import core.utils.BungeeConfig;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisInfo {


    public RedisInfo(Plugin plugin) {
        BungeeConfig config = new BungeeConfig("settings");
    }

    public RedisInfo(JavaPlugin plugin) {
        BukkitConfig config = new BukkitConfig(plugin.getDataFolder().getAbsolutePath(), "settings");
    }

    protected RedisCredentials getRedisCredentials(Plugin plugin) {
        BungeeConfig settings = new BungeeConfig("settings");

        if (settings.getKeys().isEmpty()) {
            settings.set("redis.ip", "");
            settings.set("redis.password", "");
            settings.set("redis.port", "");
            settings.set("redis.clientName", "");
            return null;
        }

        String ip = settings.getString("redis.ip");
        String password = settings.getString("redis.password");
        String clientName = settings.getString("redis.clientName");
        int port = settings.getInteger("redis.port");

        return new RedisCredentials(ip, port, password, clientName);
    }

    protected RedisCredentials getRedisCredentials(JavaPlugin plugin) {
        BukkitConfig settings = new BukkitConfig(plugin.getDataFolder().getAbsolutePath(), "settings");

        if (settings.getKeys(false).isEmpty()) {
            settings.set("redis.ip", "");
            settings.set("redis.password", "");
            settings.set("redis.port", "");
            settings.set("redis.clientName", "");
            return null;
        }

        String ip = settings.getString("redis.ip");
        String password = settings.getString("redis.password");
        String clientName = settings.getString("redis.clientName");
        int port = settings.getInt("redis.port");

        return new RedisCredentials(ip, port, password, clientName);
    }

}
