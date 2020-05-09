package com.graduation.android.share.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduation.android.share.R;


/**
 *
 *
 * Created by Administrator on 2017/8/17.
 */

public class DialogUtil {
    private static DialogUtil mShareChooseInstance;
    private OnClickListener mDialogClickListener;
    public static DialogUtil getInstance() {
        if (mShareChooseInstance == null) {
            mShareChooseInstance = new DialogUtil();
        }
        return mShareChooseInstance;
    }
    public interface OnClickListener {
        void onQqClick();

        void onWeChatClick();

        void onQZoneClick();

        void onWeChatFriendsClick();

        void onMessageClick();
    }
    //分享弹窗
    public Dialog showShareDialog(Activity mContext, boolean showShareMessage){
        final Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_dialog_layout, null);
        LinearLayout llyShareWx=(LinearLayout) view.findViewById(R.id.lly_share_wx);
        LinearLayout llySharePyq=(LinearLayout) view.findViewById(R.id.lly_share_pyq);
        LinearLayout llyShareQQ=(LinearLayout) view.findViewById(R.id.lly_share_qq);
        LinearLayout llyShareQQkj=(LinearLayout) view.findViewById(R.id.lly_share_qqkj);
        LinearLayout llyShareMessage=(LinearLayout) view.findViewById(R.id.lly_share_message);
        LinearLayout llyMessage=(LinearLayout) view.findViewById(R.id.lly_message);
        TextView tvCancel=(TextView) view.findViewById(R.id.tv_cancel);
        llyShareMessage.setVisibility(showShareMessage? View.VISIBLE: View.GONE);
        llyMessage.setVisibility(showShareMessage? View.VISIBLE: View.GONE);
        llyShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogClickListener!=null)
                    mDialogClickListener.onWeChatClick();
            }
        });
        llySharePyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogClickListener!=null)
                    mDialogClickListener.onWeChatFriendsClick();
            }
        });
        llyShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogClickListener!=null)
                    mDialogClickListener.onQqClick();
            }
        });
        llyShareQQkj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogClickListener!=null)
                    mDialogClickListener.onQZoneClick();

            }
        });
        llyShareMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogClickListener!=null)
                    mDialogClickListener.onMessageClick();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        if (mContext instanceof Activity){
            WindowManager windowManager = mContext.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int)(display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
        }
        //获得窗体的属性
        dialog.show();//显示对话框
        return dialog;
    }

    public void setmDialogClickListener(OnClickListener mDialogClickListener) {
        this.mDialogClickListener = mDialogClickListener;
    }
//    public static String setNewBitmap(Bitmap bmp) {
//        Bitmap newBitmap = BitmapUtils.createNewBitmap(bmp);
//        String filePath = FileUtils.saveBitmap(FileUtils.getDirectory("share") + File.separator + "app_icon.jpg", newBitmap, Bitmap.CompressFormat.JPEG);
////        newBitmap.recycle();
////        newBitmap = null;
////        bmp.recycle();
////        bmp = null;
//        return filePath;
//    }
}
