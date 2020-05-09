package com.graduation.android.readme.base.utils;

import android.util.Base64;


/**
 * 加密工具
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/8 下午2:29
 */

public class EncryptHelper {

    /**
     * 通用加密方法
     *
     * @param encrypt 加密类型
     * @param key     加密key
     * @param data    原始数据
     * @return 加密结果
     */
    public static String encryptWith(EncryptTypeEnum encrypt, String key, String data) {
        switch (encrypt) {
            default:
            case NONE:
                return data;
            case XXTEA:
                return encryptByXXTEAWithBase64(data, key);
            case BASE:
                return AES256Cipher.AES_Encode(data);
        }
    }

    /**
     * 通用解密方法
     *
     * @param decrypt 解密类型
     * @param key     加密key
     * @param data    加密数据
     * @return 解密结果
     */
    public static String decryptWith(EncryptTypeEnum decrypt, String key, String data) {
        switch (decrypt) {
            default:
            case NONE:
                return data;
            case XXTEA:
                return decryptByXXTEAWithBase64(data, key);
            case BASE:
                return AES256Cipher.AES_Decode(data);
        }
    }

    /**
     * xxtea加密
     *
     * @param data 待加密字串
     * @param key  密钥
     * @return 加密结果
     */
    public static String encryptByXXTEAWithBase64(String data, String key) {
        try {
            return Base64.encodeToString(XXTEA.encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8")), Base64.NO_WRAP);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * xxtea解密
     *
     * @param data 待解密字串
     * @param key  密钥
     * @return 解密结果
     */
    public static String decryptByXXTEAWithBase64(String data, String key) {
        try {
            return new String(XXTEA.decrypt(Base64.decode(data, Base64.NO_WRAP), key.getBytes("UTF-8")), "UTF-8");
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * des加密
     *
     * @param data 待加密字串
     * @param key  密钥
     * @return 加密结果
     */
    public static String encryptByDesWithBase64(String data, String key) {
        try {
            key = key.length() > 8 ? key.substring(0, 8) : key;
            return DES.encryptDESWithBase64(data, key);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * des解密
     *
     * @param data 待解密字串
     * @param key  密钥
     * @return 解密结果
     */
    public static String decryptByDesWithBase64(String data, String key) {
        try {
            key = key.length() > 8 ? key.substring(0, 8) : key;
            return DES.decryptDESWithBase64(data, key);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * md5加密
     *
     * @param content 字符串
     * @return 加密结果
     */
    public static String md5(String content) {
        return MD5.md5(content);
    }

    /**
     * md5加密
     *
     * @param content 字节数组
     * @return 加密结果
     */
    public static String md5(byte[] content) {
        return MD5.md5(content);
    }
}
