package com.jingyi.myconnection.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jingyi.myconnection.MyApplication;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.SmsTimeUtils;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText register_tel;
    private EditText register_sms;
    private TextView register_code;
    private EditText register_pwd;
    private Button register_commit;
    private TextView register_protocol;
    private CheckBox register_check;
    private String TAG="RegisterActivity";
    private EditText register_invent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setBack(true);
        setTitle("注册");
        ToolBarStyle(1);
        initView();
        //判断是否需要倒计时
        if (SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, true)) {
            SmsTimeUtils.startCountdown(register_code);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        register_tel=findViewById(R.id.register_tel);
        register_sms=findViewById(R.id.register_sms);
        register_code=findViewById(R.id.register_code);
        register_code.setOnClickListener(this);
        register_pwd=findViewById(R.id.register_pwd);
        register_commit=findViewById(R.id.register_commit);
        register_commit.setOnClickListener(this);
        register_protocol=findViewById(R.id.register_protocol);
        register_protocol.setOnClickListener(this);
        register_check=findViewById(R.id.register_check);
        register_invent=findViewById(R.id.register_invent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_code:
                if(TextUtils.isEmpty(register_tel.getText().toString())){
                    MyUtils.showToast(RegisterActivity.this,"请先输入手机号");
                    return;
                }
                getSms();
                break;
            case R.id.register_commit:
                if(TextUtils.isEmpty(register_tel.getText().toString())){
                    MyUtils.showToast(RegisterActivity.this,"请先输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(register_sms.getText().toString())) {
                    MyUtils.showToast(RegisterActivity.this, "请先输入短信验证码");
                    return;
                }
                if(TextUtils.isEmpty(register_pwd.getText().toString())){
                    MyUtils.showToast(RegisterActivity.this,"请先输入密码");
                    return;
                }
                if(!register_check.isChecked()){
                    MyUtils.showToast(RegisterActivity.this,"请先阅读并同意协议");
                    return;
                }

                register();
                break;
            case R.id.register_protocol:
                break;
        }
    }

    /**
     * 获取手机验证码
     */
    private void getSms() {
        String url = HttpsConts.BASE_URL + HttpsConts.SMS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        MyUtils.showToast(RegisterActivity.this, "验证码发送成功");
                        SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, false);
                        SmsTimeUtils.startCountdown(register_code);
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(RegisterActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(RegisterActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("tel", register_tel.getText().toString().trim());
                map.put("type", "register");
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 提交注册
     */
    private void register(){
        showLoadingAnim();
        String url = HttpsConts.BASE_URL + HttpsConts.REGISTER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        finish();
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(RegisterActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(RegisterActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("tel", register_tel.getText().toString().trim());
                map.put("vercode", register_sms.getText().toString().trim());
                map.put("password",register_pwd.getText().toString().trim());
                if(!TextUtils.isEmpty(register_invent.getText().toString())){
                    map.put("invite_code",register_invent.getText().toString().trim());
                }
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
    }
}
