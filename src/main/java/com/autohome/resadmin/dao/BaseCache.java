package com.autohome.resadmin.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;




/**
 * 缓存，提供给数据缓存和缓存管理器
 * Created by xw.luan on 2016/8/31.
 */
public abstract class BaseCache<K, V> {

    //用于初始化cache的参数及其缺省值
    private int maximumSize = 1000;                 //最大缓存条数，子类在构造方法中调用setMaximumSize(int size)来更改
    private int expireAfterWriteDuration = 60;      //数据存在时长，子类在构造方法中调用setExpireAfterWriteDuration(int duration)来更改
    private TimeUnit timeUnit = TimeUnit.SECONDS;      //时间单位
    private Date resetTime;     //Cache初始化或被重置的时间
    private long highestSize=0; //历史最高记录数
    private Date highestTime;   //创造历史记录的时间

    private LoadingCache<K, V> cache;

    /**
     * 通过调用getCache().get(key)来获取数据
     * @return cache
     */
    public LoadingCache<K, V> getCache() {
        if(cache == null){  //使用双重校验锁保证只有一个cache实例
            synchronized (this) {
                if(cache == null){
                    cache = CacheBuilder.newBuilder()  //.maximumSize(maximumSize)   //缓存数据的最大条目，也可以使用.maximumWeight(weight)代替
                            .expireAfterWrite(expireAfterWriteDuration, timeUnit)   //数据被创建多久后被移除
                            .recordStats()                                          //启用统计
                            .build(new CacheLoader<K, V>() {
                                @Override
                                public V load(K key) throws Exception {
                                    return fetchData(key);
                                }
                            });
                    this.resetTime = new Date();
                    this.highestTime = new Date();
                }
            }
        }

        return cache;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     * @param key
     * @return value,连同key一起被加载到缓存中的。
     */
    protected abstract  V fetchData(K key);

    /**
     * 从缓存中获取数据（第一次自动调用fetchData从外部获取数据），并处理异常
     * @param key
     * @return Value
     * @throws ExecutionException
     */
    protected V getValue(K key) {
        V result = getCache().getUnchecked(key);
        return result;
    }

    public long getHighestSize() {
        return highestSize;
    }

    public Date getHighestTime() {
        return highestTime;
    }

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public int getExpireAfterWriteDuration() {
        return expireAfterWriteDuration;
    }

    /**
     * 设置最大缓存条数
     * @param maximumSize
     */
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    /**
     * 设置数据存在时长（分钟）
     * @param expireAfterWriteDuration
     */
    public void setExpireAfterWriteDuration(int expireAfterWriteDuration) {
        this.expireAfterWriteDuration = expireAfterWriteDuration;
    }

    /**
     * 设置时间单位
     * @param t
     */
    public void setTimeUnit(TimeUnit t){
        this.timeUnit = t;
    }
}


