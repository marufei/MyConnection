package com.jingyi.myconnection.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jingyi.myconnection.MyApplication;
import com.jingyi.myconnection.R;
import com.jingyi.myconnection.adapter.MainVpAdapter;
import com.jingyi.myconnection.fragment.HomeFragment;
import com.jingyi.myconnection.fragment.MineFragment;
import com.jingyi.myconnection.views.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CheckPermissionsActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private MyViewPager vp_show;
    private RadioButton rb0;
    private RadioButton rb1;
    private RadioGroup rg_bottom;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private List<Fragment> listFragnet = new ArrayList<>();
    long[] mHits = new long[2];
    boolean showToast = true;
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDarkStatusIcon(true);
        setPermission(permissions);
        initView();
        initEvent();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void initEvent() {
        //设置主页第一个分页被选中
        rb0.setSelected(true);
        rb0.setChecked(true);
        homeFragment = new HomeFragment();
        mineFragment = new MineFragment();
        listFragnet.add(homeFragment);
        listFragnet.add(mineFragment);
        rg_bottom.setOnCheckedChangeListener(this);
        vp_show.setAdapter(new MainVpAdapter(getSupportFragmentManager()));
        vp_show.setCurrentItem(0);
        vp_show.setOffscreenPageLimit(2);
        vp_show.addOnPageChangeListener(this);
    }

    private void initView() {
        vp_show = findViewById(R.id.vp_show);
        rb0 = findViewById(R.id.rb0);
        rb1 = findViewById(R.id.rb1);
        rg_bottom = findViewById(R.id.rg_bottom);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (vp_show.getCurrentItem()) {
                case 0:
                    rb0.setChecked(true);
                    break;
                case 1:
                    rb1.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb0:
                vp_show.setCurrentItem(0);
                break;
            case R.id.rb1:
                vp_show.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);// 数组向左移位操作
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
            MyApplication.AppExit();
            finish();
        } else {
            showToast = true;
        }
        if (showToast) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            showToast = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
