package com.graduation.android.readme.base.network;

import java.util.Map;

/**
 * author：zhengchaoyi on $date$
 * desc：头部参数合并，将组建特有的头部信息插入主工程
 */
public interface IHeaderInject {
    void headerInject(Map<String, String> injectMap);
}
