package core.user.provider;


import core.communicator.Communicator;
import core.data.mysql.async.Query;
import core.data.redis.async.RedisQuery;
import core.exceptions.UserNotFoundException;
import core.rank.Rank;
import core.rank.provider.RankProvider;
import core.user.User;
import core.user.inserter.UserInserter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UserProvider {

    private UUID uuid;

    private Connection connection;
    private RedissonClient redissonClient;

    public UserProvider(UUID uuid) {
        this.uuid = uuid;
        connection = Communicator.getMySQLConnection().getMySQLAcces().getConnection();
        redissonClient = Communicator.getRedisConnection().getRedisAcces().getRedissonClient();
    }


    public User getUser(Plugin plugin) throws UserNotFoundException {
        User user = getFromRedis(plugin);
        if (user == null) {
            user = getFromSql(plugin);
            new UserInserter(user).toRedis(plugin);
            if (user == null) {
                new UserInserter(user).toSql(plugin).toRedis(plugin);
            }
        }
        return user;

    }

    public User getUser(JavaPlugin plugin) throws UserNotFoundException {
        User user = getFromRedis(plugin);
        if (user == null) {
            user = getFromSql(plugin);
            new UserInserter(user).toRedis(plugin);
            if (user == null) {
                new UserInserter(user).toSql(plugin).toRedis(plugin);
            }
        }
        return user;

    }

    private User getFromRedis(Plugin plugin) {

        final String key = "accounts:" + uuid.toString();
        final RBucket<User> userRBucket = redissonClient.getBucket(key);
        final User[] user = new User[]{};

        plugin.getProxy().getScheduler().runAsync(plugin, new RedisQuery(redissonClient, key, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    user[0] = (User) result.get();
                }
            }
        })));

        return user[0];
    }

    private User getFromRedis(JavaPlugin plugin) {

        final String key = "accounts:" + uuid.toString();
        final RBucket<User> userRBucket = redissonClient.getBucket(key);
        final User[] user = new User[]{};

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new RedisQuery(redissonClient, key, ((result, thrown) -> {
            if(thrown == null) {
                if(result != null) {
                    user[0] = (User) result.get();
                }
            }
        })));

        return user[0];
    }


    private User getFromSql(Plugin plugin) {

        final PreparedStatement preparedStatement;
        final User[] user = new User[]{};

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM 'players_data' WHERE uuid=?");
            String uuid = this.uuid.toString();
            preparedStatement.setString(1, uuid);

            plugin.getProxy().getScheduler().runAsync(plugin, new Query(preparedStatement, connection, (((result, thrown) ->  {
                if (thrown == null) {
                    try {
                        if(result.next()) {
                            int level = result.getInt("level");
                            int coins = result.getInt("coins");
                            double exp = result.getDouble("exp");
                            Rank rank = new RankProvider(result.getString("rank")).getRank(plugin);
                            Rank subrank = new RankProvider(result.getString("subrank")).getRank(plugin);
                            List<String> list = Arrays.asList(result.getString("permissions"));
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(list);

                            user[0] = new User(UUID.fromString(uuid), level, coins, exp, rank, subrank, perms);
                        }else {
                            Rank rank = new RankProvider("").getDefaultRank();
                            Rank subrank = new RankProvider("").getDefaultRank();
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(rank.getPermissions());
                            user[0] = new User(UUID.fromString(uuid), 0, 0, 0, rank, subrank, perms);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }))));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user[0];
    }

    private User getFromSql(JavaPlugin plugin) {

        final PreparedStatement preparedStatement;
        final User[] user = new User[]{};

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM 'players_data' WHERE uuid=?");
            String uuid = this.uuid.toString();
            preparedStatement.setString(1, uuid);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Query(preparedStatement, connection, (((result, thrown) ->  {
                if (thrown == null) {
                    try {
                        if(result.next()) {
                            int level = result.getInt("level");
                            int coins = result.getInt("coins");
                            double exp = result.getDouble("exp");
                            Rank rank = new RankProvider(result.getString("rank")).getRank(plugin);
                            Rank subrank = new RankProvider(result.getString("subrank")).getRank(plugin);
                            List<String> list = Arrays.asList(result.getString("permissions"));
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(list);

                            user[0] = new User(UUID.fromString(uuid), level, coins, exp, rank, subrank, perms);
                        }else {
                            Rank rank = new RankProvider("").getDefaultRank();
                            Rank subrank = new RankProvider("").getDefaultRank();
                            LinkedList<String> perms = new LinkedList<>();
                            perms.addAll(rank.getPermissions());
                            user[0] = new User(UUID.fromString(uuid), 0, 0, 0, rank, subrank, perms);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }))));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user[0];
    }

}
