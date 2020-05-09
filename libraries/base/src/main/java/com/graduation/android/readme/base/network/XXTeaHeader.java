package com.graduation.android.readme.base.network;

import android.support.annotation.Keep;

import com.graduation.android.readme.base.utils.ProjectHelper;

import java.util.Map;

/**
 * Created by lytjackson on 2018/1/9.
 * email:lytjackson@gmail.com
 */
@Keep
public final class XXTeaHeader extends BaseHeader implements IHeaderInject {

    public static final String XXTEA_HEAD_STR = IDynamicParam.HEAD_KEY + ":com.sinyee.babybus.core.network.header.XXTeaHeader";

    @Override
    public String getProductID() {
        return String.valueOf(ProjectHelper.getProductID());
    }

    @Override
    public String getSecretKey() {
        return ProjectHelper.getSecretKey();
    }

    @Override
    public String getXXteaKey() {
        return ProjectHelper.getXxteaKey();
    }

    @Override
    public void appendOrEditParam(Map<String, String> headerMap) {

    }

    @Override
    public void headerInject(Map<String, String> injectMap) {

    }
}
