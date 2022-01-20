package com.xkcoding.cache.redis.lock;

/**
 * @author xugangq
 * @date 2022/1/20 20:18
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加注释
 *
 * @author Paul.Yang E-mail:yaboocn@qq.com
 * @version 1.0 2016-7-28
 * @since 1.7
 */
@Slf4j
public class LockHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    /**
     * jedis模板
     */
    private static RedisTemplate redisTemplate = null;

    //加锁标志
    private static final byte[] LOCKED = new byte[] {'Y'};

    /**
     * 一毫秒等于的纳秒数
     */
    public static final long ONE_MILLI_NANOS = 1000000L;

    /**
     * 默认超时时间（毫秒）
     */
    public static final long DEFAULT_TIME_OUT = 3000;

    /**
     * 锁的超时时间（秒），过期删除
     */
    public static final int EXPIRE = 5 * 60;

    /** 线程变量 */
    private static ThreadLocal<Map<String, Integer>> locks = new ThreadLocal<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 按键值增加锁
     *
     * @param key 锁的键值
     * @param timeout 超时时长
     * @param expire 锁过期时间
     * @return 是否锁定成功
     */
    public static boolean lock(String key, long timeout, int expire) {
        //解决嵌套加锁
        Map<String, Integer> ls = locks.get();
        if (ls != null) {
            Integer c = ls.get(key);
            if (c != null) {
                c++;
                ls.put(key, c);
                return true;
            }
        } else {
            ls = new HashMap<>();
            locks.set(ls);
        }
        Boolean rs = (Boolean)getRedisTemplate().execute(new LockRedisCallback(key, timeout, expire));
        boolean flag = rs != null && rs;
        if (flag) {
            ls.put(key, 1);
        }
        return flag;
    }

    /**
     * 按键值增加锁
     *
     * @param key 锁的键值
     * @param timeout 超时时长
     * @return 是否锁定成功
     */
    public static boolean lock(String key, long timeout) {
        return lock(key, timeout, EXPIRE);
    }

    /**
     * 按键值增加锁
     *
     * @param key 锁的键值
     * @return 是否锁定成功
     */
    public static boolean lock(String key) {
        return lock(key, DEFAULT_TIME_OUT);
    }

    /**
     * 解锁指定的键值
     */
    public static void unlock(String key) {
        //解决嵌套加锁
        Map<String, Integer> ls = locks.get();
        if (ls == null)
            return;
        Integer c = ls.get(key);
        if (c == null) {
            return;
        }
        if (c > 1) {
            c--;
            ls.put(key, c);
            return;
        }
        ls.remove(key);
        if (ls.isEmpty())
            locks.remove();

        getRedisTemplate().execute(new UnLockRedisCallback(key));
    }

    /**
     * 清除线程占用的所有的锁
     */
    public void clearLock() {
        Map<String, Integer> ls = locks.get();
        for (String key : ls.keySet()) {
            unlock(key);
        }
        locks.remove();
    }

    /**
     * 排他独占锁
     *
     * @param key 独占键值
     * @param expire 超期时长
     * @return 布尔值
     */
    public static boolean exclusive(String key, int expire) {
        Boolean rs = (Boolean)getRedisTemplate().execute(new ExclusiveRedisCallback(key, expire));
        return rs != null && rs;
    }

    /**
     * 排他独占锁
     *
     * @param key 键值
     * @return 布尔值
     */
    public static boolean exclusive(String key) {
        return exclusive(key, EXPIRE);
    }

    /**
     * 释放排他独占锁
     *
     * @param key 键值
     */
    public static void releaseExcusive(String key) {
        getRedisTemplate().execute(new UnLockRedisCallback(key));
    }

    /**
     * 获取redis访问模板
     *
     * @return　redis访问模板
     */
    public static RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = applicationContext.getBean(RedisTemplate.class);
            if (redisTemplate == null) {
                Map<String, RedisTemplate> beans = applicationContext.getBeansOfType(RedisTemplate.class);
                if (!beans.isEmpty())
                    redisTemplate = beans.values().iterator().next();
            }
        }
        if (redisTemplate == null) {
            log.error("redis config is error! please init RedisTemplate bean!");
            throw new RuntimeException("redis config is error! please init RedisTemplate bean!");
        }
        return redisTemplate;
    }

    /**
     * 加锁回调指令
     */
    public static class LockRedisCallback implements RedisCallback<Boolean> {
        private long timeout;
        private byte[] key;
        private int expire;

        public LockRedisCallback(String key, long timeout, int expire) {
            this.timeout = timeout;
            this.expire = expire;
            this.key = redisTemplate.getStringSerializer().serialize(key);
        }

        /**
         * 加锁方法
         *
         * @param connection redis连接
         * @return 布尔值
         */
        public Boolean doInRedis(RedisConnection connection) {
            long nano = System.nanoTime();
            timeout *= ONE_MILLI_NANOS;
            try {
                while ((System.nanoTime() - nano) < timeout) {
                    connection.watch(key);
                    // 开启watch之后，如果key的值被修改，则事务失败，exec方法返回null
                    byte[] value = connection.get(key);
                    if (value == null) {
                        connection.multi();
                        connection.setEx(key, expire, LOCKED);
                        connection.get(key);
                        List<Object> rs = connection.exec();
                        if (rs != null && rs.size() > 0) {
                            connection.unwatch();
                            return true;
                        }
                    }
                    connection.unwatch();
                    // 短暂休眠，nano避免出现活锁
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                log.error("lock error happened", e);
            }
            return false;
        }
    }

    /**
     * 解锁回调指令
     */
    public static class UnLockRedisCallback implements RedisCallback<Boolean> {
        private byte[] key;

        public UnLockRedisCallback(String key) {
            try {
                this.key = key.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.info("");
            }
        }

        public Boolean doInRedis(RedisConnection connection) {
            connection.del(key);
            return true;
        }
    }

    /**
     * 排他独占锁回调，无需解锁
     */
    public static class ExclusiveRedisCallback implements RedisCallback<Boolean> {
        private byte[] key;
        private int expire;

        public ExclusiveRedisCallback(String key, int expire) {
            this.expire = expire;
            this.key = redisTemplate.getStringSerializer().serialize(key);
        }

        /**
         * 加锁方法
         *
         * @param connection redis连接
         * @return 布尔值
         */
        public Boolean doInRedis(RedisConnection connection) {
            try {
                connection.watch(key);
                // 开启watch之后，如果key的值被修改，则事务失败，exec方法返回null
                byte[] value = connection.get(key);
                if (value != null)
                    return false;
                connection.multi();
                connection.setEx(key, expire, LOCKED);
                connection.get(key);
                List<Object> rs = connection.exec();
                if (rs != null && rs.size() > 0) {
                    return true;
                }
            } catch (Exception e) {
                log.error("lock error happened", e);
            } finally {
                connection.unwatch();
            }
            return false;
        }
    }
}
