package com.graduation.android.readme.base.core;

import com.graduation.android.readme.base.utils.Utils;

import java.lang.reflect.Type;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * desc: 缓存的基类
 * Created by lyt
 * email:lytjackson@gmail.com
 */
public abstract class BaseCache {
    private final ReadWriteLock mLock = new ReentrantReadWriteLock();

    /**
     * 读取缓存
     *
     * @param key       缓存key
     * @param existTime 缓存时间
     */
    final <T> T load(Type type, String key, long existTime) {
        //1.先检查key
        Utils.checkNotNull(key, "key == null");

        //2.判断key是否存在,key不存在去读缓存没意义
        if (!containsKey(key)) {
            return null;
        }

        //3.判断是否过期，过期自动清理
        if (isExpiry(key, existTime)) {
            remove(key);
            return null;
        }

        //4.开始真正的读取缓存
        mLock.readLock().lock();
        try {
            // 读取缓存
            return doLoad(type, key);
        } finally {
            mLock.readLock().unlock();
        }
    }

    /**
     * 保存缓存
     *
     * @param key   缓存key
     * @param value 缓存内容
     * @return
     */
    final <T> boolean save(String key, T value) {
        //1.先检查key
        Utils.checkNotNull(key, "key == null");

        //2.如果要保存的值为空,则删除
        if (value == null) {
            return remove(key);
        }

        //3.写入缓存
        boolean status = false;
        mLock.writeLock().lock();
        try {
            status = doSave(key, value);
        } finally {
            mLock.writeLock().unlock();
        }
        return status;
    }

    /**
     * 删除缓存
     */
    final boolean remove(String key) {
        mLock.writeLock().lock();
        try {
            return doRemove(key);
        } finally {
            mLock.writeLock().unlock();
        }
    }

    /**
     * 清空缓存
     */
    final boolean clear() {
        mLock.writeLock().lock();
        try {
            return doClear();
        } finally {
            mLock.writeLock().unlock();
        }
    }

    /**
     * 是否包含 加final 是让子类不能被重写，只能使用doContainsKey<br>
     * 这里加了锁处理，操作安全。<br>
     *
     * @param key 缓存key
     * @return 是否有缓存
     */
    public final boolean containsKey(String key) {
        mLock.readLock().lock();
        try {
            return doContainsKey(key);
        } finally {
            mLock.readLock().unlock();
        }
    }

    /**
     * 是否包含  采用protected修饰符  被子类修改
     *
     * @param key
     * @return
     */
    protected abstract boolean doContainsKey(String key);

    /**
     * 是否过期
     *
     * @param key
     * @param existTime
     * @return
     */
    protected abstract boolean isExpiry(String key, long existTime);

    /**
     * 读取缓存
     *
     * @param type
     * @param key
     * @param <T>
     * @return
     */
    protected abstract <T> T doLoad(Type type, String key);

    /**
     * 保存
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    protected abstract <T> boolean doSave(String key, T value);

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    protected abstract boolean doRemove(String key);

    /**
     * 清空缓存
     *
     * @return
     */
    protected abstract boolean doClear();
}
