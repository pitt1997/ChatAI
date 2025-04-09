package com.lijs.chatai.common.cache.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lijs.chatai.common.cache.config.RedissonConfig;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ljs
 * @date 2024-10-10
 * @description Redis缓存操作接口
 */
@Component
@ConditionalOnClass(RedissonClient.class)
public class RedisCacheService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedissonConfig redissonConfig;
    @Autowired
    private ObjectMapper objectMapper;

    public RedissonClient getRedissonClient() {
        if (redissonClient == null || redissonClient.isShutdown()) {
            try {
                redissonClient = redissonConfig.redisson();
            } catch (IOException e) {
                logger.error("Error initializing Redisson client", e);
                throw new RuntimeException(e);
            }
        }
        return redissonClient;
    }

    /**
     * 模糊删除
     */
    public long deleteByPattern(String pattern) {
        return getRedissonClient().getKeys().deleteByPattern(getKey(pattern));
    }

    /**
     * 模糊查询
     */
    public Iterable<String> getKeysByPattern(String pattern) {
        return getRedissonClient().getKeys().getKeysByPattern(getKey(pattern));
    }

    /**
     * 根据key获取到对应value值, 若key不存在, 则返回null。
     */
    private <T> T get(String key) {
        return getRedissonClient().<T>getBucket(getKey(key)).get();
    }

    /**
     * 根据key，获取到对应的value值
     *
     * @param key        key
     * @param valueClass value的class类型
     * @return 该key对应的值，如果key不存在，则返回 null。
     */
    public <T> T get(String key, Class<T> valueClass) {
        T t = getRedissonClient().<T>getBucket(getKey(key)).get();
        if (t != null) {
            return convertValue(t, valueClass);
        }
        return null;
    }

    /**
     * 设置缓存对象
     */
    public void set(String key, Object obj) {
        getBucket(key).set(obj);
    }

    /**
     * 设置缓存对象及其过期时间
     */
    public void setWithTTL(String key, Object obj, long timeout, TimeUnit unit) {
        getBucket(key).set(obj, timeout, unit);
    }

    /**
     * 给指定的key设置过期时间
     */
    public boolean setExpire(String key, long timeout, TimeUnit unit) {
        return getBucket(key).expire(timeout, unit);
    }

    /**
     * 如果key已存在，则不作任何操作, 并且返回 false。
     * 如果key不存在, 则添加, 成功返回 true，失败返回 false。
     */
    public boolean setIfExists(String key, Object value) {
        return getBucket(key).setIfExists(value);
    }

    /**
     * 如果key已存在，则不作任何操作, 并且返回 false。
     * 如果key不存在, 则添加, 成功返回 true，失败返回 false。
     */
    public boolean setIfExistsWithTTL(String key, Object value, long timeout, TimeUnit unit) {
        return getBucket(key).setIfExists(value, timeout, unit);
    }

    /**
     * 批量获取多个key的值
     */
    private <E> Map<String, E> multiGet(List<String> keys) {
        RBuckets buckets = getRedissonClient().getBuckets();
        if (buckets == null) {
            return null;
        }
        List<String> keyList = new ArrayList<>();
        for (String key : keys) {
            keyList.add(getKey(key));
        }
        return buckets.get(keyList.toArray(new String[0]));
    }

    /**
     * 批量获取多个key的值
     */
    public <E> Map<String, E> multiGet(List<String> keys, Class<E> valueClass) {
        RBuckets buckets = getRedissonClient().getBuckets();
        if (buckets == null) {
            return null;
        }
        List<String> keyList = new ArrayList<>();
        for (String key : keys) {
            keyList.add(getKey(key));
        }
        Map<String, Object> stringObjectMap = buckets.get(keyList.toArray(new String[0]));
        if (stringObjectMap == null) {
            return null;
        }
        Map<String, E> resultMap = new HashMap<>();
        stringObjectMap.forEach((key, value) -> {
            if (value != null) {
                resultMap.put(key, convertValue(value, valueClass));
            }
        });
        return resultMap;
    }

    /**
     * 批量设置 key-value
     * 如果存在相同的key, 则覆盖原来的key-value。
     *
     * @param maps key-value 集
     */
    public void multiSet(Map<String, ?> maps) {
        getRedissonClient().getBuckets().set(maps);
    }

    /**
     * key是否存在
     */
    public boolean isExist(String key) {
        return getBucket(key).isExists();
    }

    /**
     * 根据key删除
     */
    public boolean delete(String key) {
        return getBucket(key).delete();
    }

    /**
     * 整数新增
     */
    public long incrBy(String key, long increment) {
        return getRedissonClient().getAtomicLong(getKey(key)).addAndGet(increment);
    }

    /**
     * 插入集合对象
     */
    public <T> boolean setCollection(String key, Collection<T> collection) {
        RQueue<T> queue = getRedissonClient().getQueue(getKey(key));
        queue.clear();
        return queue.addAll(collection);
    }

    /**
     * 插入集合对象
     */
    public <T> boolean addCollection(String key, Collection<T> collection) {
        return getRedissonClient().<T>getQueue(getKey(key)).addAll(collection);
    }

    /**
     * 插入集合对象
     */
    public <T> boolean addCollection(String key, T element) {
        return getRedissonClient().<T>getQueue(getKey(key)).add(element);
    }

    /**
     * 读取集合对象
     */
    public <T> List<T> getCollection(String key, Class<T> valueClass) {
        RQueue<Object> queue = getRedissonClient().getQueue(getKey(key));
        return queue.readAll().stream()
                .map(item -> convertValue(item, valueClass))
                .collect(Collectors.toList());
    }

    /**
     * 从左端推入元素进列表
     * 若redis中不存在对应的key, 那么会自动创建
     *
     * @param key  定位list的key
     * @param item 要推入list的元素
     * @return 是否成功
     */
    public Boolean listLeftPush(String key, String item) {
        return getRedissonClient().getList(getKey(key)).add(item);
    }

    /**
     * 查询list所有缓存
     */
    private <T> List<T> getAllList(String key) {
        return getRedissonClient().<T>getList(getKey(key)).readAll();
    }

    /**
     * 查询list所有缓存
     */
    public <T> List<T> getAllList(String key, Class<T> valueClass) {
        List<Object> collection = getAllList((key));
        List<T> resultCollection = Lists.newArrayList();
        if (!collection.isEmpty()) {
            collection.forEach(element -> resultCollection.add(convertValue(element, valueClass)));
        }
        return resultCollection;
    }

    /**
     * 向key对应的set中添加items
     * 1、若key不存在，则会自动创建。
     * 2、set中的元素会去重。
     *
     * @param key   定位set的key
     * @param items 要向(key对应的)set中添加的items
     * @return 此次添加操作, 添加到set中的元素的个数
     */
    public Boolean setAdd(String key, List<String> items) {
        return getRedissonClient().getSet(getKey(key)).addAll(items);
    }

    /**
     * 从key对应的set中删除items
     *
     * @param key   定位set的key
     * @param items 要从(key对应的)set中删除的items
     * @return 是否删除成功
     */
    public Boolean setRemove(String key, List<String> items) {
        RSet<Object> set = getRedissonClient().getSet(getKey(key));
        if (set.isExists()) {
            return set.removeAll(items);
        }
        return false;
    }

    /**
     * 查询set所有值
     */
    private <T> Set<T> sGetAll(String key) {
        return getRedissonClient().<T>getSet(getKey(key)).readAll();
    }

    /**
     * 查询set所有值
     */
    public <T> Set<T> setGetAll(String key, Class<T> valueClass) {
        Set<Object> set = sGetAll(key);
        Set<T> resultSet = Sets.newHashSet();
        if (!set.isEmpty()) {
            set.forEach(element -> resultSet.add(convertValue(element, valueClass)));
        }
        return resultSet;
    }

    /**
     * 向key对应的zset中添加(item, score)
     */
    public boolean zsetAdd(String key, String item, double score) {
        return getRedissonClient().getScoredSortedSet(getKey(key)).add(score, item);
    }

    public Collection<String> zsetRangeWithScores(String key, long startScore, long endScore) {
        RScoredSortedSet<String> scoredSortedSet = getRedissonClient().getScoredSortedSet(getKey(key));
        if (scoredSortedSet == null) {
            return new HashSet<>();
        }
        return scoredSortedSet.valueRange(startScore, true, endScore, true);
    }

    /**
     * hash类型设值
     */
    public boolean hSet(String key, Object field, Object value) {
        String k = getKey(key);
        if (k == null || k.isEmpty()) {
            return false;
        }
        if (value == null) {
            return false;
        }
        RMap<Object, Object> map = getRedissonClient().getMap(k);
        return map.fastPut(field, value);
    }

    /**
     * hash类型获取值（指定类型）
     */
    public <T> T hGet(String key, Object field, Class<T> valueClass) {
        Object o = hGet(key, field);
        if (o != null) {
            return convertValue(o, valueClass);
        }
        return null;
    }

    /**
     * hash类型获取值
     */
    private <E> E hGet(String key, Object field) {
        String k = getKey(key);
        if (k == null || k.isEmpty()) {
            return null;
        }
        RMap<Object, E> map = getRedissonClient().getMap(k);
        if (map == null) {
            return null;
        }
        return map.get(field);
    }

    /**
     * hash类型获取全部值
     */
    private <E> Map<String, E> hGetAll(String key) {
        String k = getKey(key);
        if (k == null || k.isEmpty()) {
            return null;
        }
        RMap<String, E> map = getRedissonClient().getMap(k);
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.readAllMap();
    }

    /**
     * hash类型获取全部值（指定类型）
     */
    public <E> Map<String, E> hGetAll(String key, Class<E> valueClass) {
        Map<String, Object> objectMap = hGetAll((key));
        if (objectMap == null) {
            return null;
        }
        Map<String, E> resultMap = new HashMap<>();
        objectMap.forEach((k, v) -> {
            if (v != null) {
                resultMap.put(k, convertValue(v, valueClass));
            }
        });
        return resultMap;
    }

    /**
     * hash类型删除field
     */
    public boolean hDel(String key, Object field) {
        String k = getKey(key);
        if (k == null || k.isEmpty()) {
            return false;
        }
        RMap<Object, Object> map = getRedissonClient().getMap(k);
        if (map == null || map.isEmpty()) {
            return false;
        }
        if (map.containsKey(field)) {
            map.fastRemove(field);
        }
        return true;
    }

    public <T> T convertValue(Object value, Class<T> valueClass) {
        try {
            return objectMapper.convertValue(value, valueClass);
        } catch (IllegalArgumentException e) {
            logger.error("Error converting value: {}", value, e);
            return null;
        }
    }

    /**
     * 通用的 Bucket 获取方法，减少重复代码
     */
    private <T> RBucket<T> getBucket(String key) {
        return getRedissonClient().getBucket(getKey(key));
    }

    /**
     * 清空所有键值对
     */
    public void flushAll() {
        getRedissonClient().getKeys().flushall();
    }

}
