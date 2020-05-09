package com.graduation.android.readme.base.utils;

import io.reactivex.annotations.Nullable;

public class Utils {

    /**
     * 非空检查
     *
     * @param object
     * @param message
     * @param <T>
     * @return T  返回值，返回任意对象
     */
    public static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }


}
