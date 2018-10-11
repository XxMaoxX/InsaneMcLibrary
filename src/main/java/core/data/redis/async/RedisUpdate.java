package core.data.redis.async;


import core.data.Callback;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class RedisUpdate implements Runnable {

    private RedissonClient client;
    private String key;
    private Object value;
    private RBucket<Object> bucket;
    private Callback<RBucket<?>, Exception> callback;


    public RedisUpdate(RedissonClient client, String key, Object value, Callback<RBucket<?>, Exception> callback) {
        this.client = client;
        this.key = key;
        this.value = value;
        this.callback = callback;
    }

    @Override
    public void run() {
        bucket = client.getBucket(key);
        if(bucket == null) {
            try {
                throw new Exception("Could not find a redis object with the key: " + key);
            } catch (Exception e) {
                e.printStackTrace();
                callback.call(null, e);
            }
            return;
        }
        bucket.setAsync(value);
        callback.call(bucket, null);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public RBucket<Object> getBucket() {
        return bucket;
    }

    public void setRedissonClient(RedissonClient client) {
        this.client = client;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
