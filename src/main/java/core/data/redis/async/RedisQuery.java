package core.data.redis.async;


import core.data.Callback;
import core.data.redis.RedisAcces;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class RedisQuery implements Runnable {

    private RedissonClient redisClient;
    private String key;
    private RBucket<Object> bucket;
    private Callback<RBucket<Object>, Exception> callback;

    private boolean exists;


    public RedisQuery(RedisAcces redisAcces, String key, Callback<RBucket<Object>, Exception> callback) {
        this.redisClient = redisAcces.getRedissonClient();
        this.key = key;
        this.callback = callback;
    }

    public RedisQuery(RedissonClient redisClient, String key,Callback<RBucket<Object>, Exception> callback) {
        this.redisClient = redisClient;
        this.key = key;
        this.callback = callback;
    }

    @Override
    public void run() {
        bucket = redisClient.getBucket(key);

        if((!bucket.isExists()) || bucket == null) {
            exists = false;
            try {
                throw new Exception("Could not find a redis object with the key: " + key);
            } catch (Exception e) {
                e.printStackTrace();
                callback.call(null, e);
            }
            return;
        }
        callback.call(bucket, null);
        exists = true;
    }

    public String getKey() {
        return key;
    }


    public RBucket<Object> getBucket() {
        return bucket;
    }

    public boolean isExists() {
        return exists;
    }

    public void setRedisClient(RedisAcces redisAcces) {
        this.redisClient = redisAcces.getRedissonClient();
    }

    public void setRedisClient(RedissonClient redisClient) {
        this.redisClient = redisClient;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
