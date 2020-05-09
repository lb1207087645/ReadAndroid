package com.graduation.android.readme.base.core;

import android.text.TextUtils;

import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.base.utils.Utils;

import java.lang.reflect.Type;

import okio.ByteString;

/**
 * desc: 缓存管理类
 * Created by lyt
 * email:lytjackson@gmail.com
 */
public class CacheCore {

    private LruDiskCache disk;

    public CacheCore(LruDiskCache disk) {
        this.disk = Utils.checkNotNull(disk, "disk==null");
    }


    /**
     *
     * 读取
     */
    public synchronized <T> T load(Type type, String key, long time) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        L.e("rxcache", "CacheCore loadCache  key=" + key);
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        if (disk != null) {
            T result = disk.load(type, cacheKey, time);
            L.e("rxcache", "CacheCore loadCache  result=" + result);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * 保存
     */
    public synchronized <T> boolean save(String key, T value) {
        if (TextUtils.isEmpty(key) || null == value) {
            return false;
        }
        L.e("rxcache", "CacheCore saveCache  key=" + key);
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        return disk.save(cacheKey, value);
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    public synchronized boolean containsKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        L.e("rxcache", "CacheCore containsCache  key=" + key);
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        if (disk != null) {
            if (disk.containsKey(cacheKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public synchronized boolean remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        L.e("rxcache", "CacheCore removeCache  key=" + key);
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        if (disk != null) {
            return disk.remove(cacheKey);
        }
        return true;
    }

    /**
     * 清空缓存
     */
    public synchronized boolean clear() {
        if (disk != null) {
            return disk.clear();
        }
        return false;
    }

}
