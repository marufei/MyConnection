package com.jingyi.myconnection.utils;

import android.app.Activity;
import android.content.Intent;

import com.jingyi.myconnection.MyApplication;
import com.jingyi.myconnection.ui.LoginActivity;

import java.util.ArrayList;

public class ActivityUtil {
	static String TAG ="TAG--ActivityUtil";

	static ArrayList<Activity> mActivities = new ArrayList<Activity>();

	public static void exitAll() {
		for (Activity activity : mActivities) {
			activity.finish();
		}
	}
	public static void add(Activity activity) {
		if (!mActivities.contains(activity)) {
			mActivities.add(activity);
		}
//		MyUtils.Loge(TAG,"启动activity得个数="+mActivities.size()+"---activity顺序-0="+mActivities.get(0).toString()+"最后="+mActivities.get(mActivities.size()-1).toString());
		getLastActivityName();
	}
	public static int select() {
		return mActivities.size();
	}
	public static void delect(Activity activity){
		if (mActivities.contains(activity)){
			mActivities.remove(activity);
		}
//		MyUtils.Loge(TAG,"启动activity得个数="+mActivities.size());

	}
	public static String getLastActivityName(){
//		MyUtils.Loge(TAG,"最后ActivityName="+mActivities.get(mActivities.size()-1).toString());
		return mActivities.get(mActivities.size()-1).toString();

	}
	public static void toLogin(Activity activity, int errcode, String errMsg){
		if(errcode==401) {
			SaveUtils.setString(KeyUtils.TOKEN,"");
			SaveUtils.setString(KeyUtils.TOKEN_TYPE,"");
			SaveUtils.setString(KeyUtils.ACCESS_TOKEN,"");
			exitAll();
			MyApplication.finishAllActivity();
			Intent intent = new Intent(activity, LoginActivity.class);
			intent.putExtra("logout", true);
			activity.startActivity(intent);
		}else {
			MyUtils.showToast(activity,errMsg);
		}
	}
}
