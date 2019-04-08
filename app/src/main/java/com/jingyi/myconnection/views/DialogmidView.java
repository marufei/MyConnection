package com.jingyi.myconnection.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingyi.myconnection.R;


/**
 * Created by MaRufei on 2017/9/2.
 */

/**
 * @author: MaRufei
 * @date: 2017/11/20.
 * @Email: 867814102@qq.com
 * @Phone: 132 1358 0912
 * TODO:中间弹窗
 */


public class DialogmidView extends Dialog {
    private String TAG = "DialogmidView";
    public OnEventClickListenner onEventClickListenner;
    private Context context;
    private String url;
    private ImageView dialog_ad_pic;

    public DialogmidView(Context context, int layoutId) {
        super(context);
        this.context = context;
        setContentView(layoutId);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(false);
    }

    public void setOnEventClickListenner(OnEventClickListenner onEventClickListenner) {
        this.onEventClickListenner = onEventClickListenner;
    }

    public void showDialog() {
        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置弹窗位置
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        show();

        TextView dialog_add_friend_current = findViewById(R.id.dialog_add_friend_current);
        TextView dialog_add_friend_all=findViewById(R.id.dialog_add_friend_all);


    }

    public interface OnEventClickListenner {
        void onSure();

        void onCancle();

    }
}
