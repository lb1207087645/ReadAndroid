package com.graduation.android.readme.downloadapk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.graduation.android.readme.R;
import com.graduation.android.readme.utils.FileUtils;

import java.io.File;


/**
 * 版本升级
 */
public class VersionDialog extends Dialog {
    private Context context;
    private String content;
    private String apkUrl;
    private boolean isDownload = false;
    private String sdcardPath;
    private String fileName = "meinv.apk";
    RelativeLayout imageView;
    TextView tv_updata;
    ProgressBar progress_bar;

    public VersionDialog(Context context, String apkUrl) {
        super(context);
        this.context = context;
        this.apkUrl = apkUrl;
        sdcardPath = FileUtils.getSDcardPath(context) + "/";
        initview();
    }


    public void initview() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.version_dialog, null, false);
        setContentView(view);
        imageView = findViewById(R.id.image);
        tv_updata = findViewById(R.id.tv_updata);
        progress_bar = findViewById(R.id.progress_bar);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updata();
            }
        });

    }

    public void finish() {
        this.dismiss();
    }

    public void updata() {
        if (!isDownload) {
            updateApp(apkUrl);
            tv_updata.setEnabled(false);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri data = FileProvider.getUriForFile(context, "com.graduation.android.readme.myfileprovider", new File(sdcardPath, fileName));
                intent.setDataAndType(data, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(sdcardPath, fileName)), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            dismiss();
        }
    }

    //下载更新app
    public void updateApp(String apkUrl) {

        //弄个Handler用于通知主线程
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        int downloadSize = message.arg1;
                        int fileSize = message.arg2;
                        float pre = (float) downloadSize / fileSize * 100;
                        int _pre = (int) pre;
                        progress_bar.setProgress(_pre);
//                        progressDialog.setProgressNumberFormat(String.format("%1dkb/%2dkb", downloadSize / 1024, fileSize / 1024));
                        break;

                    case 1:
                        isDownload = true;
                        tv_updata.setText("下载完成,立即安装");

                        tv_updata.setEnabled(true);

                        break;

                    case 2:
                        //progressDialog.dismiss();
                        isDownload = false;
                        tv_updata.setText("下载失败，重新下载");
                        progress_bar.setProgress(0);
                        tv_updata.setEnabled(true);
                        break;
                }
            }
        };
        //开始下载apk
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.setOnDownloadProcessListener(new FileDownloader.OnDownloadProcessListener() {
            int FileSize;

            //将要开始下载时
            @Override
            public void initDownload(int fileSize) {
                FileSize = fileSize;
            }

            //下载中
            @Override
            public void onDownloadProcess(int downloadSize) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.arg1 = downloadSize;
                message.arg2 = FileSize;
                handler.sendMessage(message);
            }

            //下载完成
            @Override
            public void onDownloadDone(int code) {
                Message message = handler.obtainMessage();
                message.what = code;
                handler.sendMessage(message);
            }

        });
        //执行下载
        fileDownloader.downloadFile(apkUrl, sdcardPath + fileName);
    }
}