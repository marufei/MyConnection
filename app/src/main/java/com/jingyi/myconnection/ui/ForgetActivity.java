package com.jingyi.myconnection.ui;

import android.support.v7.app.AppCompatActivity;
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
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.SmsTimeUtils;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetActivity extends BaseActivity implements View.OnClickListener{

    private EditText forget_tel;
    private EditText forget_sms;
    private EditText forget_pwd;
    private TextView forget_code;
    private Button forget_commit;
    private String TAG="ForgetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        setBack(true);
        setTitle("忘记密码");
        ToolBarStyle(1);
        initView();
        //判断是否需要倒计时
        if (SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, true)) {
            SmsTimeUtils.startCountdown(forget_code);
        }
        initView();
    }

    private void initView() {
        forget_tel=findViewById(R.id.forget_tel);
        forget_sms=findViewById(R.id.forget_sms);
        forget_pwd=findViewById(R.id.forget_pwd);
        forget_code=findViewById(R.id.forget_code);
        forget_code.setOnClickListener(this);
        forget_commit=findViewById(R.id.forget_commit);
        forget_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_code:
                if(TextUtils.isEmpty(forget_tel.getText().toString())){
                    MyUtils.showToast(ForgetActivity.this,"请先输入手机号");
                    return;
                }
                getSms();
                break;
            case R.id.forget_commit:
                if(TextUtils.isEmpty(forget_tel.getText().toString())){
                    MyUtils.showToast(ForgetActivity.this,"请先输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(forget_sms.getText().toString())) {
                    MyUtils.showToast(ForgetActivity.this, "请先输入短信验证码");
                    return;
                }
                if(TextUtils.isEmpty(forget_pwd.getText().toString())){
                    MyUtils.showToast(ForgetActivity.this,"请先输入密码");
                    return;
                }
                commit();
                break;
        }
    }

    /**
     * 提交
     */
    private void commit() {
        showLoadingAnim();
        String url = HttpsConts.BASE_URL + HttpsConts.FORGET;
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
                        ActivityUtil.toLogin(ForgetActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(ForgetActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("tel", forget_tel.getText().toString().trim());
                map.put("vercode", forget_sms.getText().toString().trim());
                map.put("password",forget_pwd.getText().toString().trim());
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(ForgetActivity.this).addToRequestQueue(stringRequest);
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
                        MyUtils.showToast(ForgetActivity.this, "验证码发送成功");
                        SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, false);
                        SmsTimeUtils.startCountdown(forget_code);
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(ForgetActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(ForgetActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("tel", forget_tel.getText().toString().trim());
                map.put("type", "reset");
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(ForgetActivity.this).addToRequestQueue(stringRequest);
    }
}
