package com.graduation.android.readme.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;


import com.graduation.android.readme.base.R;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目助手
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/12 下午5:15
 */

public class ProjectHelper {

    /**
     * 取得当前软件版本
     *
     * @return version code
     */
    public static int getCurrentVersion() {
        int versionCode = 0;
        try {
            PackageInfo info = Helper.getContext().getPackageManager()
                    .getPackageInfo(Helper.getContext().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 取得当前软件版本名称
     *
     * @return version name
     */
    public static String getCurrentVersionName() {
        String versionName = "";
        try {
            PackageInfo info = Helper.getContext().getPackageManager()
                    .getPackageInfo(Helper.getContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    // region 读取meta-data标签

    /**
     * 项目ID
     */
    private static int sProjectID = 0;
    /**
     * 产品ID
     */
    private static int sProductID = 0;
    /**
     * 产品Business ID
     */
    private static int sBusinessProductID = 0;
    /**
     * 渠道ID
     */
    private static int sChID = 0;
    /**
     * 渠道名
     */
    private static String sChCode = "";
    /**
     * 广告sdk版本
     */
    private static String sSdkVerName = "";
    /**
     * 广告sdk版本号
     */
    private static int sSdkVerCode = 0;
    /**
     * 推送类型
     */
    private static int sTokenType = -1;
    /**
     * md5签名key
     */
    private static String sSecretKey = "";
    /**
     * XXtea加密key
     */
    private static String sXxteaKey = "";
    /**
     * Des加密key
     */
    private static String sDesKey = "";
    /**
     * Business md5签名key
     */
    private static String sBusinessSecretKey = "";
    /**
     * Business XXtea加密key
     */
    private static String sBusinessXxteaKey = "";
    /**
     * Business Des加密key
     */
    private static String sBusinessDesKey = "";

    /**
     * 取得项目ID
     *
     * @return 项目ID
     */
    public static long getProjectID() {
        if (sProjectID == 0) {
            String key = Helper.getContext().getString(R.string.network_project_id);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sProjectID = Integer.parseInt((String.valueOf(obj)));
            } catch (NumberFormatException e) {
                return sProjectID = 0;
            }
        }
        return sProjectID;
    }

    public static void setProjectID(int projectID) {
        sProjectID = projectID;
    }

    /**
     * 取得产品ID
     *
     * @return 产品ID
     */
    public static long getProductID() {
        if (sProductID == 0) {
            String key = Helper.getContext().getString(R.string.network_product_id);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sProductID = Integer.parseInt((String.valueOf(obj)));
            } catch (NumberFormatException e) {
                return sProductID = 0;
            }
        }
        return sProductID;
    }

    public static void setProductID(int productID) {
        sProductID = productID;
    }

    /**
     * 取得产品ID
     *
     * @return 产品ID
     */
    public static long getBusinessProductID() {
        if (sBusinessProductID == 0) {
            String key = Helper.getContext().getString(R.string.network_business_product_id);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sBusinessProductID = Integer.parseInt((String.valueOf(obj)));
            } catch (NumberFormatException e) {
                return sBusinessProductID = 0;
            }
        }
        return sBusinessProductID;
    }

    public static void setBusinessProductID(int productID) {
        sBusinessProductID = productID;
    }

    /**
     * 取得当前软件渠道ID
     *
     * @return 渠道ID
     */
    public static int getChID() {
        if (sChID == 0) {
            String key = Helper.getContext().getString(R.string.network_ch_id);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sChID = Integer.parseInt(String.valueOf(obj));
            } catch (NumberFormatException e) {
                return sChID = 0;
            }
        }
        return sChID;
    }

    public static void setChID(int chID) {
        sChID = chID;
    }

    /**
     * 取得当前软件渠道名
     *
     * @return 渠道名
     */
    public static String getChCode() {
        if (TextUtils.isEmpty(sChCode)) {
            String key = Helper.getContext().getString(R.string.network_ch_code);
            Object obj = loadMetaData(Helper.getContext(), key);
            sChCode = String.valueOf(obj);
        }
        return ("null".equals(sChCode) || TextUtils.isEmpty(sChCode)) ? "Official" : sChCode;
    }

    public static void setChCode(String chCode) {
        sChCode = chCode;
    }

    /**
     * 广告sdk版本
     *
     * @return 渠道名
     */
    public static String getSdkVerName() {
        if (TextUtils.isEmpty(sSdkVerName)) {
            String key = Helper.getContext().getString(R.string.network_sdk_ver_name);
            Object obj = loadMetaData(Helper.getContext(), key);
            sSdkVerName = String.valueOf(obj);
        }
        return ("null".equals(sSdkVerName) || TextUtils.isEmpty(sSdkVerName)) ? "" : sSdkVerName;
    }

    public static void setSdkVerName(String sdkVerName) {
        sSdkVerName = sdkVerName;
    }

    /**
     * 广告sdk版本号
     *
     * @return 渠道名
     */
    public static int getSdkVerCode() {
        if (0 >= sSdkVerCode) {
            String key = Helper.getContext().getString(R.string.network_sdk_ver_code);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sSdkVerCode = Integer.parseInt(String.valueOf(obj));
            } catch (NumberFormatException e) {
                return sSdkVerCode = 0;
            }
        }
        return sSdkVerCode;
    }

    public static void setSdkVerCode(int sdkVerCode) {
        sSdkVerCode = sdkVerCode;
    }

    /**
     * 取得当前推送类型
     * <
     * <br/>极光推送：0（默认）
     * <br/>小米推送：1
     * </p>
     *
     * @return 推送类型
     */
    public static int getTokenType() {
        if (sTokenType < 0) {
            String key = Helper.getContext().getString(R.string.network_token_type);
            Object obj = loadMetaData(Helper.getContext(), key);
            try {
                return sTokenType = Integer.parseInt(String.valueOf(obj));
            } catch (NumberFormatException e) {
                return sTokenType = 0;
            }
        }
        return sTokenType;
    }

    public static void setTokenType(int tokenType) {
        sTokenType = tokenType;
    }

    /**
     * 取得md5签名key
     *
     * @return md5签名key
     */
    public static String getSecretKey() {
        if (TextUtils.isEmpty(sSecretKey)) {
            String key = Helper.getContext().getString(R.string.network_secret_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sSecretKey = String.valueOf(obj);
        }
        return ("null".equals(sSecretKey) || TextUtils.isEmpty(sSecretKey)) ? "Official" : sSecretKey;
    }

    public static void setSecretKey(String secretKey) {
        sSecretKey = secretKey;
    }

    /**
     * 取得Business md5签名key
     *
     * @return md5签名key
     */
    public static String getBusinessSecretKey() {
        if (TextUtils.isEmpty(sBusinessSecretKey)) {
            String key = Helper.getContext().getString(R.string.network_business_secret_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sBusinessSecretKey = String.valueOf(obj);
        }
        return ("null".equals(sBusinessSecretKey) || TextUtils.isEmpty(sBusinessSecretKey)) ? "Official" : sBusinessSecretKey;
    }

    public static void setBusinessSecretKey(String secretKey) {
        sBusinessSecretKey = secretKey;
    }

    /**
     * 取得xxtea加密key
     *
     * @return xxea加密key
     */
    public static String getXxteaKey() {
        if (TextUtils.isEmpty(sXxteaKey)) {
            String key = Helper.getContext().getString(R.string.network_xxtea_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sXxteaKey = String.valueOf(obj);
        }
        return ("null".equals(sXxteaKey) || TextUtils.isEmpty(sXxteaKey)) ? "Official" : sXxteaKey;
    }

    public static void setXxteaKey(String xxteaKey) {
        sXxteaKey = xxteaKey;
    }

    /**
     * 取得Business xxtea加密key
     *
     * @return xxea加密key
     */
    public static String getBusinessXxteaKey() {
        if (TextUtils.isEmpty(sBusinessXxteaKey)) {
            String key = Helper.getContext().getString(R.string.network_business_xxtea_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sBusinessXxteaKey = String.valueOf(obj);
        }
        return ("null".equals(sBusinessXxteaKey) || TextUtils.isEmpty(sBusinessXxteaKey)) ? "Official" : sBusinessXxteaKey;
    }

    public static void setBusinessXxteaKey(String xxteaKey) {
        sBusinessXxteaKey = xxteaKey;
    }

    /**
     * 取得des加密key
     *
     * @return xxea加密key
     */
    public static String getDesKey() {
        if (TextUtils.isEmpty(sDesKey)) {
            String key = Helper.getContext().getString(R.string.network_des_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sDesKey = String.valueOf(obj);
        }
        return ("null".equals(sDesKey) || TextUtils.isEmpty(sDesKey)) ? "Official" : sDesKey;
    }

    public static void setDesKey(String desKey) {
        sDesKey = desKey;
    }

    /**
     * 取得Business des加密key
     *
     * @return des加密key
     */
    public static String getBusinessDesKey() {
        if (TextUtils.isEmpty(sBusinessDesKey)) {
            String key = Helper.getContext().getString(R.string.network_business_des_key);
            Object obj = loadMetaData(Helper.getContext(), key);
            sBusinessDesKey = String.valueOf(obj);
        }
        return ("null".equals(sBusinessDesKey) || TextUtils.isEmpty(sBusinessDesKey)) ? "Official" : sBusinessDesKey;
    }

    public static void setBusinessDesKey(String desKey) {
        sBusinessDesKey = desKey;
    }

    /**
     * 读取manifest中相应meta-data标签的值
     *
     * @param key meta-data标签的名称
     * @return meta-data标签的值
     */
    public static Object loadMetaData(Context context, String key) {
        Object result = META_DATA_CACHE.get(key);
        if (result != null) {
            return result;
        }
        synchronized (META_DATA_CACHE) {
            result = META_DATA_CACHE.get(key);
            if (result == null) {
                result = getMeta(context, key);
                if (result != null) {
                    META_DATA_CACHE.put(key, result);
                } else {
                    L.e("ProjectHelper", "************** can not find value for " + key);
                }
            }
        }
        return result;
    }
    // endregion

    private static final Map<String, Object> META_DATA_CACHE = new ConcurrentHashMap<>();

    public static Object getMeta(Context context, String key) {
        Object value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                value = bundle.get(key);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
