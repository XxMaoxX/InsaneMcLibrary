package core.data.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAcces {

    private RedisCredentials redisCredentials;

    private RedissonClient redissonClient;

    private boolean connected = false;

    protected RedisAcces(RedisCredentials redisCredentials) {
        this.redisCredentials = redisCredentials;
    }

    protected void init() {
        final Config config = new Config();

        config.setCodec(new JsonJacksonCodec());
        config.setThreads(4);
        config.setNettyThreads(4);
        config.useSingleServer()
                .setAddress(redisCredentials.toURL())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(3)
                .setClientName(redisCredentials.getClientName());

        this.redissonClient = Redisson.create(config);

        connected = true;
    }

    protected void shutdown() {
        redissonClient.shutdown();
        connected = false;
    }

    protected boolean isConnected() {
        return connected;
    }

    protected RedisCredentials getRedisCredentials() {
        return redisCredentials;
    }

    protected void setRedisCredentials(RedisCredentials redisCredentials) {
        this.redisCredentials = redisCredentials;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    protected void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
