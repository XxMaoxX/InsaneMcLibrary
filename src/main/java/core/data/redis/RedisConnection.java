package core.data.redis;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisConnection {

    private RedisAcces redisAcces;
    private RedisInfo redisInfo;

    public RedisConnection() {
    }

    public boolean startConnection(Plugin plugin) {
        this.redisInfo = new RedisInfo(plugin);
        this.redisAcces = new RedisAcces(redisInfo.getRedisCredentials(plugin));
        redisAcces.init();
        return true;
    }

    public boolean startConnection(JavaPlugin plugin) {
        this.redisInfo = new RedisInfo(plugin);
        this.redisAcces = new RedisAcces(redisInfo.getRedisCredentials(plugin));
        redisAcces.init();
        return true;
    }

    public boolean stopConnection() {
        if(redisAcces == null) return false;
        if(redisAcces.getRedissonClient().isShutdown()) return true;
        redisAcces.shutdown();
        return true;
    }

    public boolean isConnected() {
        return redisAcces.isConnected();
    }

    public RedisAcces getRedisAcces() {
        return redisAcces;
    }
}
