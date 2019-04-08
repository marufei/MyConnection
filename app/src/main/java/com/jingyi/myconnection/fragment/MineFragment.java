package com.jingyi.myconnection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.BannerEntity;
import com.jingyi.myconnection.entity.MineEntity;
import com.jingyi.myconnection.https.HttpsConts;
import com.jingyi.myconnection.ui.CustomActivity;
import com.jingyi.myconnection.utils.ActivityUtil;
import com.jingyi.myconnection.utils.KeyUtils;
import com.jingyi.myconnection.utils.MyUtils;
import com.jingyi.myconnection.utils.PicassoUtlis;
import com.jingyi.myconnection.utils.SaveUtils;
import com.jingyi.myconnection.utils.VolleyUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView home_header;
    private TextView home_name;
    private TextView home_tel;
    private ImageView home_vip;
    private TextView mine_is_vip;
    private LinearLayout mine_ll_vip,mine_ll_ecode,mine_ll_custom,mine_ll_setting;

    @Override
    protected void lazyLoad() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        initViews();
        initData();
        initListener();
        return view;
    }

    private void initListener() {

    }

    private void initData() {
        getInfo();
    }

    private void initViews() {
        home_header=view.findViewById(R.id.home_header);
        home_name=view.findViewById(R.id.home_name);
        home_tel=view.findViewById(R.id.home_tel);
        home_vip=view.findViewById(R.id.home_vip);
        mine_is_vip=view.findViewById(R.id.mine_is_vip);

        mine_ll_vip=view.findViewById(R.id.mine_ll_vip);
        mine_ll_vip.setOnClickListener(this);
        mine_ll_ecode=view.findViewById(R.id.mine_ll_ecode);
        mine_ll_ecode.setOnClickListener(this);
        mine_ll_custom=view.findViewById(R.id.mine_ll_custom);
        mine_ll_custom.setOnClickListener(this);
        mine_ll_setting=view.findViewById(R.id.mine_ll_setting);
        mine_ll_setting.setOnClickListener(this);
    }

    private void getInfo(){
        String url = HttpsConts.BASE_URL + HttpsConts.MINE;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = new Gson();
                        MineEntity mineEntity=gson.fromJson(response,MineEntity.class);
                        if (mineEntity!=null&&mineEntity.getUser()!=null){
                            setPersonInfoView(mineEntity.getUser());
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", SaveUtils.getString(KeyUtils.TOKEN));
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void setPersonInfoView(MineEntity.UserBean user) {
        if (!TextUtils.isEmpty(user.getNickname())){
            home_name.setText(user.getNickname());
        }
        if(!TextUtils.isEmpty(user.getAvatar_url())){
            PicassoUtlis.rountimg(user.getAvatar_url(),home_header);
        }
        if (!TextUtils.isEmpty(user.getTel())){
            home_tel.setText(user.getTel());
        }
        if(user.getMember_level()==2){
            //是会员
            home_vip.setImageResource(R.drawable.vector_drawable_vip_y);
            mine_is_vip.setVisibility(View.GONE);
        }else {
            home_vip.setImageResource(R.drawable.vector_drawable_vip_n);
            mine_is_vip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_ll_vip:
                break;
            case R.id.mine_ll_ecode:
                break;
            case R.id.mine_ll_custom:
                CustomActivity.start(getActivity());
                break;
            case R.id.mine_ll_setting:
                break;

        }
    }
}
