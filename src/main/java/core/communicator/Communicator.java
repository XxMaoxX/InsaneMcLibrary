package core.communicator;


import core.communicator.listener.CommunicatorListener;
import core.communicator.message.Message;
import core.data.mysql.MySQLConnection;
import core.data.redis.RedisConnection;
import core.exceptions.MySqlNotConnectedException;
import core.exceptions.RedisNotConnectedException;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.HashSet;
import java.util.Set;

public class Communicator {

    private final Set<CommunicatorListener> listeners = new HashSet<CommunicatorListener>();

    private static MySQLConnection mySQLConnection;
    private static RedisConnection redisConnection;

    public Communicator() {
        this.mySQLConnection = new MySQLConnection();
        this.redisConnection = new RedisConnection();
    }

    public void startConnection(Plugin plugin) {
        if (!mySQLConnection.startConnection(plugin)) {
            try {
                throw new MySqlNotConnectedException();
            } catch (MySqlNotConnectedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!redisConnection.startConnection(plugin)) {
            try {
                throw new RedisNotConnectedException();
            } catch (RedisNotConnectedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startConnection(JavaPlugin plugin) {
        if (!mySQLConnection.startConnection(plugin)) {
            try {
                throw new MySqlNotConnectedException();
            } catch (MySqlNotConnectedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!redisConnection.startConnection(plugin)) {
            try {
                throw new RedisNotConnectedException();
            } catch (RedisNotConnectedException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopConnection() {
        mySQLConnection.stopConnection();
        redisConnection.stopConnection();
    }

    public Communicator unloadServer(String server) {
        RedissonClient client = getRedisConnection().getRedisAcces().getRedissonClient();
        RList<String> list = client.getList("servers");
        list.removeAsync(server);
        return this;
    }

    private Communicator loadServer(String server) {
        RedissonClient client = getRedisConnection().getRedisAcces().getRedissonClient();
        RList<String> list = client.getList("servers");
        list.addAsync(server);
        return this;
    }

    private void loadListener(String server) {
        RedissonClient client = getRedisConnection().getRedisAcces().getRedissonClient();
        RTopic<Message> topic = client.getTopic("communicator");
        topic.addListener(($, message) -> {
            if (!message.getTo().equalsIgnoreCase("all"))
                if (!message.getTo().equalsIgnoreCase(server))
                    return;
            listeners.stream()
                    .filter(listener -> listener.getChannel().equalsIgnoreCase(message.getType()))
                    .forEach(listener -> listener.execute(message));
        });
    }

    public void sendMessage(Message message) {
        RedissonClient client = getRedisConnection().getRedisAcces().getRedissonClient();
        RTopic<Message> topic = client.getTopic("communicator");
        topic.publish(message);
    }

    public void sendMessage(Message message, boolean async) {
        RedissonClient client = getRedisConnection().getRedisAcces().getRedissonClient();
        RTopic<Message> topic = client.getTopic("communicator");
        if(async) {
            topic.publishAsync(message);
        }else topic.publish(message);
    }

    public Set<CommunicatorListener> getListeners() {
        return listeners;
    }


    public boolean isMySQLConnected() {
        return mySQLConnection.isConnected();
    }

    public boolean isRedisConnected() {
        return redisConnection.isConnected();
    }


    public static MySQLConnection getMySQLConnection() {
        return mySQLConnection;
    }

    public static void setMySQLConnection(MySQLConnection mySQLConnection) {
        Communicator.mySQLConnection = mySQLConnection;
    }

    public static RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public static void setRedisConnection(RedisConnection redisConnection) {
        Communicator.redisConnection = redisConnection;
    }
}
