package com.graduation.android.readme.base.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {

    public static String encryptDESWithBase64(String encryptString, String encryptKey) {
        String result = encryptString;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            int len = encryptKey.length();
            if (len < DESKeySpec.DES_KEY_LEN) {
                for (int i = len; i < DESKeySpec.DES_KEY_LEN; i++) {
                    encryptKey += "\0";
                }
            }
            DESKeySpec desKeySpec = new DESKeySpec(encryptKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(encryptKey.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            result = Base64.encodeToString(cipher.doFinal(encryptString.getBytes("UTF-8")), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decryptDESWithBase64(String decryptString, String decryptKey) {
        String result = decryptString;
        try {
            byte[] bytesrc = Base64.decode(decryptString, Base64.NO_WRAP);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            int len = decryptKey.length();
            if (len < DESKeySpec.DES_KEY_LEN) {
                for (int i = len; i < DESKeySpec.DES_KEY_LEN; i++) {
                    decryptKey += "\0";
                }
            }
            DESKeySpec desKeySpec = new DESKeySpec(decryptKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(decryptKey.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            result = new String(retByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
