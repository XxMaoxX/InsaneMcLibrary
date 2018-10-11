package core.rank.inserter;

import core.communicator.Communicator;
import core.data.mysql.async.Update;
import core.data.redis.async.RedisUpdate;
import core.rank.Rank;


import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RankInserter {

    private Rank rank;

    private Connection connection;
    private RedissonClient redissonClient;


    public RankInserter(Rank rank) {
        this.rank = rank;
        this.connection = Communicator.getMySQLConnection().getMySQLAcces().getConnection();
        this.redissonClient = Communicator.getRedisConnection().getRedisAcces().getRedissonClient();
    }


    public RankInserter toSql(Plugin plugin) {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO 'ranks_data' VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE UPDATE name=?");
            ps.setString(1,rank.getName());
            ps.setString(2, rank.getPrefix());
            ps.setString(3, rank.getSuffix());
            ps.setBoolean(4, rank.isStaff());
            ps.setBoolean(5, rank.isDonator());
            ps.setString(6, rank.getPermissions().toString().replace("[","").replace("]", ""));
            ps.setString(7,rank.getName());

            plugin.getProxy().getScheduler().runAsync(plugin, new Update(ps, connection, ((result, thrown) -> {

                if(thrown == null) {
                    if(result != null) {
                        plugin.getLogger().info(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&aSuccessufly inserted a rank into the Sql database!"));
                        return;
                    }
                }

            })));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public RankInserter toSql(JavaPlugin plugin) {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO 'ranks_data' VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE UPDATE name=?");
            ps.setString(1,rank.getName());
            ps.setString(2, rank.getPrefix());
            ps.setString(3, rank.getSuffix());
            ps.setBoolean(4, rank.isStaff());
            ps.setBoolean(5, rank.isDonator());
            ps.setString(6, rank.getPermissions().toString().replace("[","").replace("]", ""));
            ps.setString(7,rank.getName());

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Update(ps, connection, ((result, thrown) -> {

                if(thrown == null) {
                    if(result != null) {
                        plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aSuccessufly inserted a rank into the Sql database!"));
                        return;
                    }
                }

            })));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public RankInserter toRedis(Plugin plugin) {

        final String key = "ranks:" + rank.getName();

        plugin.getProxy().getScheduler().runAsync(plugin, new RedisUpdate(redissonClient, key, rank, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    plugin.getLogger().info(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&aSuccessufly inserted a rank into the Redis cache!"));
                    return;
                }
            }
        })));

        return this;
    }

    public RankInserter toRedis(JavaPlugin plugin) {

        final String key = "ranks:" + rank.getName();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new RedisUpdate(redissonClient, key, rank, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aSuccessufly inserted a rank into the Redis cache!"));
                    return;
                }
            }
        })));

        return this;
    }

    public RankInserter sofStave(JavaPlugin plugin) {
        toRedis(plugin);
        return this;
    }

    public RankInserter sofStave(Plugin plugin) {
        toRedis(plugin);
        return this;
    }

    public RankInserter save(JavaPlugin plugin) {
        toRedis(plugin);
        toSql(plugin);
        return this;
    }

    public RankInserter save(Plugin plugin) {
        toRedis(plugin);
        toSql(plugin);
        return this;
    }


}
