package com.jingyi.myconnection.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/14.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class SystemUtil {

    public static String TAG = "SystemUtil";


    public static void getAppPackage(Context context) {
        /**
         * 手机内所有安装包对象
         */
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        try {
            for (PackageInfo info : packages) {
//              if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                MyUtils.Loge(TAG, "包名：" + info.packageName + "--版本号：" + info.versionCode + "--版本名：" + info.versionName);
//              }
            }
//            for (int i=0;i<packages.size();i++) {
//                if(packages.get(i).packageName.equals("com.tencent.mm")){
//                    String wx_version=packages.get(i).versionName;
//                    MyUtils.Loge(TAG,"微信版本号为："+wx_version);
//                }else {
//                    MyUtils.Loge(TAG,"您还未安装微信");
//                }
//            }
        } catch (Exception e) {
            MyUtils.Loge(TAG, "e:" + e.getMessage());
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Context mContext,float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
