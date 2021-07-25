package com.xkcoding.cache.redis.lock;

/**
 * @author xugangq
 * @description
 * @createTime 2020/8/27 19:07
 */
public interface IRedisLockService {
    /**
     * 上锁，默认重试5次；默认的锁的时间是 10s；
     * @param lock 锁名
     * @param val 一般为唯一id
     * @return
     */
    boolean tryLock(String lock, String val);

    /**
     *  上锁
     * @param lock
     * @param val
     * @param second
     * @return
     */
    boolean tryLock(String lock, String val, int second);

    /**
     * 解锁
     * @param lock
     * @param val
     */
    void unlock(String lock, String val);
}
