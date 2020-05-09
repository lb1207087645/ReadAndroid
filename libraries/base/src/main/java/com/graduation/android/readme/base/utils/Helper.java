package com.graduation.android.readme.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 基础助手
 * <p>
 * 关于正确持有context实例的做法，可以看看这两个地方对于 <a href="https://developer.android.com/training/volley/requestqueue.html#singleton">官方做法</a> 的讨论，
 * 尽管并没有消除这个警告，但是可以避免不正确的持有context造成的内存泄漏。所以我觉得官方的示例是一个最佳实践：
 * <li><a href="https://stackoverflow.com/questions/37709918/warning-do-not-place-android-context-classes-in-static-fields-this-is-a-memory/40235834#40235834">warning-do-not-place-android-context-classes-in-static-fields-this-is-a-memory</a></li>
 * <li><a href="https://stackoverflow.com/questions/39840818/android-googles-contradiction-on-singleton-pattern/39841446#39841446">android-googles-contradiction-on-singleton-pattern</a></li>
 * </p>
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/10 下午1:38
 */

public class Helper {

    private static Helper sHelper;
    private static Context sContext;
    private EncryptTypeEnum encrypt = EncryptTypeEnum.XXTEA;

    private Helper(Context context) {
        sContext = context;
    }

    /**
     * 基础助手类初始化
     *
     * @param context 上下文
     */
    public static synchronized Helper init(Context context) {
        if (sHelper == null) {
            sHelper = new Helper(context.getApplicationContext());
        }
        return sHelper;
    }

    public static Helper get() {
        if (sHelper == null) {
            throw new IllegalStateException("请先初始化Helper： Helper.init(context)");
        }
        return sHelper;
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalStateException("请先初始化： Helper.init(context)");
        }
        return sContext;
    }

    public Helper withEncrypt(EncryptTypeEnum encrypt) {
        this.encrypt = encrypt;
        return this;
    }

    public EncryptTypeEnum getEncrypt() {
        return this.encrypt;
    }

    private String userHost = "https://udb.babybus.com/";

    public Helper withUserHost(String host) {
        this.userHost = host;
        return this;
    }

    public String getUserHost() {
        return userHost;
    }

    private String pushToken;

    public Helper withPushToken(String pushToken) {
        this.pushToken = pushToken;
        return this;
    }

    public String getPushToken() {
        return TextUtils.isEmpty(pushToken) ? "" : pushToken;
    }

    /**
     * 调试状态
     * @param isDebug
     * @return
     */
    public void setIsDebug(int isDebug) {
        SharedPreferences sp = Helper.getContext().getSharedPreferences("uid", Context.MODE_PRIVATE);
        sp.edit().putInt("isDebug", isDebug).apply();
    }

    public int getIsDebug() {
        SharedPreferences sp = Helper.getContext().getSharedPreferences("uid", Context.MODE_PRIVATE);
        int debug = sp.getInt("isDebug", 0);
        return debug;
    }
}
