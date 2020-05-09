package com.graduation.android.readme.model;

import android.util.Log;

public class Translation3 {


    public int status;

    private content content;

    public static class content {
        public String from;
        public String to;
        public String vendor;
        public String out;
        public int errNo;

    }

    //定义 输出返回数据 的方法
    public void show() {
        Log.d("RxJava", content.out);
    }


}
