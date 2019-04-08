package com.jingyi.myconnection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.jingyi.myconnection.R;
import com.jingyi.myconnection.entity.RegionEntity;

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
public class CityLvAdapter extends BaseAdapter {
    private Context context;
    private List<RegionEntity.RegionsBean> listData=new ArrayList<>();
    public CityLvAdapter(Context context){
        this.context=context;
    }

    public void setListData(List<RegionEntity.RegionsBean> listData) {
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
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= View.inflate(context, R.layout.item_classify_left,null);
            viewHolder.item_classify_name=view.findViewById(R.id.item_classify_name);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.item_classify_name.setText(listData.get(i).getName());
        if(listData.get(i).isSelect()){
            viewHolder.item_classify_name.setTextColor(context.getResources().getColor(R.color.blue_6a));
        }else {
            viewHolder.item_classify_name.setTextColor(context.getResources().getColor(R.color.gray_4a));
        }
        return view;
    }

    class ViewHolder{
        TextView item_classify_name;
    }
}
