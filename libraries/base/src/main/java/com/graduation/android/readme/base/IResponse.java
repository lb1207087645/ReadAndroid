package com.graduation.android.readme.base;

public interface IResponse<T> {

    /**
     * 请求返回码
     *
     * @return
     */
    String getCode();

    /**
     * 设置请求返回码
     *
     * @param code
     */
    void setCode(String code);

    /**
     * 请求结果
     *
     * @return
     */
    String getMsg();

    /**
     * 设置请求结果
     *
     * @param msg
     */
    void setMsg(String msg);

    /**
     * 请求sign
     *
     * @return
     */
    String getSign();

    /**
     * 设置请求sign
     *
     * @param sign
     */
    void setSign(String sign);

    /**
     * 请求数据体
     *
     * @return
     */
    T getData();

    /**
     * 设置请求数据体
     *
     * @param data
     */
    void setData(T data);

    /**
     * 本次请求是否成功
     *
     * @return
     */
    boolean isSuccess();
}

