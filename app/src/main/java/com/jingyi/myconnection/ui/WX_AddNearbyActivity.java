package com.jingyi.myconnection.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jingyi.myconnection.R;
import com.jingyi.myconnection.service.HelperSerivce;
import com.jingyi.myconnection.utils.KeyUtils;
import com.jingyi.myconnection.utils.SaveUtils;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

public class WX_AddNearbyActivity extends BaseActivity implements View.OnClickListener {

    private Button wx_add_nearby_add;

    private String TAG = "WX_AddNearbyActivity";
    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.e(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.e(TAG, "onFail");
        }
    };

    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.e(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.e(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.e(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.e(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.e(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.e(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.e(TAG, "onBackToDesktop");
        }
    };
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx__add_nearby);
        setBack(true);
        setTitle("自动添加附近的人");
        ToolBarStyle(1);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        SaveUtils.setBoolean(KeyUtils.WX_OPEN, false);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WX_AddNearbyActivity.class);
        context.startActivity(intent);
    }

    private void initListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUtils.setBoolean(KeyUtils.WX_OPEN, true);
            }
        });
    }

    private void initView() {
        wx_add_nearby_add = findViewById(R.id.wx_add_nearby_add);
        wx_add_nearby_add.setOnClickListener(this);
        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        imageView.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_add_nearby_add:

                if (Settings.canDrawOverlays(WX_AddNearbyActivity.this)) {
                    if (isAccessibilitySettingsOn()) {
//                        Intent intent1 = new Intent(WX_AddNearbyActivity.this, HelperSerivce.class);
//                        Toast.makeText(WX_AddNearbyActivity.this, "已开启Toucher", Toast.LENGTH_SHORT).show();
//                        startService(intent1);
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        startActivity(intent);
                    }else {
                        requestAssistPermission();
                    }
                } else {
                    //若没有权限，提示获取.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Toast.makeText(WX_AddNearbyActivity.this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }


//                if (isAccessibilitySettingsOn()) {
//                    FloatWindow
//                            .with(getApplicationContext())
//                            .setView(imageView)
//                            .setWidth(100)                               //设置控件宽高
//                            .setHeight(Screen.width, 0.2f)
//                            .setX(Screen.width, 0.8f)                                   //设置控件初始位置
//                            .setY(Screen.height, 0.3f)
//                            .setDesktopShow(true)                        //桌面显示
//                            .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
//                            .setPermissionListener(mPermissionListener)  //监听权限申请结果
//                            .build();
//
//                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                    startActivity(intent);
//
//                } else {
//                    requestAssistPermission();
//                }
                break;
        }
    }

    /**
     * 检测辅助功能是否开启
     */
    public boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        String service = getPackageName() + "/" + HelperSerivce.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.d(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    /**
     * 申请辅助功能权限
     */
    private void requestAssistPermission() {
        try {
            //打开系统设置中辅助功能
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(WX_AddNearbyActivity.this, "开启服务即可", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
