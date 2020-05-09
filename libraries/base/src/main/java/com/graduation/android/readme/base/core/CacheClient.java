package com.graduation.android.readme.base.core;

import android.text.TextUtils;


import com.graduation.android.readme.base.CommonApplication;
import com.graduation.android.readme.base.model.CacheMode;
import com.graduation.android.readme.base.utils.Utils;

import java.io.File;
import java.lang.reflect.Type;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 缓存的单例httpclient
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 * 参考项目：https://github.com/HolenZhou/rxcache
 */
public class CacheClient {

    /**
     * 缓存过期时间，默认永久缓存
     */
    public static final int DEFAULT_CACHE_NEVER_EXPIRE = -1;

    private static CacheClient instance;
    private RxCache.Builder mCacheBuilder;
    /**
     * 缓存类型
     */
    private CacheMode mCacheMode = CacheMode.NO_CACHE;
    /**
     * 缓存key
     */
//    private String mCacheKey;
    /**
     * 缓存时间
     */
    private long mGlobalCacheTime = DEFAULT_CACHE_NEVER_EXPIRE;
    /**
     * 缓存的转换器
     */
    private IDiskConverter diskConverter;
    /**
     * 缓存目录
     */
    private File mCacheDirectory;
    /**
     * 缓存大小
     */
    private long mCacheMaxSize;

    /**
     * 接口请求key与接口完整请求地址的map
     */
    private final ConcurrentMap<String, String> mCachePathMap;
    /**
     * 接口缓存key与时间的map
     */
    private final ConcurrentMap<String, Long> mCacheTimeMap;
    /**
     * 接口缓存key与sign的map
     */
    private final ConcurrentMap<String, String> mCacheSignMap;

    public CacheClient() {
        mCachePathMap = new ConcurrentHashMap<>();
        mCacheTimeMap = new ConcurrentHashMap<>();
        mCacheSignMap = new ConcurrentHashMap<>();
        mCacheBuilder = new RxCache.Builder().init(CommonApplication.getContext()).diskConverter(new GsonDiskConverter());
    }

    public static CacheClient getInstance() {
        if (instance == null) {
            synchronized (CacheClient.class) {
                if (instance == null) {
                    instance = new CacheClient();
                }
            }
        }
        return instance;
    }

    /**
     * 清空缓存的key，未清除本地缓存文件，但效果等同，没有key，就找不到对应缓存了
     */
    public void clearCache() {
        if (mCachePathMap != null) {
            mCachePathMap.clear();
        }
        if (mCacheTimeMap != null) {
            mCacheTimeMap.clear();
        }
        if (mCacheSignMap != null) {
            mCacheSignMap.clear();
        }
    }

    public RxCache.Builder getRxCacheBuilder() {
        return mCacheBuilder;
    }

    public RxCache getRxCache() {
        return getRxCacheBuilder().build();
    }

    /**
     * 全局的缓存模式
     */
    public CacheClient setCacheMode(CacheMode cacheMode) {
        this.mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /**
     * 设置全局的缓存过期时间
     *
     * @param cacheTime
     */
    public CacheClient setCacheTime(long cacheTime) {
        this.mGlobalCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存过期时间，默认-1L
     *
     * @return
     */
    public long getCacheTime() {
        return mGlobalCacheTime;
    }

    /**
     * 获取某个key的缓存过期时间，没有则返回全局配置的值
     */
    public synchronized long getCacheTime(String key) {
        if (!TextUtils.isEmpty(key) && mCacheTimeMap != null) {
            Long val = mCacheTimeMap.get(key);
            return null == val ? mGlobalCacheTime : val;
        }
        return mGlobalCacheTime;
    }

    /**
     * 设置某个key的缓存过期时间
     */
    public synchronized CacheClient setCacheTime(String key, long cacheTime) {
        if (!TextUtils.isEmpty(key) && mCacheTimeMap != null) {
            if (cacheTime <= -1L) {
                cacheTime = mGlobalCacheTime;
            }
            mCacheTimeMap.put(key, cacheTime);
        }
        return this;
    }

    /**
     * 全局的缓存大小,默认50M
     */
    public CacheClient setCacheMaxSize(long maxSize) {
        this.mCacheMaxSize = maxSize;
        mCacheBuilder.diskMax(maxSize);
        return this;
    }

    /**
     * 获取全局的缓存大小
     */
    public long getCacheMaxSize() {
        return mCacheMaxSize;
    }

    /**
     * 全局设置缓存的版本，默认为1，缓存的版本号
     */
    public CacheClient setCacheVersion(int cacheVersion) {
        if (cacheVersion < 0) {
            throw new IllegalArgumentException("cache version must > 0");
        }
        mCacheBuilder.appVersion(cacheVersion);
        return this;
    }

    /**
     * 全局设置缓存的路径，默认是应用包下面的缓存
     */
    public CacheClient setCacheDirectory(File directory) {
        mCacheDirectory = Utils.checkNotNull(directory, "directory == null");
        mCacheBuilder.diskDir(directory);
        return this;
    }

    /**
     * 获取缓存的路径
     */
    public File getCacheDirectory() {
        return mCacheDirectory;
    }

    /**
     * 全局设置缓存的转换器
     */
    public CacheClient setCacheDiskConverter(IDiskConverter converter) {
        this.diskConverter = Utils.checkNotNull(converter, "converter == null");
        mCacheBuilder.diskConverter(diskConverter);
        return this;
    }

    /**
     * 全局设置缓存的转换器
     */
    public IDiskConverter getCacheDiskConverter() {
        return diskConverter;
    }

    /**
     * 暂存接口短路径缓存key，在拦截器NetworkInterceptor中会进行路径转换更新
     *
     * @param key
     */
    public synchronized void setCacheKey(String key) {
        putCache(key, key);
    }

    /**
     * 存储key与完整路径
     *
     * @param key 接口缓存key
     * @param val 接口完整地址
     */
    public synchronized void putCache(String key, String val) {
        if (mCachePathMap != null) {
            mCachePathMap.put(key, val);
        }
    }

    /**
     * 获取对应key与完整路径value
     *
     * @param key
     * @return
     */
    public synchronized String getCache(String key) {
        if (mCachePathMap != null) {
            return mCachePathMap.get(key);
        }
        return null;
    }

    /**
     * 是否存在对应key
     *
     * @param key
     * @return
     */
    public synchronized boolean containCacheKey(String key) {
        if (mCachePathMap != null) {
            return mCachePathMap.containsKey(key);
        }
        return false;
    }

    /**
     * 缓存数据sign
     *
     * @param key
     * @param sign
     */
    public synchronized void putSign(String key, String sign) {
        if (mCacheSignMap != null && containCacheKey(key)) {
            mCacheSignMap.put(key, sign);
            //由于presenter处的key只传入短路径，此处需要将sign对应长短路径都进行存储
            mCacheSignMap.put(mCachePathMap.get(key), sign);
        }
    }

    public synchronized String getSign(String key) {
        if (mCacheSignMap != null) {
            return mCacheSignMap.get(key);
        }
        return null;
    }

    public void resetCache(final String oldKey, final String newKey, final Type type) {
        Observable.create(resetCacheInner(oldKey, newKey, type)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe();
    }

    private ObservableOnSubscribe<Boolean> resetCacheInner(final String oldKey, final String newKey, final Type type) {
        return new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                if (TextUtils.isEmpty(oldKey) || TextUtils.isEmpty(newKey) || oldKey.equals(newKey) || (mCachePathMap != null && mCachePathMap.containsKey(newKey))) {
                    e.onNext(false);
                } else {
                    long cacheTime = getCacheTime(oldKey);
                    Object val = getRxCache().getCacheCore().load(type, oldKey, cacheTime);
                    if (val == null) {
                        e.onNext(false);
                    } else {
                        if (mCachePathMap != null && mCachePathMap.containsKey(oldKey)) {
                            mCachePathMap.remove(oldKey);
                            mCachePathMap.put(newKey, newKey);
                        }
                        if (mCacheTimeMap != null && mCacheTimeMap.containsKey(oldKey)) {
                            mCacheTimeMap.remove(oldKey);
                            CacheClient.getInstance().setCacheTime(newKey, cacheTime);
                        }
                        if (mCacheSignMap != null && mCacheSignMap.containsKey(oldKey)) {
                            String oldSign = mCacheSignMap.get(oldKey);
                            mCacheSignMap.remove(oldKey);
                            mCacheSignMap.put(newKey, oldSign);
                        }
                        getRxCache().getCacheCore().save(newKey, val);
                        e.onNext(true);
                    }
                }
                e.onComplete();
            }
        };
    }

//    /**
//     * model中设置的key只是完整接口请求地址的部分，value为key或""<br>
//     * 找到后将value修改为完整地址<br>
//     * ps：http://api-nursery.babybus.co/Index/GetRecommendPageV130/aaaabbbb-->Index/GetRecommendPageV130/aaaabbbb<br>
//     *
//     * @param key
//     * @return 是否更新了key，value
//     */
//    public synchronized boolean updatePath(String key) {
//        boolean replace = false;
//        if (TextUtils.isEmpty(key)) {
//            return false;
//        }
//        if (mCachePathMap == null || mCachePathMap.size() <= 0) {
//            return false;
//        }
//        if (mCachePathMap.containsKey(key)) {
//            return true;
//        }
//        Set<String> sets = mCachePathMap.keySet();
//        String repKey = "";
//        for (String str : sets) {
//            if (key.contains(str)) {
//                repKey = str;
//                replace = true;
//                break;
//            }
//        }
//        if (replace) {
//            mCachePathMap.put(key, repKey);
////            mCachePathMap.remove(repKey);
//        } else {
//            mCachePathMap.put(key, key);
//        }
//        return replace;
//    }
}