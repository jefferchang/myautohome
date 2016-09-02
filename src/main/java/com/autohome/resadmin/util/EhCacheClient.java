package com.autohome.resadmin.util;


import com.google.common.base.Strings;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Menong on 2015/9/15.
 * EhCache 客户端
 */
public class EhCacheClient {

    private Cache cache;

    public Cache getCache() {
        return cache;
    }

    public EhCacheClient(CacheManager manager, String cacheName) {
        this.cache = manager.getCache(cacheName);
    }

    public <K, V> String getCacheKey(K Id, Class<V> clazz) {
        return clazz.getName() + "#" + Id;
    }

    public <K, V> void set(K key, V value) {
        cache.put(getCacheKey(key, value.getClass()), value);
    }

    public <K, V> void mset(Map<K, V> map) {
        if (map==null||map.isEmpty())
            return;

        for (Map.Entry<K, V> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public <K, V> V get(K key, Class<V> clazz) {
        return cache.get(getCacheKey(key, clazz), clazz);
    }

    public <K, V> V get(K key, Class<V> clazz, Function<K, V> reload) {
        String cacheKey = getCacheKey(key, clazz);
        V value = cache.get(cacheKey, clazz);
        if (value == null)
            value = reload.apply(key);

        return value;
    }

    public <K, V> Map<K, V> mget(List<K> keys, Class<V> clazz, Function<List<K>, Map<K, V>> reload) {
        Map<K, V> map = new LinkedHashMap<>();
        List<K> notExists = new ArrayList<>();
        for (K key : keys) {
            if (Strings.isNullOrEmpty(key.toString()))
                continue;

            V value = get(key, clazz);

            if (value == null) {
                notExists.add(key);
            } else {
                map.put(key, value);
            }
        }

        if (notExists==null||notExists.isEmpty())
            return map;

        Map<K, V> db = reload.apply(notExists);
        if (db==null||db.isEmpty())
            return map;

        for (Map.Entry<K, V> entry : db.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
