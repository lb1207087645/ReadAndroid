package com.graduation.android.readme.base.utils;

/**
 * desc: 加密枚举
 * Created by lyt
 * email:lytjackson@gmail.com
 */
public enum EncryptTypeEnum {

    /**
     * 不加密
     */
    NONE("", 0),
    /**
     * XXTEA加密
     */
    XXTEA("", 1),
    /**
     * aes加密
     */
    BASE("", 2);

    private String key;
    private int type;

    EncryptTypeEnum(String key, int type) {
        this.key = key;
        this.type = type;
    }

    public int type() {
        return type;
    }

    public String key() {
        return key;
    }

    public EncryptTypeEnum key(String key) {
        this.key = key;
        return this;
    }
}
