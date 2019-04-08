package com.jingyi.myconnection.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Clearlee on 2017/12/22 0023.
 * 微信版本7.0.0
 */

public class WeChatTextWrapper {

    public static final String WECAHT_PACKAGENAME = "com.tencent.mm";


    public static class WechatClass{
        //微信首页
        public static final String WECHAT_CLASS_LAUNCHUI = "com.tencent.mm.ui.LauncherUI";
        //微信联系人页面
        public static final String WECHAT_CLASS_CONTACTINFOUI = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";
        //微信聊天页面
        public static final String WECHAT_CLASS_CHATUI = "com.tencent.mm.ui.chatting.ChattingUI";

        //附近的人界面
        public static final String WECHAT_CLASS_NEARBY="com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI";

        //打招呼界面
        public static final String WECHAT_CLASS_HELLO="com.tencent.mm.ui.contact.SayHiEditUI";

        public static final String WECHAT_VIEWPAGER="com.tencent.mm.ui.mogic.WxViewPager";
    }


    public static class WechatId{
        /**
         * 通讯录界面
         */
        public static final String WECHATID_CONTACTUI_LISTVIEW_ID = "com.tencent.mm:id/m_";
        public static final String WECHATID_CONTACTUI_ITEM_ID = "com.tencent.mm:id/n4";
        public static final String WECHATID_CONTACTUI_NAME_ID = "com.tencent.mm:id/n8";

        /**
         * 聊天界面
         */
        public static final String WECHATID_CHATUI_EDITTEXT_ID = "com.tencent.mm:id/alm";
        public static final String WECHATID_CHATUI_USERNAME_ID = "com.tencent.mm:id/jw";
        public static final String WECHATID_CHATUI_BACK_ID = "com.tencent.mm:id/ju";
        public static final String WECHATID_CHATUI_SWITCH_ID = "com.tencent.mm:id/als";
    }

    private static final String ACTION = "action";
    private static final String ACTION_START_ACCESSIBILITY_SETTING = "action_start_accessibility_setting";


}
