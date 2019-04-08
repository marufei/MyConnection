package com.jingyi.myconnection.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.AddFriendEntity;
import com.jingyi.myconnection.utils.PicassoUtlis;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MaRufei
 * on 2018/5/29.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AddFriendLvAdapter extends BaseAdapter {
    private Context context;
    private List<AddFriendEntity.UsersBean> listData = new ArrayList<>();

    public AddFriendLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<AddFriendEntity.UsersBean> listData) {
        this.listData = listData;
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_add_friend, null);
            viewHolder.item_add_friend_number = view.findViewById(R.id.item_add_friend_number);
            viewHolder.item_add_friend_address = view.findViewById(R.id.item_add_friend_address);
            viewHolder.item_add_friend_select = view.findViewById(R.id.item_add_friend_select);
            viewHolder.item_add_friend_add = view.findViewById(R.id.item_add_friend_add);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (listData != null && listData.get(i) != null) {
            if (!TextUtils.isEmpty(listData.get(i).getTel())) {
                viewHolder.item_add_friend_number.setText(listData.get(i).getTel());
            }
            viewHolder.item_add_friend_address.setText(listData.get(i).getProvince_name() + "  " + listData.get(i).getCity_name());
        }
        if (listData.get(i).isAdd()) {
            viewHolder.item_add_friend_add.setVisibility(View.VISIBLE);
            viewHolder.item_add_friend_select.setVisibility(View.GONE);
        } else {
            viewHolder.item_add_friend_add.setVisibility(View.GONE);
            viewHolder.item_add_friend_select.setVisibility(View.VISIBLE);
            if (listData.get(i).isSelect()) {
                viewHolder.item_add_friend_select.setImageResource(R.drawable.vector_drawable_select_y);
            } else {
                viewHolder.item_add_friend_select.setImageResource(R.drawable.vector_drawable_select_n);
            }
        }
        return view;
    }

    class ViewHolder {
        TextView item_add_friend_number, item_add_friend_address, item_add_friend_add;
        ImageView item_add_friend_select;
    }
}
