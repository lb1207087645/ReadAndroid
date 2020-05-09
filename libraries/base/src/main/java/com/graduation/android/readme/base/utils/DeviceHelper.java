package com.graduation.android.readme.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 设备助手
 * <p>参考：https://github.com/nisrulz/easydeviceinfo</p>
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/12 下午6:07
 */

public class DeviceHelper {


    private static final String TAG = "DeviceHelper";

    /**
     * 获取设备AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        String androidId = "";
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(android_id)) {
                androidId = android_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidId;
    }

    /**
     * 获取设备类型
     *
     * @return 1-phone 2-pad
     */
    public static String getDeviceType() {
        return (Helper.getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE ? "2" : "1";
    }

    /**
     * 是否ROOT
     *
     * @return true if rooted, else return false.
     */
    public static boolean isRoot() {
        String su = "su";
        String[] locations = {
                "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取得mac地址
     */
    public static String getMacAddress() {
        String result = "02:00:00:00:00:00";
        if (Helper.getContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Hardware ID are restricted in Android 6+
                // https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
                Enumeration<NetworkInterface> interfaces = null;
                try {
                    interfaces = NetworkInterface.getNetworkInterfaces();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                while (interfaces != null && interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();

                    byte[] addr = new byte[0];
                    try {
                        addr = networkInterface.getHardwareAddress();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                    if (addr == null || addr.length == 0) {
                        continue;
                    }

                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    String wifiInterfaceName = "wlan0";
                    result = wifiInterfaceName.equals(networkInterface.getName()) ? mac : result;
                }
            } else {
                WifiManager wm = (WifiManager) Helper.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wm.getConnectionInfo();
                if (null != connectionInfo) {
                    result = connectionInfo.getMacAddress();
                }
            }
        }
        return result;
    }

    /**
     * 获取路由器地址
     */
    public static String getBssid() {
        if (Helper.getContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            WifiManager wm = (WifiManager) Helper.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wm == null) {
                return "";
            }
            WifiInfo info = wm.getConnectionInfo();
            if (info == null) {
                return "";
            }
            return TextUtils.isEmpty(info.getBSSID()) ? "" : info.getBSSID();
        }
        return "";
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI() {
        if (Helper.getContext().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) Helper.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return TextUtils.isEmpty(telephonyManager.getDeviceId()) ? "" : telephonyManager.getDeviceId();
        } else {
            return "";
        }
    }

    /**
     * Gets psuedo unique id.
     *
     * @return the psuedo unique id
     */
    public static String getPseudoUniqueID() {
        SharedPreferences sp = Helper.getContext().getSharedPreferences("uid", Context.MODE_PRIVATE);
        String uuid = sp.getString("uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            sp.edit().putString("uuid", uuid).apply();
        }
        return uuid;
    }

    /**
     * Gets psuedo unique id.
     *
     * @return the psuedo unique id
     */
    public static String getUUID() {
        SharedPreferences sp = Helper.getContext().getSharedPreferences("uid", Context.MODE_PRIVATE);
        String uuid = sp.getString("uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            sp.edit().putString("uuid", uuid).apply();
        }
        return uuid;
    }

    /**
     * 获取序列号
     */
    public static String getSerial() {
        // Build.VERSION_CODES.O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Helper.getContext().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return Build.getSerial();
            } else {
                return "";
            }
        }
        return Build.SERIAL;
    }

    // region 屏幕

    private static int[] sScreenSize = null;

    /**
     * 取得屏幕尺寸(0:宽度; 1:高度)
     *
     * @return size of screen
     */
    public static int[] getScreenSize() {
        if (sScreenSize == null) {
            sScreenSize = new int[2];
            DisplayMetrics dm = Helper.getContext().getResources().getDisplayMetrics();
            sScreenSize[0] = dm.widthPixels;
            sScreenSize[1] = dm.heightPixels;
        }
        return sScreenSize;
    }

    /**
     * 取得屏幕宽度
     *
     * @return width of screen
     */
    public static int getScreenWidth() {
        return getScreenSize()[0];
    }

    /**
     * 取得屏幕高度
     *
     * @return height of screen
     */
    public static int getScreenHeight() {
        return getScreenSize()[1];
    }
    // endregion

    // region 网络

    /**
     * 获取网络类型 http://blog.csdn.net/hknock/article/details/37650917
     */
    public static String getNetworkType() {
        int networkClass = getNetworkClass();
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "0";
                break;
            case NETWORK_CLASS_WIFI:
                type = "1";
                break;
            case NETWORK_CLASS_2_G:
                type = "2";
                break;
            case NETWORK_CLASS_3_G:
                type = "3";
                break;
            case NETWORK_CLASS_4_G:
                type = "4";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "100";
                break;
            default:
                break;
        }
        return type;
    }

    private static int getNetworkClass() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) Helper.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnectedOrConnecting()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) Helper.getContext().getSystemService(
                            Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    // 适配低版本手机
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;

    // endregion

}
