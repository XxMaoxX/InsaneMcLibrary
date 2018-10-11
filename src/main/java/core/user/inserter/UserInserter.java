package core.user.inserter;

import core.communicator.Communicator;
import core.data.mysql.async.Update;
import core.data.redis.async.RedisUpdate;
import core.user.User;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserInserter {

    private User user;
    private RedissonClient redissonClient;
    private Connection connection;

    public UserInserter(User user) {
        this.user = user;
        connection = Communicator.getMySQLConnection().getMySQLAcces().getConnection();
        redissonClient = Communicator.getRedisConnection().getRedisAcces().getRedissonClient();
    }


    public UserInserter toSql(Plugin plugin) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO 'players_data' VALUES(?, ?, ?, ?, ?) ON DUPLICATE UPDATE uuid=?");

            ps.setString(1, user.getUuid().toString());
            ps.setInt(2, user.getLevel());
            ps.setInt(3, user.getCoins());
            ps.setDouble(4, user.getExp());
            ps.setString(5, user.getRank().getName());
            ps.setString(6, user.getSubrank().getName());
            ps.setString(7, user.getUuid().toString());

            plugin.getProxy().getScheduler().runAsync(plugin, new Update(ps, connection, ((result, thrown) -> {
                if(thrown == null) {
                    if(result != null) {
                        plugin.getLogger().info("&aSuccessufly inserted a User to the Sql database");
                        return;
                    }
                }
            })));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }
    public UserInserter toSql(JavaPlugin plugin) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO 'players_data' VALUES(?, ?, ?, ?, ?) ON DUPLICATE UPDATE uuid=?");

            ps.setString(1, user.getUuid().toString());
            ps.setInt(2, user.getLevel());
            ps.setInt(3, user.getCoins());
            ps.setDouble(4, user.getExp());
            ps.setString(5, user.getRank().getName());
            ps.setString(6, user.getSubrank().getName());
            ps.setString(7, user.getUuid().toString());

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Update(ps, connection, ((result, thrown) -> {
                if(thrown == null) {
                    if(result != null) {
                        plugin.getLogger().info("&aSuccessufly inserted a User to the Sql database");
                        return;
                    }
                }
            })));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }


    public UserInserter toRedis(Plugin plugin) {
        final String key = "accounts:" + user.getUuid();

        plugin.getProxy().getScheduler().runAsync(plugin, new RedisUpdate(redissonClient, key, user, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    plugin.getLogger().info("&aSuccessufly inserted a User to the Redis cache");
                    return;
                }
            }
        })));

        return this;
    }

    public UserInserter toRedis(JavaPlugin plugin) {
        final String key = "accounts:" + user.getUuid();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new RedisUpdate(redissonClient, key, user, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    plugin.getLogger().info("&aSuccessufly inserted a User to the Redis cache");
                    return;
                }
            }
        })));

        return this;
    }

    public UserInserter softSave(Plugin plugin) {
        toRedis(plugin);
        return this;
    }

    public UserInserter softSave(JavaPlugin plugin) {
        toRedis(plugin);
        return this;
    }

    public UserInserter save(Plugin plugin) {
        toSql(plugin);
        toRedis(plugin);
        return this;
    }

    public UserInserter save(JavaPlugin plugin) {
        toSql(plugin);
        toRedis(plugin);
        return this;
    }

}
