package com.graduation.android.readme.http;

import com.google.gson.annotations.SerializedName;
import com.graduation.android.readme.base.IResponse;


/**
 * 返回的参数
 * @param <T>
 */
public class BaseResponse<T> implements IResponse<T> {

    @SerializedName("resultCode")
    public String status;
    @SerializedName("__Cache__")
    public String sign;
    @SerializedName("resultMessage")
    public String message;
    @SerializedName(value = "data", alternate = "content")
    public T data;

    @Override
    public String getCode() {
        return status;
    }

    @Override
    public void setCode(String code) {
        this.status = code;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public void setMsg(String msg) {
        this.message = msg;
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return "0".equals(status);
    }

    @Override
    public String toString() {
        return "resp{" +
                "status='" + status + '\'' +
                ", sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
