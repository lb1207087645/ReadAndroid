package com.graduation.android.share.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


/**
 * 参考光速达管家
 * Created by Zhengbinbin on 2016/8/5.
 */
public class ShareSdkUtils {
    private Context mContext;

    /**
     * onCreate 调用方法
     *
     * @param context
     */
    public ShareSdkUtils(Context context) {
        mContext = context;
//        ShareSDK.initSDK(mContext);
    }

    /**
     * onDestroy 调用此方法
     */
    public void stopShareSdk() {
//        ShareSDK.stopSDK(mContext);
    }

//    /**
//     * 分享到微信好友 图片
//     */
//    public void shareWeChat_ImagePath(@NonNull String mBmpPath, @NonNull String title, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform("Wechat");
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_IMAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setImagePath(mBmpPath);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        // 执行图文分享
//        platform.share(sp);
//
//    }
//
//    /**
//     * 分享到短信图片
//     */
//    public void shareMessage_ImagePath(@NonNull String mBmpPath, @NonNull String text, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform("ShortMessage");
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_IMAGE);// 一定要设置分享属性
//        sp.setTitle(text);
//        sp.setImagePath(mBmpPath);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        // 执行图文分享
//        platform.share(sp);
//    }
//

    /**
     * QQ分享图片
     *
     * @param mBmpPath
     * @param listener
     */
    public void shareQQ_ImagePath(@NonNull String mBmpPath, PlatformActionListener listener) {
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);// 一定要设置分享属性 不能设置标题
        sp.setImagePath(mBmpPath);
        platform.setPlatformActionListener(listener); // 设置分享事件回调
        // 执行图文分享
        platform.share(sp);
    }

    //
//
//    /**
//     * 分享到微信好友 文本
//     */
//    public void shareWeChat_Test(String text, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform("Wechat");
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_TEXT);// 一定要设置分享属性
//        sp.setText(text);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        // 执行图文分享
//        platform.share(sp);
//    }
//
//    /**
//     * 分享到短信好友
//     */
//    public void shareMessage_Test(String text, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform("ShortMessage");
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_TEXT);// 一定要设置分享属性
//        sp.setText(text);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        // 执行图文分享
//        platform.share(sp);
//    }
//
//    /**
//     * @param text
//     * @param listener
//     */
//    public void shareQQ_Test(String text, PlatformActionListener listener) {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
//        sendIntent.setType("text/plain");
//        try {
//            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
//            Intent chooserIntent = Intent.createChooser(sendIntent, "选择分享途径");
//            if (chooserIntent == null) {
//                return;
//            }
//            mContext.startActivity(chooserIntent);
//        } catch (Exception e) {
//            mContext.startActivity(sendIntent);
//        }
//    }
//
//    /**
//     * 分享微信 （本地图标）
//     */
//    public void shareWechat_WebPage(String url, String title, String content, Bitmap imagePath, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setText(content);
//        sp.setImageData(imagePath);
//        sp.setUrl(dealWithUrl(url));
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 分享微信 （本地图标）
//     */
//    public void shareWechat_WebPage(String url, String title, String content, String imagePath, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setText(content);
//        sp.setImagePath(imagePath);
//        sp.setUrl(dealWithUrl(url));
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 分享微信 （图标Url）
//     */
//    public void shareWechat_WebPage2(String url, String title, String content, String imageUrl, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setText(content);
//        sp.setImageUrl(imageUrl);
//        sp.setUrl(dealWithUrl(url));
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 分享微信（本地图标路径地址）
//     * @param url
//     * @param title
//     * @param content
//     * @param bmp
//     * @param listener
//     */
    public void shareQQ_WebPage(String url, String title, String content, String bmp, PlatformActionListener listener) {
        Platform platform = ShareSDK.getPlatform(QQ.NAME);//?
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(content);
//        sp.setImagePath(bmp);
        platform.setPlatformActionListener(listener); // 设置分享事件回调
        platform.share(sp);
    }
//
//    public void shareQQ_WebPage(String url, String title, String content, Bitmap bmp, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(QQ.NAME);//?
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setTitleUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageData(bmp);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    public void shareQQ_WebPage2(String url, String title, String content, String imageUrl, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(QQ.NAME);//?
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setTitleUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageUrl(imageUrl);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    public void shareQZone_WebPage(String url, String title, String content, String bmp, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(QZone.NAME);//OK
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setTitleUrl(url);
//        sp.setSite("光");
//        sp.setSiteUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImagePath(bmp);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    public void shareQZone_WebPage(String url, String title, String content, Bitmap bmp, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(QZone.NAME);//OK
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setTitleUrl(dealWithUrl(url));
//        sp.setSite("光");
//        sp.setSiteUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageData(bmp);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    public void shareQZone_WebPage2(String url, String title, String content, String imageUrl, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(QZone.NAME);//OK
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setTitleUrl(dealWithUrl(url));
//        sp.setSite("光");
//        sp.setSiteUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageUrl(imageUrl);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 带图标和连接
//     */
//    public void shareWechatFriends_WebPage(String url, String title, String content, String imagePath, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImagePath(imagePath);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 带图标和连接
//     */
//    public void shareWechatFriends_WebPage(String url, String title, String content, Bitmap bitmap, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageData(bitmap);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 带图标和连接
//     */
//    public void shareWechatFriends_WebPage2(String url, String title, String content, String imageUrl, PlatformActionListener listener) {
//        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(title);
//        sp.setUrl(dealWithUrl(url));
//        sp.setText(content);
//        sp.setImageUrl(imageUrl);
//        platform.setPlatformActionListener(listener); // 设置分享事件回调
//        platform.share(sp);
//    }
//
//    /**
//     * 分享链接处理 Lppstore应用 需带参数标识
//     *
//     * @param url
//     * @return
//     */
//    private String dealWithUrl(String url) {
//        String packeName = LibCommonUtils.getPackageName(mContext);
//        if (packeName.equals("cn.gtscn.lppstore")) {
//            if (url.contains("?")) {
//                url = url + "&t=6";
//            } else {
//                url = url + "?t=6";
//            }
//        }
//        return url;
//    }

//    public void showShare(Context context) {
//        OnekeyShare oks = new OnekeyShare();
//        // title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle("分享");
//        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
//        // url在微信、Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
//        // 启动分享GUI
//        oks.show(context);
//    }


}
