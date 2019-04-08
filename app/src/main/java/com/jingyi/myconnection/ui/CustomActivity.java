package com.jingyi.myconnection.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.CustomEntity;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.json.JSONObject;

public class CustomActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private LinearLayout custom_ll_call;
    private String[] permissions=new String[]{
            Manifest.permission.CALL_PHONE};
    private String TAG="CustomActivity";
    private TextView custom_phone;
    private TextView custom_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        setBack(true);
        setTitle("客服");
        ToolBarStyle(1);
        setPermission(permissions);
        initView();
        initData();
    }

    private void initData() {
        getService();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CustomActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        custom_ll_call = findViewById(R.id.custom_ll_call);
        custom_ll_call.setOnClickListener(this);
        custom_phone=findViewById(R.id.custom_phone);
        custom_time=findViewById(R.id.custom_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_ll_call:
                callPhone(custom_phone.getText().toString().trim()
                );
                break;
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void getService(){
        String url = HttpsConts.BASE_URL + HttpsConts.SERVICE;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "getService():" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        CustomEntity customEntity=gson.fromJson(response,CustomEntity.class);
                        if(customEntity!=null){
                            if(!TextUtils.isEmpty(customEntity.getService_phone())) {
                                custom_phone.setText(customEntity.getService_phone());
                            }
                            if(!TextUtils.isEmpty(customEntity.getService_time())){
                                custom_time.setText(customEntity.getService_time());
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(CustomActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(CustomActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(CustomActivity.this).addToRequestQueue(stringRequest);
    }
}
