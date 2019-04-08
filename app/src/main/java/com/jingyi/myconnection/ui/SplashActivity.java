package com.jingyi.myconnection.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingyi.myconnection.MyApplication;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.LoginEntity;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.KeyUtils;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.SaveUtils;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //跳转到MainActivity，并结束当前的LauncherActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        };
    };
    private String TAG="SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setDarkStatusIcon(true);
        setContentView(R.layout.activity_splash);


        initData();

    }

    private void initData() {
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.TOKEN))){
            refreshToken();
        }else {
            // 停留3秒后发送消息，跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000);
        }
    }

    /**
     * 刷新token
     */
    private void refreshToken(){
        String url = HttpsConts.BASE_URL + HttpsConts.REFRESH;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson=new Gson();
                        LoginEntity loginEntity=gson.fromJson(response,LoginEntity.class);
                        if (loginEntity!=null&&loginEntity.getData()!=null){
                            SaveUtils.setString(KeyUtils.ACCESS_TOKEN,loginEntity.getData().getAccess_token());
                            SaveUtils.setInt(KeyUtils.EXPIRES_IN,loginEntity.getData().getExpires_in());
                            SaveUtils.setString(KeyUtils.TOKEN_TYPE,loginEntity.getData().getToken_type());
                            SaveUtils.setString(KeyUtils.TOKEN,loginEntity.getData().getToken_type()+" "+loginEntity.getData().getAccess_token());
                            MyApplication.member_level=loginEntity.getData().getMember_level();
                            MainActivity.start(SplashActivity.this);
                            finish();
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(SplashActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(SplashActivity.this, "网络有问题");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", SaveUtils.getString(KeyUtils.TOKEN));
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(SplashActivity.this).addToRequestQueue(stringRequest);
    }
}
