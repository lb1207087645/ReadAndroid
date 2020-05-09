package com.graduation.android.readme.base.network;

import android.text.TextUtils;


import com.graduation.android.readme.base.utils.EncryptHelper;
import com.graduation.android.readme.base.utils.Helper;
import com.graduation.android.readme.base.utils.L;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by lytjackson on 2018/1/9.
 * email:lytjackson@gmail.com
 */

public abstract class BaseHeader implements IDynamicParam {

    @Override
    public String getProductID() {
        return "";
    }

    @Override
    public String getSecretKey() {
        return "";
    }

    @Override
    public String getXXteaKey() {
        return "";
    }

    /**
     * 获取xxtea加密秘钥
     *
     * @return
     */
    public abstract void appendOrEditParam(Map<String, String> headerMap);

    @Override
    public Request getHeaderStr(Request request) throws IOException {
        // 添加头部
        Map<String, String> headerMap = Header.generateHeaderMap();
        appendOrEditParam(headerMap);
        // 产品信息
        headerMap.put("ProductID", getProductID());
        String headData = Header.mapToString(headerMap, true);
        L.e("request-header", "header = " + headData);
        String header = EncryptHelper.encryptWith(Helper.get().getEncrypt(), getXXteaKey(), headData);
        Request headerRequest;
        Request.Builder builder = request.newBuilder();
        if (!TextUtils.isEmpty(header)) {
            builder.header("ClientHeaderInfo", header);
        }
        headerRequest = builder.build();
        return appendedParamsRequest(header, headerRequest);
    }

    protected Request appendedParamsRequest(String header, Request headerRequest) throws IOException {
        // 生成附加参数
        Request appendedParamsRequest;
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        String signatureStamp = String.valueOf(System.currentTimeMillis() / 1000);

        String headerMD5 = EncryptHelper.md5(header);

        String productID = getProductID();
        String contentMD5 = Header.getRequestBodyStr(headerRequest);
        // 获取url里的参数
        TreeMap<String, String> paramsMap = getUrlParamsFromRequest(headerRequest);
        // 获取MultipartBody里的参数
        paramsMap.putAll(getMultipartBodyParamsFromRequest(headerRequest));
        // 附加上参数
        paramsMap.put("AccessToken", accessToken);
        paramsMap.put("HeaderMD5", headerMD5);
        paramsMap.put("ProductID", productID);
        paramsMap.put("EncryptType", String.valueOf(Helper.get().getEncrypt().type()));
        paramsMap.put("ContentMD5", contentMD5);
        paramsMap.put("SignatureStamp", signatureStamp);
        paramsMap.put("SignatureMD5", Header.getSignatureMD5(paramsMap, getSecretKey()));
        // 生成新的添加过参数的请求
        Set<String> appendedParamKeys = paramsMap.keySet();
        HttpUrl.Builder urlBuilder = headerRequest.url().newBuilder();
        for (String key : appendedParamKeys) {
            urlBuilder.removeAllQueryParameters(key);
            urlBuilder.addQueryParameter(key, paramsMap.get(key));
        }
        appendedParamsRequest = headerRequest.newBuilder()
                .url(urlBuilder.build())
                .build();
        return appendedParamsRequest;
    }

    private TreeMap<String, String> getMultipartBodyParamsFromRequest(Request request) throws IOException {
        TreeMap<String, String> paramsMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 升序排序
                        return obj1.toLowerCase().compareTo(obj2.toLowerCase());
                    }
                });

        RequestBody body = request.body();
        if (!(body instanceof MultipartBody)) {
            return paramsMap;
        }

        MultipartBody bodyParams = (MultipartBody) body;
        List<MultipartBody.Part> parts = bodyParams.parts();
        for (MultipartBody.Part p : parts) {
            try {
                String contentDisposition = p.headers().get("Content-Disposition");
                boolean isFile = contentDisposition.contains("filename");
                if (!isFile) {
                    int i = contentDisposition.indexOf("name=\"") + 6;
                    String name = contentDisposition.substring(i, contentDisposition.indexOf("\"", i));
                    Buffer buffer = new Buffer();
                    p.body().writeTo(buffer);
                    paramsMap.put(name, buffer.readUtf8());
                }
            } catch (NullPointerException e) {
            }
        }
        return paramsMap;
    }

    private TreeMap<String, String> getUrlParamsFromRequest(Request request) {
        HttpUrl url = request.url();
        Set<String> originalParamKeys = url.queryParameterNames();
        TreeMap<String, String> paramsMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 升序排序
                        return obj1.toLowerCase().compareTo(obj2.toLowerCase());
                    }
                });
        for (String key : originalParamKeys) {
            paramsMap.put(key, url.queryParameter(key));
        }
        return paramsMap;
    }

}
