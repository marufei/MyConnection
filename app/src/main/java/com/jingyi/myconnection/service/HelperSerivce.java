package com.jingyi.myconnection.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jingyi.myconnection.MyApplication;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.ui.RegisterActivity;
import com.jingyi.myconnection.ui.WX_AddNearbyActivity;
import com.jingyi.myconnection.utils.KeyUtils;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.SaveUtils;
import com.jingyi.myconnection.utils.WeChatTextWrapper;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by MaRufei
 * on 2018/12/19.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class HelperSerivce extends AccessibilityService {
    public String TAG = "HelperSerivce";
    private android.os.Handler handler = new android.os.Handler();

    private int i = 0;//记录已打招呼的人数
    private int page = 1;//记录附近的人列表页码,初始页码为1
    private int prepos = -1;//记录页面跳转来源，0--从附近的人页面跳转到详细资料页，1--从打招呼页面跳转到详细资料页
    /**
     * 向附近的人自动打招呼的内容
     */
    private String hello = "你好啊";
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private LinearLayout toucherLayout;
    private int statusBarHeight;
    private ImageButton imageButton1;
    private String className;

    @Override
    public void onCreate() {
        super.onCreate();
//        floatView();
        initView();
    }

    private void initView() {
        View view = View.inflate(getApplication(), R.layout.float_view, null);
        FloatWindow
                .with(getApplicationContext())
                .setView(view)
                .setWidth(100)                               //设置控件宽高
                .setHeight(Screen.width, 0.2f)
                .setX(Screen.width, 0.8f)                                   //设置控件初始位置
                .setY(Screen.height, 0.3f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();
        view.findViewById(R.id.float_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "点击了");
                if (MyApplication.isOpen) {
//                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    MyUtils.Loge(TAG, "服务开关关闭");
                    MyApplication.isOpen = false;

                    //隐藏
                    FloatWindow.get().hide();
                    //销毁
//                    FloatWindow.destroy();

                } else {
                    if (!TextUtils.isEmpty(className) && !className.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)) {
                        MyUtils.Loge(TAG, "请返回微信首页操作");
                        MyUtils.showToast(getApplication(), "请返回微信首页操作");
                    }
//                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                    startActivity(intent);
                    MyUtils.Loge(TAG, "服务开关打开");
                    MyApplication.isOpen = true;
                }
            }
        });
    }

    private void floatView() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
//        params.width = 350;
//        params.height = 500;

//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;//点击事件穿透到桌面
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (LinearLayout) inflater.inflate(R.layout.float_view, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        Log.e(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
        Log.e(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
        Log.e(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
        Log.e(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e(TAG, "状态栏高度为:" + statusBarHeight);

//        //浮动窗口按钮.
//        imageButton1 = (ImageButton) toucherLayout.findViewById(R.id.imageButton1);
//
//        imageButton1.setOnClickListener(new View.OnClickListener() {
//            long[] hints = new long[2];
//
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG, "点击了");
//                if (!TextUtils.isEmpty(className) && !className.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)) {
//                    MyUtils.Loge(TAG, "请返回微信首页操作");
//                    MyUtils.showToast(getApplication(), "请返回微信首页操作");
////                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                    MyUtils.Loge(TAG, "服务开关打开");
//                    MyApplication.isOpen = true;
//                } else {
//                    MyUtils.Loge(TAG, "服务开关打开");
//                    MyApplication.isOpen = true;
//
//                }
//                Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                startActivity(intent);
//            }
//        });
//
//
//        imageButton1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                params.x = (int) event.getRawX() - 150;
//                params.y = (int) event.getRawY() - 150 - statusBarHeight;
//                windowManager.updateViewLayout(toucherLayout, params);
//                return false;
//            }
//        });
        ImageView float_header = toucherLayout.findViewById(R.id.float_header);
        float_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "点击了");
                if (MyApplication.isOpen) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    windowManager.removeView(toucherLayout);
                    MyUtils.Loge(TAG, "服务开关关闭");
                    MyApplication.isOpen = false;
                } else {
                    if (!TextUtils.isEmpty(className) && !className.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)) {
                        MyUtils.Loge(TAG, "请返回微信首页操作");
                        MyUtils.showToast(getApplication(), "请返回微信首页操作");
                    }
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    startActivity(intent);
                    MyUtils.Loge(TAG, "服务开关打开");
                    MyApplication.isOpen = true;
                }
            }
        });

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();

        CharSequence classNameChr = event.getClassName();
        className = classNameChr.toString();
        Log.e(TAG, "事件类型：" + event.toString());

        MyUtils.Loge(TAG, "------------------微信辅助开启");
        if (!MyApplication.isOpen){
            MyUtils.showToast(getApplication(),"开关未打开");
//            FloatWindow.get().hide();
            return;
        }

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: //TYPE_WINDOW_STATE_CHANGED
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                switch (event.getClassName().toString()) {

                    case WeChatTextWrapper.WechatClass.WECHAT_VIEWPAGER:
                        MyApplication.isOpen = true;
                        MyUtils.Loge(TAG, "进入微信首页");
                        //记录打招呼人数置零
                        i = 0;
                        //当前在微信聊天页就点开发现
                        openNext("发现");
                        //然后跳转到附近的人
                        openDelay(1000, "附近的人");
                        break;

                    case WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI:
                        MyUtils.Loge(TAG, "进入微信首页");
                        //记录打招呼人数置零
                        i = 0;
                        //当前在微信聊天页就点开发现
                        openNext("发现");
                        //然后跳转到附近的人
                        openDelay(1000, "附近的人");
                        break;
                    case WeChatTextWrapper.WechatClass.WECHAT_CLASS_NEARBY:
                        prepos = 0;
                        //当前在附近的人界面就点选人打招呼
                        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("以内");
                        Log.d("name", "附近的人列表人数: " + list.size());
                        if (i < (list.size() * page)) {
                            list.get(i % list.size()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            list.get(i % list.size()).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else if (i == list.size() * page) {
                            //本页已全部打招呼，所以下滑列表加载下一页，每次下滑的距离是一屏
                            for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                                if (nodeInfo.getChild(0).getChild(i).getClassName().equals("android.widget.ListView")) {
                                    AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(i);
                                    node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                    page++;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException mE) {
                                                mE.printStackTrace();
                                            }
                                            AccessibilityNodeInfo nodeInfo_ = getRootInActiveWindow();
                                            List<AccessibilityNodeInfo> list_ = nodeInfo_.findAccessibilityNodeInfosByText("以内");
                                            Log.d("name", "列表人数: " + list_.size());
                                            //滑动之后，上一页的最后一个item为当前的第一个item，所以从第二个开始打招呼
                                            list_.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            list_.get(1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        }
                                    }).start();
                                }
                            }
                        }
                        break;
                    case WeChatTextWrapper.WechatClass.WECHAT_CLASS_CONTACTINFOUI:
                        if (prepos == 1) {
                            //从打招呼界面跳转来的，则点击返回到附近的人页面
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            i++;
                        } else if (prepos == 0) {
                            //从附近的人跳转来的，则点击打招呼按钮
                            AccessibilityNodeInfo nodeInfo1 = getRootInActiveWindow();
                            if (nodeInfo1 == null) {
                                Log.d(TAG, "rootWindow为空");
                                return;
                            }
                            List<AccessibilityNodeInfo> list1 = nodeInfo1.findAccessibilityNodeInfosByText("打招呼");
                            if (list1.size() > 0) {
                                list1.get(list1.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                list1.get(list1.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            } else {
                                //如果遇到已加为好友的则界面的“打招呼”变为“发消息"，所以直接返回上一个界面并记录打招呼人数+1
                                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                i++;
                            }
                        }
                        break;
                    case WeChatTextWrapper.WechatClass.WECHAT_CLASS_HELLO:
                        //当前在打招呼页面
                        prepos = 1;
                        //输入打招呼的内容并发送
                        inputHello(hello);
                        openNext("发送");
                        break;
                }
                break;
        }
    }

    //自动输入打招呼内容

    private void inputHello(String hello) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //找到当前获取焦点的view
            AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            if (target == null) {
                Log.d(TAG, "inputHello: null");
                return;
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", hello);
            clipboard.setPrimaryClip(clip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        } else {
            Toast.makeText(this, "nodeInfo为null", Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        MyUtils.Loge(TAG, "系统成功绑定服务");
        //跳转到本APP
        Intent intent = new Intent(this, WX_AddNearbyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 点击匹配的nodeInfo
     *
     * @param str text关键字
     */
    private void openNext(String str) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(str);
        if (list != null && list.size() > 0) {
            list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            list.get(list.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
//            Toast.makeText(this, "找不到有效的节点", Toast.LENGTH_SHORT).show();
            MyUtils.Loge(TAG, "找不到有效的节点");
        }
    }

    //延迟打开界面
    private void openDelay(final int delaytime, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                openNext(text);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        //用imageButton检查悬浮窗还在不在，这里可以不要。优化悬浮窗时要用到。
        if (imageButton1 != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }

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


}
