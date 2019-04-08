package com.jingyi.myconnection.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.jingyi.myconnection.utils.SystemUtil;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.json.JSONObject;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText login_tel;
    private EditText login_pwd;
    private Button btn_login_login;
    private TextView tv_login_register;
    private TextView tv_login_forget;
    private String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setDarkStatusIcon(true);
        SystemUtil.getAppPackage(this);
        initView();
        initData();
    }

    private void initData() {
        if(!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.TEL))){
            login_tel.setText(SaveUtils.getString(KeyUtils.TEL));
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        login_tel=findViewById(R.id.login_tel);
        login_pwd=findViewById(R.id.login_pwd);
        btn_login_login=findViewById(R.id.btn_login_login);
        btn_login_login.setOnClickListener(this);
        tv_login_register=findViewById(R.id.tv_login_register);
        tv_login_register.setOnClickListener(this);
        tv_login_forget=findViewById(R.id.tv_login_forget);
        tv_login_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:
                if(TextUtils.isEmpty(login_tel.getText().toString())){
                    MyUtils.showToast(LoginActivity.this,"请先输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(login_tel.getText().toString())){
                    MyUtils.showToast(LoginActivity.this,"请先输入密码");
                    return;
                }
                login();
                break;
            case R.id.tv_login_register:
                RegisterActivity.start(this);
                break;
            case R.id.tv_login_forget:
                break;

        }
    }

    /**
     * 登录
     */
    private void login(){
        showLoadingAnim();
        String url = HttpsConts.BASE_URL + HttpsConts.LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
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
                            MainActivity.start(LoginActivity.this);
                            MyApplication.AppExit();
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(LoginActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(LoginActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("tel", login_tel.getText().toString().trim());
                map.put("password",login_pwd.getText().toString().trim());
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }
}
