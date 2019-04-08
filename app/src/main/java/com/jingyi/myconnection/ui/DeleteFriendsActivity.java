package com.jingyi.myconnection.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.jingyi.myconnection.Constants.AppConstant;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.utils.LXRUtil;
import com.jingyi.myconnection.utils.MyUtils;

public class DeleteFriendsActivity extends BaseActivity implements View.OnClickListener{

    private WebView delete_friends_wv;
    private static Button delete_friends_sure;
    private static DeleteFriendsActivity activity;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConstant.ADD_PHONE_DELETE:
                    if (msg.arg1==msg.arg2){
                        activity.hideLoadingAnim();
                        MyUtils.showToast(activity,"删除完成");
                        delete_friends_sure.setText("删除");
                        delete_friends_sure.setClickable(true);
                    }else{
                        delete_friends_sure.setText("删除中:"+msg.arg1+"/"+msg.arg2);
                        delete_friends_sure.setClickable(false);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_friends);
        setBack(true);
        setTitle("删除通讯录粉丝");
        ToolBarStyle(1);
        activity=this;
        initView();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DeleteFriendsActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        delete_friends_wv=findViewById(R.id.delete_friends_wv);
        delete_friends_sure=findViewById(R.id.delete_friends_sure);
        delete_friends_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_friends_sure:
                showLoadingAnim();
                LXRUtil.deleteContacts(DeleteFriendsActivity.this);
                break;
        }
    }
}
