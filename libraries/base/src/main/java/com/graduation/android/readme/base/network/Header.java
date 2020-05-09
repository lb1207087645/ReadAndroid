package com.graduation.android.readme.base.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.graduation.android.readme.base.utils.DeviceHelper;
import com.graduation.android.readme.base.utils.EncryptHelper;
import com.graduation.android.readme.base.utils.EncryptTypeEnum;
import com.graduation.android.readme.base.utils.Helper;
import com.graduation.android.readme.base.utils.ProjectHelper;

import java.io.EOFException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * 头部信息工具
 *
 * @author pcqpcq
 * @version 1.0.0
 * @since 2017/6/6 上午10:38
 */

public class Header {

    private static final String TAG = "Header";
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    static String sSessionID = "";
    static String sClientID = "";
    //head版本
    public static final String HEAD_VERSION_ID = "HeadVerID";
    public static final String HEAD_VERSION = "1";

//    public static String generateHeaderString() {
//        return mapToString(generateHeaderMap(), true);
//    }
//
//    public static String generateHeaderMD5(EncryptTypeEnum encrypt, String key) {
//        Map<String, String> map = generateHeaderMap();
//        return generateHeaderMD5(encrypt, key, map);
//    }

    public static String generateHeaderMD5(EncryptTypeEnum encrypt, String key, Map<String, String> headerMap) {
        return EncryptHelper.md5(EncryptHelper.encryptWith(encrypt, key, mapToString(headerMap, encrypt.type() > 0)));
    }

    /**
     * 取当前App在此设备上的ID，目前作为此App的身份用于服务端允许取消加密
     */
    public static String getClientID() {
        if (TextUtils.isEmpty(sClientID)) {
            SharedPreferences sp = Helper.getContext().getSharedPreferences(Helper.getContext().getPackageName() + "_client", Context.MODE_PRIVATE);
            String clientID = sp.getString("client_id", "");
            if (TextUtils.isEmpty(clientID)) {
                clientID = UUID.randomUUID().toString().replaceAll("-", "");
                sp.edit().putString("client_id", clientID).apply();
            }
            sClientID = clientID;
        }
        return sClientID;
    }

    /**
     * 取当前会话ID
     */
    public static String getSessionID() {
        if (TextUtils.isEmpty(sSessionID)) {
            sSessionID = UUID.randomUUID().toString().replaceAll("-", "");
        }
        return sSessionID;
    }

    /**
     * 清除当前会话ID
     */
    public static void clearSessionID() {
        sSessionID = null;
    }

    public static String getSignatureMD5(Map<String, String> paramsMap, String key) {
        //1->参数Map转字符串
        String lowcaseParams = mapToString(paramsMap, false).toLowerCase();
        //2->得到md5值
        return EncryptHelper.md5(lowcaseParams + key);
    }

    public static Map<String, String> generateHeaderMap() {
        Map<String, String> map = new HashMap<>(64);
        // 设备信息
        map.put("OSType", "2");
        map.put("DeviceType", DeviceHelper.getDeviceType());
        map.put("PlatForm", "11");
        map.put("OSVer", Build.VERSION.RELEASE);
        map.put("DeviceModel", Build.MANUFACTURER.concat(" ").concat(Build.MODEL));
        map.put("DeviceLang", Locale.getDefault().getLanguage());
        map.put("AppLang", Locale.getDefault().getLanguage());
        map.put("Net", DeviceHelper.getNetworkType());
        map.put("Mac", DeviceHelper.getMacAddress());
        map.put("Screen", String.format("%s*%s", DeviceHelper.getScreenWidth(), DeviceHelper.getScreenHeight()));

        map.put("Serial", DeviceHelper.getSerial());
        map.put("AndroidId", DeviceHelper.getAndroidID(Helper.getContext()));

        map.put("OpenID", DeviceHelper.getPseudoUniqueID());
        map.put("IMEI", DeviceHelper.getIMEI());

//        map.put("ClientID", getClientID());
        // iOS属性
        map.put("IDFA", "");
        map.put("IDFV", "");
        map.put("RTime", "0");
        map.put("Token", Helper.get().getPushToken());
        map.put("TokenType", String.valueOf(ProjectHelper.getTokenType()));
        map.put("SimIDFA", "");
        // 应用信息
        map.put("ProjectID", String.valueOf(ProjectHelper.getProjectID()));
        map.put("CHID", String.valueOf(ProjectHelper.getChID()));
        map.put("CHCode", ProjectHelper.getChCode());
        map.put("VerID", String.valueOf(ProjectHelper.getCurrentVersion()));
        map.put("VerCode", ProjectHelper.getCurrentVersionName());
        map.put("SdkVerID", "0");
        map.put("SdkVer", "");
        // 用户信息
        Long accountID = UserBase.getAccountID();
        Long loginStamp = UserBase.getLoginStamp();
        map.put("AccountID", accountID > -1 ? String.valueOf(accountID) : "");
        map.put("AccountIDSignature", UserBase.getAccountIDSignature());
        map.put("LoginStamp", loginStamp > -1 ? String.valueOf(loginStamp) : "");
        map.put("LoginSignature", UserBase.getLoginSignature());
        map.put("LoginCode", UserBase.getLoginCode());
        map.put("SessionID", getSessionID());
        map.put(HEAD_VERSION_ID, HEAD_VERSION); // 当前生命周期内唯一
        map.put("IsDebug", String.valueOf(Helper.get().getIsDebug())); // 调试模式 1调试 0否
        return map;
    }

    public static String mapToString(Map<String, String> map, boolean urlEncoded) {
        if (map == null || map.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String value = map.get(key);

            sb.append(key);
            sb.append("=");
            if (!TextUtils.isEmpty(value)) {
                sb.append(urlEncoded ? URLEncoder.encode(value) : value);
            } else {
                sb.append(value);
            }
            sb.append("&");
        }
        String s = sb.deleteCharAt(sb.length() - 1).toString();
        return s;
    }

    /**
     * 获取动态头部处理器
     *
     * @param originalRequest
     * @return
     */
    @Nullable
    public static IDynamicParam getDynamicParam(Request originalRequest) {
        String headerClazz = originalRequest.header(IDynamicParam.HEAD_KEY);
        IDynamicParam dh = null;
        try {
            if (!TextUtils.isEmpty(headerClazz)) {
                Class<IDynamicParam> clazz = (Class<IDynamicParam>) Class.forName(headerClazz);
                dh = clazz.newInstance();
            }
        } catch (Exception e) {
            //异常情况，未配置加密情况，给默认值xxtea加密方式
            dh = new XXTeaHeader();
        }
        return dh;
    }

    /**
     * 根据是否传入秘钥与是否md5，读取请求体内容
     * 1-直接读取
     * 2-反gzip，解密
     * 3-反gzip，解密, md5
     *
     * @param request
     * @return
     */
    public static String getRequestBodyStr(Request request) {
        return getRequestBodyStr(request, null, true);
    }

    /**
     * 根据是否传入秘钥与是否md5，读取请求体内容
     * 1-直接读取
     * 2-反gzip，解密
     * 3-反gzip，解密, md5
     *
     * @param request
     * @param md5     是否md5，true则对body体进行md5后输出
     * @return
     */
    public static String getRequestBodyStr(Request request, boolean md5) {
        return getRequestBodyStr(request, null, md5);
    }

    /**
     * 根据是否传入秘钥与是否md5，读取请求体内容
     * 1-直接读取
     * 2-反gzip，解密
     * 3-反gzip，解密, md5
     *
     * @param request
     * @param key     解密秘钥，有传入，则进行xxtea解密
     * @param md5     是否md5，true则对body体进行md5后输出
     * @return
     */
    public static String getRequestBodyStr(Request request, String key, boolean md5) {
        byte[] content;
        String result = "";
        if (request == null) {
            return result;
        }
        try {
            // 无参
            RequestBody body = request.body();
            if (body == null || body.contentLength() == 0) {
                return result;
            }
            // 有参
            Buffer originBuffer = new Buffer();
            body.writeTo(originBuffer);
//            Charset charset = UTF_8;
//            MediaType contentType = body.contentType();
//            if (contentType != null) {
//                charset = contentType.charset(UTF_8);
//            }
            // 目前仅文件是不作gzip
            if (body instanceof MultipartBody) {
                result = new String(originBuffer.readByteArray());
            } else {
                if (isPlaintext(originBuffer)) {
                    content = originBuffer.readByteArray();
                } else {
                    BufferedSource gzipBuffer = Okio.buffer(new GzipSource(originBuffer));
                    content = gzipBuffer.readByteArray();
                    gzipBuffer.close();
                }
                if (!TextUtils.isEmpty(key)) {
                    result = EncryptHelper.decryptWith(Helper.get().getEncrypt(), key, new String(content));
                } else {
                    result = new String(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        L.e("Header", "Header getRequestBodyStr===" + result);
        if (md5) {
            return EncryptHelper.md5(result);
        } else {
            return result;
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
