package com.autohome.resadmin.util;


import com.google.common.base.Strings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Menong on 2015/9/17.
 * Redis client
 */
public class RedisClient {

    private JedisPool pool;

    private int expired;

    public RedisClient(JedisPoolConfig config, String ip, Integer port, int timeOut, int expired) {
        this.pool = new JedisPool(config, ip, port, timeOut);
        this.expired = expired;
    }

    public String getCustomKey(Object id, Class<?> clazz) {
        return clazz.getName() + "#" + id;
    }

    public <K> String getCustomKey(K id, Class<?> clazz, Function<K, String> getKey) {
        return getCustomKey(getKey.apply(id), clazz);
    }

    public <K> String[] getCustomKeys(List<K> ids, Class<?> clazz) {
        return getCustomKeys(ids, clazz, K::toString);
    }

    public <K> String[] getCustomKeys(List<K> ids, Class<?> clazz, Function<K, String> getKey) {
        String[] keys = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            keys[i] = getCustomKey(getKey.apply(ids.get(i)), clazz);
        }
        return keys;
    }

    public <K, V> String set(K id, V object, Function<K, String> getKey) {
        return set(getKey.apply(id), object, expired);
    }

    public String set(final String key, Object object) {
        return set(key, object, expired);
    }

    public String set(final String key, Object object, int seconds) {
        try (Jedis jedis = pool.getResource()) {
            String value = JsonHelper.Serialize(object);
            return jedis.setex(key, seconds, value);
        }
    }

    public <K, V> void mset(Map<K, V> map, Class<V> clazz, int seconds) {
        try (Jedis jedis = pool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                String key = getCustomKey(entry.getKey(), clazz);
                pipeline.set(key, JsonHelper.Serialize(entry.getValue()));
                pipeline.expire(key, seconds);
            }
            pipeline.syncAndReturnAll();
        }
    }

    public <K, V> void hmset(Map<K, Map<String, V>> map, Class<V> clazz, int seconds) {
        if (MapExtensions.isEmpty(map))
            return;

        try (Jedis jedis = pool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<K, Map<String, V>> entry : map.entrySet()) {
                String key = getCustomKey(entry.getKey(), clazz);
                Map<String, String> value = toStringMap(entry.getValue());
                if (MapExtensions.isEmpty(value))
                    continue;

                pipeline.hmset(key, value);
                pipeline.expire(key, seconds);
            }
            pipeline.syncAndReturnAll();
        }
    }

    private Map<String, String> toStringMap(Map<String, ?> map) {
        if (MapExtensions.isEmpty(map))
            return null;

        Map<String, String> pairs = new LinkedHashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            pairs.put(entry.getKey(), JsonHelper.Serialize(entry.getValue()));
        }
        return pairs;
    }

    public Long increment(final String key, Long value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.incrBy(key, value);
        }
    }

    public Double increment(final String key, Double value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.incrByFloat(key, value);
        }
    }

    public String get(final String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    public <K, V> V get(K key, Class<V> clazz, Function<K, V> reload) {
        return get(key, clazz, id -> getCustomKey(id, clazz), reload);
    }

    public <T> T get(Supplier<String> getKey, Class<T> clazz, Supplier<T> reload) {
        String value = get(getKey.get());
        if (!Strings.isNullOrEmpty(value)) {
            return JsonHelper.DeSerialize(value, clazz);
        }

        if (reload == null)
            return null;

        T object = reload.get();
        if (object == null)
            return null;

        set(getKey.get(), object);
        return object;
    }

    public <K, V> V get(K key, Class<V> clazz, Function<K, String> getKey, Function<K, V> reload) {
        String value = get(getKey.apply(key));
        if (!Strings.isNullOrEmpty(value)) {
            return JsonHelper.DeSerialize(value, clazz);
        }

        if (reload == null)
            return null;

        V object = reload.apply(key);
        if (object == null)
            return null;

        set(key, object, getKey);
        return object;
    }

    public List<String> mget(String... keys) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.mget(keys);
        }
    }

    public <V> Map<Integer, V> mget(List<Integer> keys, Class<V> clazz, Function<List<Integer>, Map<Integer, V>> reload) {
        return mget(keys, clazz, Object::toString, reload);
    }

    public <K, V> Map<K, V> mget(List<K> keys, Class<V> clazz, Function<K, String> getKey, Function<List<K>, Map<K, V>> reload) {
        if (CollectionExtensions.isEmpty(keys))
            return null;

        Map<K, V> map = new LinkedHashMap<>(keys.size());
        List<K> notExists = new ArrayList<>(keys.size());
        List<String> values = mget(getCustomKeys(keys, clazz, getKey));
        for (int i = 0; i < keys.size(); i++) {
            String json = values.get(i);
            V value = null;
            if (!Strings.isNullOrEmpty(json)) {
                notExists.add(keys.get(i));
            } else {
                value = JsonHelper.DeSerialize(json, clazz);
            }
            map.put(keys.get(i), value);
        }

        if (notExists==null||notExists.isEmpty()) {
            return map;
        }

        Map<K, V> db = reload.apply(notExists);
        if (MapExtensions.isNotEmpty(db))
            db.forEach(map::put);

        return map;
    }

    public <K, V> Map<K, Map<String, V>> hgetAll(List<K> keys,
                                                 Class<V> clazz,
                                                 Function<List<K>, Map<K, Map<String, V>>> reload) {
        return hgetAll(keys, clazz, Object::toString, reload);
    }

    public <K, V> Map<K, Map<String, V>> hgetAll(List<K> keys,
                                                 Class<V> clazz,
                                                 Function<K, String> getKey,
                                                 Function<List<K>, Map<K, Map<String, V>>> reload) {
        if (CollectionExtensions.isEmpty(keys))
            return null;

        Map<K, Map<String, V>> map = new LinkedHashMap<>(keys.size());
        List<K> notExists = new ArrayList<>(keys.size());
        List<Object> pipeReturn;
        try (Jedis jedis = pool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            keys.forEach(key -> pipeline.hgetAll(getCustomKey(key, clazz, getKey)));
            pipeReturn = pipeline.syncAndReturnAll();
        }

        for (int i = 0; i < keys.size(); i++) {
            Object o = pipeReturn.get(i);
            if (!(o instanceof Map)) {
                notExists.add(keys.get(i));
                continue;
            }

            Map<String, String> hash = (Map<String, String>) o;
            if (MapExtensions.isEmpty(hash)) {
                notExists.add(keys.get(i));
                continue;
            }

            Map<String, V> value = new LinkedHashMap<>();
            map.put(keys.get(i), value);
            hash.forEach(
                    (key, val) -> value.put(key, JsonHelper.DeSerialize(val, clazz))
            );
        }

        if (notExists==null|| notExists.isEmpty()) {
            return map;
        }

        Map<K, Map<String, V>> db = reload.apply(notExists);
        if (MapExtensions.isNotEmpty(db))
            db.forEach(map::put);

        return map;
    }
}
