package core.rank.provider;

import core.communicator.Communicator;
import core.data.mysql.async.Query;
import core.data.redis.async.RedisQuery;
import core.rank.Rank;
import core.rank.inserter.RankInserter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitTask;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RankProvider {

    private String rankName;
    private Connection connection;
    private RedissonClient redissonClient;


    public RankProvider(String rankName) {
        this.rankName = rankName;
        this.connection = Communicator.getMySQLConnection().getMySQLAcces().getConnection();
        this.redissonClient = Communicator.getRedisConnection().getRedisAcces().getRedissonClient();
    }

    public Rank getRank(JavaPlugin plugin) {

        Rank rank = getFromRedis(plugin);

        if (rank == null) {

            rank = getFromSql(plugin);
            new RankInserter(rank).toRedis(plugin);

            if (rank == null)
                new RankInserter(rank).toSql(plugin).toRedis(plugin);
        }

        return rank;
    }

    public Rank getRank(Plugin plugin) {

        Rank rank = getFromRedis(plugin);

        if (rank == null) {

            rank = getFromSql(plugin);
            new RankInserter(rank).toRedis(plugin);

            if (rank == null) {
                new RankInserter(rank).toSql(plugin).toRedis(plugin);
            }
        }

        return rank;
    }

    private Rank getFromRedis(Plugin plugin) {
        final String key = "ranks:" + rankName;
        final Rank[] rank = new Rank[]{};

        plugin.getProxy().getScheduler().runAsync(plugin, new RedisQuery(redissonClient, key, ((result, thrown) -> {
            if (thrown == null) {
                if (result != null) rank[0] = (Rank) result.get();
            }
        })));

        return rank[0];
    }

    private Rank getFromRedis(JavaPlugin plugin) {
        final String key = "ranks:" + rankName;
        final Rank[] rank = new Rank[]{};

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new RedisQuery(redissonClient, key, ((result, thrown) -> {
            if (thrown == null) {
                if (result != null) rank[0] = (Rank) result.get();
            }
        })));

        return rank[0];
    }

    private Rank getFromSql(Plugin plugin) {
        final Rank[] rank = new Rank[]{};

        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM 'ranks_data' WHERE name=?");

            preparedStatement.setString(1, rankName);

            final ScheduledTask scheduledTask = plugin.getProxy().getScheduler().runAsync(plugin, new Query(preparedStatement, connection, (result, thrown) -> {
                if (thrown != null) {
                    try {
                        if (result.next()) {
                            String name = result.getString("name");
                            String prefix = result.getString("prefix");
                            String suffix = result.getString("suffix");
                            boolean staff = result.getBoolean("staff");
                            boolean donator = result.getBoolean("donator");
                            String permissions = result.getString("permissions");

                            List<String> list = Arrays.asList(permissions);
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(list);

                            rank[0] = (new Rank(name, prefix, suffix, staff, donator, perms));
                        } else {
                            rank[0] = (getDefaultRank());
                            plugin.getLogger().severe("&cCould not find a rank with the name: {" + rankName + "} ");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rank[0];
    }

    private Rank getFromSql(JavaPlugin plugin) {
        final Rank[] rank = new Rank[]{};

        try {

            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM 'ranks_data' WHERE name=?");

            preparedStatement.setString(1, rankName);

            final BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Query(preparedStatement, connection, ((result, thrown) -> {
                if (thrown != null) {
                    try {
                        if (result.next()) {
                            String name = result.getString("name");
                            String prefix = result.getString("prefix");
                            String suffix = result.getString("suffix");
                            boolean staff = result.getBoolean("staff");
                            boolean donator = result.getBoolean("donator");
                            String permissions = result.getString("permissions");

                            List<String> list = Arrays.asList(permissions);
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(list);

                            rank[0] = (new Rank(name, prefix, suffix, staff, donator, perms));
                        } else {
                            rank[0] = (getDefaultRank());
                            plugin.getLogger().severe("&cCould not find a rank with the name: {" + rankName + "}");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            })));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rank[0];
    }

    public Rank getDefaultRank() {
        return new Rank("regular", "&7", "", false, false, new LinkedList<>());
    }
}
