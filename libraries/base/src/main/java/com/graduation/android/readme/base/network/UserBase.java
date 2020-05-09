package com.graduation.android.readme.base.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.graduation.android.readme.base.utils.Helper;


/**
 * 用户系统基础信息存取
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/12 下午4:58
 */

public class UserBase {

    private static final String ACCOUNTID_KEY = "AccountID";
    private static final String ACCOUNTID_SIGNATURE_KEY = "AccountIDSignature";
    private static final String LOGIN_STAMP_KEY = "LoginStamp";
    private static final String LOGIN_SIGNATURE_KEY = "LoginSignature";
    private static final String LOGIN_CODE = "LoginCode";
    private static final String LOGIN_TOKEN = "Authorization";

    private static final String USER_INFO_PREFERENCE_NAME = "nineteen_user_info_prefs";

    private static long sAccountID = -1;
    private static String sAccountIDSignature;
    private static String sLoginSignature;
    private static long sLoginStamp = -1;
    private static String sLoginCode;
    private static String sLoginToken;

    public static void saveAccountID(long accountID) {
        getUserSharedPrefs().edit().putLong(ACCOUNTID_KEY, accountID).apply();
        sAccountID = -1;
    }

    public static void saveAccountIDSignature(String accountIDSignature) {
        getUserSharedPrefs().edit().putString(ACCOUNTID_SIGNATURE_KEY, accountIDSignature).apply();
        sAccountIDSignature = null;
    }

    public static void saveLoginStamp(long loginStamp) {
        getUserSharedPrefs().edit().putLong(LOGIN_STAMP_KEY, loginStamp).apply();
        sLoginStamp = -1;
    }

    public static void saveLoginSignature(String loginSignature) {
        getUserSharedPrefs().edit().putString(LOGIN_SIGNATURE_KEY, loginSignature).apply();
        sLoginSignature = null;
    }

    public static void saveLoginCode(String loginCode) {
        getUserSharedPrefs().edit().putString(LOGIN_CODE, loginCode).apply();
        sLoginCode = null;
    }

    public static void saveLoginToken(String token) {
        getUserSharedPrefs().edit().putString(LOGIN_TOKEN, token).apply();
        sLoginToken = null;
    }

    public static Long getAccountID() {
        if (sAccountID == -1) {
            sAccountID = getUserSharedPrefs().getLong(ACCOUNTID_KEY, -1);
        }
        return sAccountID;
    }

    public static String getAccountIDSignature() {
        if (TextUtils.isEmpty(sAccountIDSignature)) {
            sAccountIDSignature = getUserSharedPrefs().getString(ACCOUNTID_SIGNATURE_KEY, "");
        }
        return sAccountIDSignature;
    }

    public static Long getLoginStamp() {
        if (sLoginStamp == -1) {
            sLoginStamp = getUserSharedPrefs().getLong(LOGIN_STAMP_KEY, -1);
        }
        return sLoginStamp;
    }

    public static String getLoginSignature() {
        if (TextUtils.isEmpty(sLoginSignature)) {
            sLoginSignature = getUserSharedPrefs().getString(LOGIN_SIGNATURE_KEY, "");
        }
        return sLoginSignature;
    }

    public static String getLoginCode() {
        if (TextUtils.isEmpty(sLoginCode)) {
            sLoginCode = getUserSharedPrefs().getString(LOGIN_CODE, "");
        }
        return sLoginCode;
    }

    public static String getLoginToken() {
        if (TextUtils.isEmpty(sLoginToken)) {
            sLoginToken = getUserSharedPrefs().getString(LOGIN_TOKEN, "");
        }
        if (!TextUtils.isEmpty(sLoginToken)) {
            return "bearer " + sLoginToken;
        }
        return "";
    }

    public static void clean() {
        // 清空缓存
        getUserSharedPrefs().edit().clear().apply();
        // 清空内存
        sAccountID = -1;
        sAccountIDSignature = null;
        sLoginSignature = null;
        sLoginStamp = -1;
        sLoginCode = null;
    }

    // region 内部方法
    private static SharedPreferences sp;

    public static SharedPreferences getUserSharedPrefs() {
        if (sp == null) {
            sp = Helper.getContext().getSharedPreferences(USER_INFO_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }
    // endregion
}
