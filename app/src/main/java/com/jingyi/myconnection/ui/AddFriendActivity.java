package com.jingyi.myconnection.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingyi.myconnection.Constants.AppConstant;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.adapter.AddFriendLvAdapter;
import com.jingyi.myconnection.adapter.CityLvAdapter;
import com.jingyi.myconnection.adapter.ProvinceLvAdapter;
import com.jingyi.myconnection.entity.AddFriendEntity;
import com.jingyi.myconnection.entity.AgeEntity;
import com.jingyi.myconnection.entity.HangEntity;
import com.jingyi.myconnection.entity.RegionEntity;
import com.jingyi.myconnection.entity.UserInfoEntity;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.KeyUtils;
import com.jingyi.myconnection.utils.LXRUtil;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.SaveUtils;
import com.jingyi.myconnection.utils.VolleyUtils;
import com.jingyi.myconnection.views.ListViewForScrollView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriendActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private static Button add_friend_commit;
    private TextView add_friend_next;
    private ListViewForScrollView add_friend_lv;
    private String TAG = "AddFriendActivity";
    private static AddFriendLvAdapter adapter;
    private static List<AddFriendEntity.UsersBean> listData = new ArrayList<>();
    private static List<AddFriendEntity.UsersBean> addList = new ArrayList<>();
    private TextView add_friend_null;
    private TextView add_friend_select;
    /**
     * 添加到手机通讯录的总数量
     */
    private static int phones_number;

    /**
     * 是否全选
     */
    private boolean isAllSelect = false;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConstant.ADD_PHONE:
                    if ((int) msg.obj == phones_number - 1) {
                        add_friend_commit.setText("添加至手机通讯录");
                        add_friend_commit.setClickable(true);
                        for (int i = 0; i < listData.size(); i++) {
                            if (listData.get(i).getId() == addList.get((int) msg.obj).getId()) {
                                listData.get(i).setAdd(true);
                            }
                        }
                        adapter.setListData(listData);
                        adapter.notifyDataSetChanged();
                        addList.clear();
                    } else {
                        add_friend_commit.setClickable(false);
                        add_friend_commit.setText("正在添加：" + msg.obj + "/" + phones_number);

                        for (int i = 0; i < listData.size(); i++) {
                            if (listData.get(i).getId() == addList.get((int) msg.obj).getId()) {
                                listData.get(i).setAdd(true);
                            }
                        }
                        adapter.setListData(listData);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private LinearLayout add_friend_ll_location;
    private LinearLayout add_friend_ll_hang;
    private LinearLayout add_friend_ll_sex;
    private LinearLayout add_friend_ll_age;
    /**
     * 行业选择popwindow
     */
    private PopupWindow popWindow;
    private TagFlowLayout flowLayout;

    /**
     * 行业数据
     */
    private List<HangEntity.ProfessionsBean> hangList = new ArrayList<>();
    private RegionEntity regionEntity;
    private List<RegionEntity.RegionsBean> provinceList = new ArrayList<>();
    private List<RegionEntity.RegionsBean> cityList = new ArrayList<>();
    private ProvinceLvAdapter left_adapter;
    private CityLvAdapter right_adapter;

    /**
     * 省市popwindow
     */
    private PopupWindow mPopupWindow;
    private ListView pop_location_lv1;
    private ListView pop_location_lv2;
    private String province_name;
    private String city_id;
    private String city_name;

    /**
     * 是否是第一次进入这个界面
     */
    private boolean isFirst = true;
    private List<AgeEntity.AgeRangesBean> ageList = new ArrayList<>();
    private TagFlowLayout age_flow_layout;
    /**
     * 年龄范围popwindow
     */
    private PopupWindow agePopWindow;
    /**
     * 性别popwindow
     */
    private PopupWindow sexPopWindow;
    private TagFlowLayout sex_flow_layout;
    private TextView add_friend_tv_location, add_friend_tv_hang, add_friend_tv_age, add_friend_tv_sex;
    /**
     * 性别1男 2女
     */
    private String sex="";

    /**
     * 年龄范围id
     */
    private String age_range_id="";

    /**
     * 行业id
     */
    private String profession_id="";

    private String accurate;
    private LinearLayout add_friend_jing;
    private LinearLayout add_friend_xuan_top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initPopuptWindow();
        initData();
        initListener();

    }

    private void initListener() {
        add_friend_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!listData.get(position).isSelect()) {
                    listData.get(position).setSelect(true);
                    addList.add(listData.get(position));
                } else {
                    listData.get(position).setSelect(false);
                    addList.remove(listData.get(position));
                }
                MyUtils.Loge(TAG, "addList.size():" + addList.size());
                if (listData.size() == addList.size()) {
                    //创建drawable对象
                    Drawable drawable = getResources().getDrawable(R.drawable.vector_drawable_select_y);
                    //设置图片的大小
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    //在文本的右边设置图片
                    add_friend_select.setCompoundDrawables(drawable, null, null, null);
                } else {
                    //创建drawable对象
                    Drawable drawable = getResources().getDrawable(R.drawable.vector_drawable_select_n);
                    //设置图片的大小
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    //在文本的右边设置图片
                    add_friend_select.setCompoundDrawables(drawable, null, null, null);
                }
                adapter.setListData(listData);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        accurate=getIntent().getStringExtra("accurate");
        switch (accurate){
            case "0":
                setTitle("极速加友");
                add_friend_jing.setVisibility(View.GONE);
                break;
            case "1":
                setTitle("精准加友");
                add_friend_jing.setVisibility(View.VISIBLE);
                break;
        }
        getFriendData();
    }


    private void initView() {
        add_friend_commit = findViewById(R.id.add_friend_commit);
        add_friend_commit.setOnClickListener(this);
        add_friend_next = findViewById(R.id.add_friend_next);
        add_friend_next.setOnClickListener(this);
        add_friend_lv = findViewById(R.id.add_friend_lv);
        adapter = new AddFriendLvAdapter(this);
        add_friend_lv.setAdapter(adapter);
        add_friend_null = findViewById(R.id.add_friend_null);
        add_friend_select = findViewById(R.id.add_friend_select);
        add_friend_select.setOnClickListener(this);

        add_friend_ll_location = findViewById(R.id.add_friend_ll_location);
        add_friend_ll_location.setOnClickListener(this);
        add_friend_ll_hang = findViewById(R.id.add_friend_ll_hang);
        add_friend_ll_hang.setOnClickListener(this);
        add_friend_ll_sex = findViewById(R.id.add_friend_ll_sex);
        add_friend_ll_sex.setOnClickListener(this);
        add_friend_ll_age = findViewById(R.id.add_friend_ll_age);
        add_friend_ll_age.setOnClickListener(this);

        add_friend_tv_location = findViewById(R.id.add_friend_tv_location);
        add_friend_tv_hang = findViewById(R.id.add_friend_tv_hang);
        add_friend_tv_sex = findViewById(R.id.add_friend_tv_sex);
        add_friend_tv_age = findViewById(R.id.add_friend_tv_age);

        add_friend_jing=findViewById(R.id.add_friend_jing);
        add_friend_xuan_top=findViewById(R.id.add_friend_xuan_top);

    }

    public static void start(Context context,String accurate) {
        Intent intent = new Intent();
        intent.putExtra("accurate",accurate);
        intent.setClass(context, AddFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend_commit:
                String users_id = "";
                for (int i = 0; i < addList.size(); i++) {
                    if (i == 0 && i == addList.size() - 1) {
                        users_id = "" + addList.get(i).getId();
                    } else if (i != 0 && i == addList.size() - 1) {
                        users_id = users_id + addList.get(i).getId();
                    } else {
                        users_id = users_id + addList.get(i).getId() + ",";
                    }
                }
                if (!TextUtils.isEmpty(users_id)) {
                    addUsers(users_id);
                } else {
                    MyUtils.showToast(AddFriendActivity.this, "请先选择要添加的粉丝");
                }
                break;
            case R.id.add_friend_next:
                //设置置空---
                //创建drawable对象
                Drawable drawable = getResources().getDrawable(R.drawable.vector_drawable_select_n);
                //设置图片的大小
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                //在文本的右边设置图片
                add_friend_select.setCompoundDrawables(drawable, null, null, null);
                isAllSelect = false;
                getFriendData();
                break;
            case R.id.add_friend_select:
                setSelectData();
                break;
            case R.id.add_friend_ll_location:
                //获取省数据
                getRegion("100000");

                break;
            case R.id.add_friend_ll_hang:
                getHangData();
                break;
            case R.id.add_friend_ll_sex:
                showSexPopupwindow();
                break;
            case R.id.add_friend_ll_age:
                getAgeRanges();
                break;
        }
    }

    /**
     * 设置全选粉丝
     */
    private void setSelectData() {
        if (isAllSelect) {
            //如果是全选，就设置全部不选
            //创建drawable对象
            Drawable drawable = getResources().getDrawable(R.drawable.vector_drawable_select_n);
            //设置图片的大小
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //在文本的右边设置图片
            add_friend_select.setCompoundDrawables(drawable, null, null, null);
            for (int i = 0; i < listData.size(); i++) {
                if (!listData.get(i).isAdd()) {
                    listData.get(i).setSelect(false);
                }
            }
            addList.clear();
            adapter.setListData(listData);
            adapter.notifyDataSetChanged();
            isAllSelect = false;
        } else {
            //如果是全部不选，就设置全部选中
            //创建drawable对象
            Drawable drawable = getResources().getDrawable(R.drawable.vector_drawable_select_y);
            //设置图片的大小
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //在文本的右边设置图片
            add_friend_select.setCompoundDrawables(drawable, null, null, null);
            for (int i = 0; i < listData.size(); i++) {
                if (!listData.get(i).isAdd()) {
                    listData.get(i).setSelect(true);
                    addList.add(listData.get(i));
                }
            }
//            addList.addAll(listData);
            adapter.setListData(listData);
            adapter.notifyDataSetChanged();
            isAllSelect = true;
        }

    }

    /**
     * 添加粉丝
     */
    private void addUsers(final String users_id) {
        showLoadingAnim();
        String url = HttpsConts.BASE_URL + HttpsConts.ADD_USERS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        UserInfoEntity userInfoEntity = gson.fromJson(response, UserInfoEntity.class);
                        if (userInfoEntity != null && userInfoEntity.getUsers() != null) {
                            addPhone(userInfoEntity.getUsers());
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(AddFriendActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(AddFriendActivity.this, "网络有问题");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", SaveUtils.getString(KeyUtils.TOKEN));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("add_users", users_id);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AddFriendActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 添加至手机通讯录
     *
     * @param users
     */
    private void addPhone(final List<UserInfoEntity.UsersBean> users) {
        phones_number = users.size();
        //添加通讯录
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < users.size(); i++) {
                    MyUtils.Loge(TAG, "开始添加第" + i + "个");
                    LXRUtil.addContacts(AddFriendActivity.this, users.get(i).getNickname(), users.get(i).getTel(), i, 1);
                }
            }
        }).start();
    }

    /**
     * 获取粉丝列表
     */
    private void getFriendData() {
        showLoadingAnim();
        String url = HttpsConts.BASE_URL + HttpsConts.GETUSERS+"?accurate="+accurate+"&sex="+sex+"&age_range_id="+age_range_id+"&profession_id="+profession_id;
        MyUtils.Loge(TAG,"url:"+url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        AddFriendEntity addFriendEntity = gson.fromJson(response, AddFriendEntity.class);
                        if (addFriendEntity != null && addFriendEntity.getUsers() != null) {
                            setFriendView(addFriendEntity.getUsers());
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(AddFriendActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(AddFriendActivity.this, "网络有问题");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", SaveUtils.getString(KeyUtils.TOKEN));
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AddFriendActivity.this).addToRequestQueue(stringRequest);
    }

    private void setFriendView(List<AddFriendEntity.UsersBean> users) {
        listData.clear();
        addList.clear();
        listData.addAll(users);
        if (listData.size() > 0) {
            add_friend_lv.setVisibility(View.VISIBLE);
            add_friend_null.setVisibility(View.GONE);
            adapter.setListData(listData);
            adapter.notifyDataSetChanged();
        } else {
            add_friend_lv.setVisibility(View.GONE);
            add_friend_null.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取行业列表
     */
    private void getHangData() {
        showLoadingAnim();
        hangList.clear();
        String url = HttpsConts.BASE_URL + HttpsConts.HANG;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        HangEntity hangEntity = gson.fromJson(response, HangEntity.class);
                        if (hangEntity != null && hangEntity.getProfessions() != null) {
                            HangEntity.ProfessionsBean professionsBean = new HangEntity.ProfessionsBean();
                            professionsBean.setId(0);
                            professionsBean.setName("全部");
                            hangList.add(professionsBean);
                            hangList.addAll(hangEntity.getProfessions());
                            showPopupwindow();
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(AddFriendActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(AddFriendActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AddFriendActivity.this).addToRequestQueue(stringRequest);

    }

    /**
     * 展示行业popwindow
     */
    public void showPopupwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_hang, null);
        popWindow = new PopupWindow(contentView);
        popWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        flowLayout = contentView.findViewById(R.id.flow_layout);
        TagAdapter<HangEntity.ProfessionsBean> adapter = new TagAdapter<HangEntity.ProfessionsBean>(hangList) {
            @Override
            public View getView(FlowLayout parent, int position, HangEntity.ProfessionsBean professionsBean) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_pop_search,
                        parent, false);
                tv.setText(professionsBean.getName());
                return tv;
            }
        };
        flowLayout.setAdapter(adapter);
        popWindow.setAnimationStyle(R.style.AnimationTopFade);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);//点击外部收起
        popWindow.setFocusable(true);//设置焦点生效
        popWindow.showAtLocation(findViewById(R.id.add_friend_parent), Gravity.BOTTOM, 0, 0);
        popWindow.setBackgroundDrawable(getDrawable());//设置背景透明以便点击外部消失
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                MyUtils.showToast(SearchActivity.this,"点击了："+position);
                popWindow.dismiss();
                MyUtils.showToast(AddFriendActivity.this, "选中了" + hangList.get(position).getName());
                add_friend_tv_hang.setText(hangList.get(position).getName());
                if (hangList.get(position).getId() == 0) {
                    profession_id = "";
                } else {
                    profession_id = String.valueOf(hangList.get(position).getId());
                }
                //刷新数据
                getFriendData();
                return true;
            }
        });
    }

    /**
     * 生成一个 透明的背景图片
     *
     * @return
     */
    private Drawable getDrawable() {
        ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
        bgdrawable.getPaint().setColor(AddFriendActivity.this.getResources().getColor(android.R.color.transparent));
        return bgdrawable;
    }

    /**
     * 显示省市popwindow
     */
    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        //user_icon替换成你自己的layout
        View popupWindow = layoutInflater.inflate(R.layout.pop_location, null);
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //这个属性一定要设置
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
        //这是设置popupwindow出现的动画
        mPopupWindow.setAnimationStyle(R.style.AnimationTopFade);
        //ll_root是你要依赖哪个控件出现，这里是跟布局的控件id，Gravity是你要从哪里出来，这里是底部。然后后两位是x，y方向的偏移量。
//        mPopupWindow.showAtLocation(ll_root, Gravity.BOTTOM, 0, 0);
        pop_location_lv1 = popupWindow.findViewById(R.id.pop_location_lv1);
        pop_location_lv2 = popupWindow.findViewById(R.id.pop_location_lv2);
        left_adapter = new ProvinceLvAdapter(this);
        pop_location_lv1.setAdapter(left_adapter);

        right_adapter = new CityLvAdapter(this);
        pop_location_lv2.setAdapter(right_adapter);

        initLocationListener();
    }

    private void initLocationListener() {
        pop_location_lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (provinceList != null && !provinceList.get(position).isSelect()) {
                    for (int i = 0; i < provinceList.size(); i++) {
                        if (i == position) {
                            provinceList.get(i).setSelect(true);
                            province_name = provinceList.get(i).getName();
                        } else {
                            provinceList.get(i).setSelect(false);
                        }
                    }
                    left_adapter.notifyDataSetChanged();
                    MyUtils.Loge(TAG, "省ID:" + provinceList.get(position).getAdcode());
                    getRegion(provinceList.get(position).getAdcode());
                }
            }
        });
        pop_location_lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cityList != null && !cityList.get(position).isSelect()) {
                    for (int i = 0; i < cityList.size(); i++) {
                        if (i == position) {
                            cityList.get(i).setSelect(true);
                            city_id = cityList.get(position).getAdcode();
                            city_name = cityList.get(position).getName();

                            add_friend_tv_location.setText(province_name + " " + city_name);
                        } else {
                            cityList.get(i).setSelect(false);
                        }
                    }
                }
                right_adapter.notifyDataSetChanged();
//                find_by_hospital_tv1.setText(province_name+" "+cityList.get(position).getName());
                //设置城市选中状态
                setCityLvPos(position);
                mPopupWindow.dismiss();
            }
        });
    }


    /**
     * 城市列表跳转到指定位置
     *
     * @param pos
     */
    private void setCityLvPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            pop_location_lv2.smoothScrollToPosition(pos);
        } else {
            pop_location_lv2.setSelection(pos);
        }
    }

    /**
     * 获取地址
     *
     * @param padcode
     */
    private void getRegion(final String padcode) {
        String url = HttpsConts.BASE_URL + HttpsConts.REGIONS + "?padcode=" + padcode;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "getProvince()--response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        regionEntity = gson.fromJson(response, RegionEntity.class);
                        if (regionEntity != null && regionEntity.getRegions() != null) {
                            if (padcode.equals("100000")) {//设置省的数据列表
                                provinceList.addAll(regionEntity.getRegions());
                                left_adapter.setListData(provinceList);
                                left_adapter.notifyDataSetChanged();
                                if (isFirst) {
                                    getRegion(provinceList.get(0).getAdcode());
                                    isFirst = false;
                                }
                                mPopupWindow.showAtLocation(findViewById(R.id.add_friend_parent), Gravity.BOTTOM, 0, 0);
                            } else {//设置市的数据列表
                                int select_index = 0;
                                cityList.clear();
                                cityList.addAll(regionEntity.getRegions());
                                if (!TextUtils.isEmpty(city_id)) {
                                    for (int i = 0; i < cityList.size(); i++) {
                                        if (cityList.get(i).getAdcode().equals(city_id)) {
                                            cityList.get(i).setSelect(true);
                                            select_index = i;
                                        }
                                    }
                                }
                                right_adapter.setListData(cityList);
                                right_adapter.notifyDataSetChanged();
                                if (!TextUtils.isEmpty(city_id)) {
                                    //设置城市选中状态
                                    setCityLvPos(select_index);
                                    MyUtils.Loge(TAG, "设置城市选中状态");
                                }
                                MyUtils.Loge(TAG, "未设置城市选中状态");
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(AddFriendActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(AddFriendActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AddFriendActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 获取年龄范围
     */
    private void getAgeRanges() {
        ageList.clear();
        String url = HttpsConts.BASE_URL + HttpsConts.AGE_RANGES;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingAnim();
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        AgeEntity ageEntity = gson.fromJson(response, AgeEntity.class);
                        if (ageEntity != null && ageEntity.getAge_ranges() != null) {
                            AgeEntity.AgeRangesBean ageRangesBean = new AgeEntity.AgeRangesBean();
                            ageRangesBean.setId(0);
                            ageRangesBean.setName("全部");
                            ageList.add(ageRangesBean);
                            ageList.addAll(ageEntity.getAge_ranges());
                            showAgePopupwindow();
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(AddFriendActivity.this, code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim();
                MyUtils.showToast(AddFriendActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AddFriendActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 展示年龄范围popwindow
     */
    public void showAgePopupwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_age, null);
        agePopWindow = new PopupWindow(contentView);
        agePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        agePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        age_flow_layout = contentView.findViewById(R.id.age_flow_layout);
        TagAdapter<AgeEntity.AgeRangesBean> adapter = new TagAdapter<AgeEntity.AgeRangesBean>(ageList) {
            @Override
            public View getView(FlowLayout parent, int position, AgeEntity.AgeRangesBean ageRangesBean) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_pop_search,
                        parent, false);
                tv.setText(ageRangesBean.getName());
                return tv;
            }
        };
        age_flow_layout.setAdapter(adapter);
        agePopWindow.setAnimationStyle(R.style.AnimationTopFade);
        agePopWindow.setBackgroundDrawable(new BitmapDrawable());
        agePopWindow.setOutsideTouchable(true);//点击外部收起
        agePopWindow.setFocusable(true);//设置焦点生效
        agePopWindow.showAtLocation(findViewById(R.id.add_friend_parent), Gravity.BOTTOM, 0, 0);
        agePopWindow.setBackgroundDrawable(getDrawable());//设置背景透明以便点击外部消失
        age_flow_layout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                MyUtils.showToast(SearchActivity.this,"点击了："+position);
                agePopWindow.dismiss();
                MyUtils.showToast(AddFriendActivity.this, "选中了" + ageList.get(position).getName());
                add_friend_tv_age.setText(ageList.get(position).getName());
                if (ageList.get(position).getId() == 0) {
                    age_range_id = "";
                } else {
                    age_range_id = String.valueOf(ageList.get(position).getId());
                }
                //刷新数据
                getFriendData();
                return true;
            }
        });
    }

    /**
     * 展示性别范围popwindow
     */
    public void showSexPopupwindow() {
        final List<String> sexList = new ArrayList<>();
        sexList.add("全部");
        sexList.add("男");
        sexList.add("女");
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_sex, null);
        sexPopWindow = new PopupWindow(contentView);
        sexPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        sexPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        sex_flow_layout = contentView.findViewById(R.id.sex_flow_layout);
        TagAdapter<String> adapter = new TagAdapter<String>(sexList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_pop_search,
                        parent, false);
                tv.setText(sexList.get(position));
                return tv;
            }

        };
        sex_flow_layout.setAdapter(adapter);
        sexPopWindow.setAnimationStyle(R.style.AnimationTopFade);
        sexPopWindow.setBackgroundDrawable(new BitmapDrawable());
        sexPopWindow.setOutsideTouchable(true);//点击外部收起
        sexPopWindow.setFocusable(true);//设置焦点生效
        sexPopWindow.showAtLocation(findViewById(R.id.add_friend_parent), Gravity.BOTTOM, 0, 0);
        sexPopWindow.setBackgroundDrawable(getDrawable());//设置背景透明以便点击外部消失
        sex_flow_layout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                MyUtils.showToast(SearchActivity.this,"点击了："+position);
                sexPopWindow.dismiss();
                MyUtils.showToast(AddFriendActivity.this, "选中了" + sexList.get(position));
                add_friend_tv_sex.setText(sexList.get(position));
                if (position == 0) {
                    sex = "";
                } else if (position == 1) {
                    sex = "1";
                } else {
                    sex = "2";
                }
                //刷新数据
                getFriendData();
                return true;
            }
        });
    }
}
