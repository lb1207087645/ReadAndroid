package com.graduation.android.readme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.base.utils.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件输出路径
 * Created by John on 2016/4/7.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    public static final String VIDEO = "Video";
    public static final String CAPTURE_PICTURE = "Capture_Picture";
    private static final String FILE_DIRECTORY = "/lszy/";
    public static String PACKAGE_NAME = "";

    public static File getExternalStorageDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    public static String getDirectory(Context context, String fileName, String packageName) {
        File directory = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            directory = getExternalStorageDirectory();
        }
        if (directory != null) {
            File file = new File(directory + FILE_DIRECTORY + packageName, fileName);
            if (file.exists() || file.mkdirs()) {
                return file.getPath();
            }
        }
        return null;
    }

    public static String getDirectory(Context context, String fileName) {
        File directory = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            directory = getExternalStorageDirectory();
        }
        if (directory != null) {
            File file = new File(directory + FILE_DIRECTORY + PACKAGE_NAME, fileName);
            if (file.exists() || file.mkdirs()) {
                return file.getPath();
            }
        }
        return context.getFilesDir().getPath();
    }

    public static String getDirectory(Context context, String fileName, boolean mkdirs) {
        File directory = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            directory = getExternalStorageDirectory();
        }
        if (directory != null) {
            File file = new File(directory + FILE_DIRECTORY + PACKAGE_NAME, fileName);
            if (mkdirs) {
                if (file.exists() || file.mkdirs()) {
                    return file.getPath();
                }
            } else {
                return file.getPath();
            }
        }
        return null;
    }

    public static String saveBitmap(Context context, Bitmap bitmap, String packageName) {
        return saveBitmap(context, bitmap, Bitmap.CompressFormat.JPEG, ".jpg", packageName);
    }

    public static String saveBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat format, String fileExtension, String packageName) {
        String directory = getDirectory(context, CAPTURE_PICTURE, packageName);
        if (directory != null) {
            String filePath = directory + "/gts_" + System.currentTimeMillis() + fileExtension;
            try {
                bitmap.compress(format, 100,
                        new FileOutputStream(filePath));
            } catch (FileNotFoundException e) {
                L.d(TAG, e.toString());
                return null;
            }
            return filePath;
        }
        return null;
    }

    public static String saveBitmap(Context context, String filePath, Bitmap bitmap, Bitmap.CompressFormat format, String packageName) {
        String directory = getDirectory(context, CAPTURE_PICTURE, packageName);
        if (directory != null) {
            try {
                bitmap.compress(format, 100, new FileOutputStream(filePath));
            } catch (FileNotFoundException e) {
                L.d(TAG, e.toString());
                return null;
            }
            return filePath;
        }
        return null;
    }

    /**
     * 保存图片到SD卡
     *
     * @param bm         图片bitmap对象
     * @param floderPath 下载文件保存目录
     * @param fileName   文件名称(不带后缀)
     */
    public static void saveImg(Bitmap bm, String floderPath, String fileName) throws IOException {
        //如果不保存在sd下面下面这几行可以不加
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            L.e("ProfileDetailsActivity", "SD卡异常");
            return;
        }

        File folder = new File(floderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String savePath = folder.getPath() + File.separator + fileName + ".jpg";
        File file = new File(savePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        L.d("ProfileDetailsActivity", savePath + " 保存成功");
        bos.flush();
        bos.close();
    }

    public static void scannerFile(Context ctx, String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                String[] paths = new String[]{};
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        paths = new String[files.length];
                        for (int i = 0; i < files.length; i++) {
                            paths[i] = files[i].getPath();
                        }
                    }
                } else {
                    paths = new String[]{filePath};
                }
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                ctx.sendBroadcast(intent);
            }
        }
    }

    public static boolean isExists(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }

    public static File getCacheDirectory(Context context, String packageName) {
        String image_cache = getDirectory(context, "image_cache", packageName);
        if (!TextUtils.isEmpty(image_cache)) {
            return new File(image_cache);
        }
        return null;
    }

    public static void deleteFiles(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            File[] files = parentFile.listFiles();
            if (files == null) {
                return;
            }
            for (File subFile : files) {
                if (subFile.getName().equals(file.getName())) {
                    continue;
                } else {
                    subFile.delete();
                }
            }
        }
    }

    //获取SD卡路径
    public static String getSDcardPath(Context context) {
        String SDcardPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
        if (!fileExists(SDcardPath)) {
            createDirectory(SDcardPath);
        }
        return SDcardPath;
    }

    //文件是否存在
    public static boolean fileExists(String fullName) {
        File file = new File(fullName);
        return file.exists();
    }

    //创建文件夹
    public static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    //得到绝对地址
    private static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }

    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //保存图片到相册
    public static void saveImageUrl(Context context, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL myFileUrl;
                Bitmap bitmap = null;
                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn;
                    conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    String fileName = System.currentTimeMillis() + ".jpg";
                    File file = new File(fileName);
                    //把文件插入到系统图库
                    String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, null);
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(new File(getRealPathFromURI(Uri.parse(insertImage), context)));
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(context, "保存成功");
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
