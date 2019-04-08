package com.jingyi.myconnection.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jingyi.myconnection.fragment.HomeFragment;
import com.jingyi.myconnection.fragment.MineFragment;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MainVpAdapter extends FragmentPagerAdapter {
    private HomeFragment home1Fragment = null;
    private MineFragment home2Fragment = null;


    public MainVpAdapter(FragmentManager fm) {
        super(fm);
        home1Fragment = new HomeFragment();
        home2Fragment = new MineFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = home1Fragment;
                break;
            case 1:
                fragment = home2Fragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
