package com.jingyi.myconnection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.BannerEntity;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.ui.AddFriendActivity;
import com.jingyi.myconnection.ui.DeleteFriendsActivity;
import com.jingyi.myconnection.ui.WX_AddNearbyActivity;
import com.jingyi.myconnection.ui.WebActivity;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.PicassoUtlis;
import com.jingyi.myconnection.utils.SaveUtils;
import com.jingyi.myconnection.utils.SystemUtil;
import com.jingyi.myconnection.utils.VolleyUtils;
import com.jingyi.myconnection.views.ImageCycleView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageCycleView home_banner;
    private LinearLayout home_wx1;
    private LinearLayout home_add_fast;
    private LinearLayout home_add_select;
    private LinearLayout home_ll_delete;

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_home, null);
        initViews();
        initListener();
        return view;
    }

    private void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        getBanner();
    }

    private void initViews() {
        home_banner = view.findViewById(R.id.home_banner);
        home_wx1 = view.findViewById(R.id.home_wx1);
        home_wx1.setOnClickListener(this);
        home_add_fast = view.findViewById(R.id.home_add_fast);
        home_add_fast.setOnClickListener(this);
        home_add_select = view.findViewById(R.id.home_add_select);
        home_add_select.setOnClickListener(this);
        home_ll_delete=view.findViewById(R.id.home_ll_delete);
        home_ll_delete.setOnClickListener(this);
    }

    /**
     * 获取banner,类目
     */
    public void getBanner() {
        String url = HttpsConts.BASE_URL + HttpsConts.BANNER;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        BannerEntity bannerEntity = gson.fromJson(response, BannerEntity.class);
                        if (bannerEntity != null) {
                            setView(bannerEntity);
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        ActivityUtil.toLogin(getActivity(), code, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(getActivity(), "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void setView(BannerEntity bannerEntity) {
        if (bannerEntity.getBanners() != null && bannerEntity.getBanners().size() > 0) {
            home_banner.setBeans(bannerEntity.getBanners(), new ImageCycleView.Listener() {
                @Override
                public void displayImage(String imageURL, ImageView imageView) {
                    MyUtils.Loge(TAG, "imageURL:" + imageURL);
                    PicassoUtlis.img(imageURL, imageView);
                }

                @Override
                public void onImageClick(BannerEntity.BannersBean bean, View imageView) {
                    /**实现点击事件*/
//                    if (!TextUtils.isEmpty(bean.getJumpUrl())) {
//                        Web_Activity.start(getActivity(), bean.getJumpUrl());
//                    }
                    MyUtils.Loge(TAG, "点击了跳转1");
                    switch (bean.getBanner_type()) {
                        case 2://跳转链接
                            if (!TextUtils.isEmpty(bean.getLink())) {
                                MyUtils.Loge(TAG, "点击了跳转2");
                                WebActivity.start(getActivity(), bean.getLink());
                            }
                            break;
                        case 3://跳转详情
                            MyUtils.Loge(TAG, "点击了跳转3");
//                            GoodsInfo2Activity.start(getActivity(),String.valueOf(bean.getProduct_id()));
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_wx1:
                WX_AddNearbyActivity.start(getActivity());
                break;
            case R.id.home_add_fast:
                AddFriendActivity.start(getActivity(),"0");
                break;
            case R.id.home_add_select:
                AddFriendActivity.start(getActivity(),"1");
                break;
            case R.id.home_ll_delete:
                DeleteFriendsActivity.start(getActivity());
                break;
        }
    }
}
