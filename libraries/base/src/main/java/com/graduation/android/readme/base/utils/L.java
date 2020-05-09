package com.graduation.android.readme.base.utils;

import android.content.Context;
import android.util.Log;

/**
 * desc: 日志工具类.
 * Created by lyt
 * email:lytjackson@gmail.com
 */
public class L {

    /**
     * debug开关.
     */
    public static boolean D = true;

    /**
     * info开关.
     */
    public static boolean I = true;

    /**
     * info开关.
     */
    public static boolean W = true;

    /**
     * error开关.
     */
    public static boolean E = true;

    /**
     * debug日志
     *
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (D) {
            Log.d(tag, message);
        }
    }

    /**
     * debug日志
     *
     * @param context
     * @param message
     */
    public static void d(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        d(tag, message);
    }

    /**
     * debug日志
     *
     * @param clazz
     * @param message
     */
    public static void d(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        d(tag, message);
    }

    /**
     * debug日志
     *
     * @param message
     */
    public static void d(String message) {
        d(getClassName(), message);
    }

    /**
     * info日志
     *
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (I) {
            Log.i(tag, message);
        }
    }

    /**
     * info日志
     *
     * @param context
     * @param message
     */
    public static void i(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        i(tag, message);
    }

    /**
     * info日志
     *
     * @param clazz
     * @param message
     */
    public static void i(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        i(tag, message);
    }

    /**
     * info日志
     *
     * @param message
     */
    public static void i(String message) {
        i(getClassName(), message);
    }

    /**
     * warn日志
     *
     * @param tag
     * @param message
     */
    public static void w(String tag, String message) {
        if (W) {
            Log.w(tag, message);
        }
    }

    /**
     * warn日志
     *
     * @param context
     * @param message
     */
    public static void w(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        w(tag, message);
    }

    /**
     * warn日志
     *
     * @param clazz
     * @param message
     */
    public static void w(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        w(tag, message);
    }

    /**
     * warn日志
     *
     * @param message
     */
    public static void w(String message) {
        w(getClassName(), message);
    }

    /**
     * error日志
     *
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        if (E) {
            Log.e(tag, message);
        }
    }

    /**
     * error日志
     *
     * @param message
     */
    public static void e(String message) {
        e(getClassName(), message);
    }

    /**
     * error日志
     *
     * @param context
     * @param message
     */
    public static void e(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        e(tag, message);
    }

    /**
     * error日志
     *
     * @param clazz
     * @param message
     */
    public static void e(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        e(tag, message);
    }

    private static String getClassName() {
        // 这里的数组的index，即2，是根据你工具类的层级取的值，可根据需求改变
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }

    /**
     * debug日志的开关
     *
     * @param d
     */
    public static void debug(boolean d) {
        D = d;
    }

    /**
     * info日志的开关
     *
     * @param i
     */
    public static void info(boolean i) {
        I = i;
    }

    /**
     * info日志的开关
     *
     * @param w
     */
    public static void warn(boolean w) {
        W = w;
    }

    /**
     * error日志的开关
     *
     * @param e
     */
    public static void error(boolean e) {
        E = e;
    }

    /**
     * 设置日志的开关
     *
     * @param e
     */
    public static void setDebugState(boolean d, boolean i, boolean w, boolean e) {
        D = d;
        I = i;
        W = w;
        E = e;
    }

    /**
     * 打开所有日志，默认全打开
     */
    public static void openAll() {
        D = true;
        I = true;
        W = true;
        E = true;
    }

    /**
     * 关闭所有日志
     */
    public static void closeAll() {
        D = false;
        I = false;
        W = false;
        E = false;
    }

}
