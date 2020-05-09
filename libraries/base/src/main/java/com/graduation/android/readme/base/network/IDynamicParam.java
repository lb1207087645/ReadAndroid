package com.graduation.android.readme.base.network;

import java.io.IOException;

import okhttp3.Request;

/**
 * Created by lytjackson on 2018/1/9.
 * email:lytjackson@gmail.com
 */
public interface IDynamicParam {

    String HEAD_KEY = "dynamic-header";

    /**
     * 获取请求头
     *
     * @param request
     * @return
     * @throws IOException
     */
    Request getHeaderStr(Request request) throws IOException;

    /**
     * 获取产品io
     *
     * @return
     */
    String getProductID();

    /**
     * 获取md5秘钥
     *
     * @return
     */
    String getSecretKey();

    /**
     * 获取xxtea加密秘钥
     *
     * @return
     */
    String getXXteaKey();
}
