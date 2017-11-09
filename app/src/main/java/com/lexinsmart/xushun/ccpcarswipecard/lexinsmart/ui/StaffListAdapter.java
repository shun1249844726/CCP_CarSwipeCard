package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;

import java.util.List;

/**
 * Created by xushun on 2017/11/9.
 * 功能描述：
 * 心情：
 */

public class StaffListAdapter extends BaseAdapter{
    private List<SwipCardLog> mSwipCardLogs ;
    private LayoutInflater mLayoutInflater;
    public StaffListAdapter(Context context ,List<SwipCardLog> swipCardLogs){
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mSwipCardLogs = swipCardLogs;
    }
    @Override
    public int getCount() {
        return mSwipCardLogs.size();
    }

    @Override
    public Object getItem(int position) {
        return mSwipCardLogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_staff_list,null);

            holder.staffName = (TextView) convertView.findViewById(R.id.tv_staff_list_name);
            holder.staffNumber = (TextView) convertView.findViewById(R.id.tv_staff_list_staffnum);
            holder.swipCount = (TextView) convertView.findViewById(R.id.tv_staff_list_swipcount);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.staffName.setText(mSwipCardLogs.get(position).getName());
        holder.staffNumber.setText(mSwipCardLogs.get(position).getStaffnum());
        holder.swipCount.setText(mSwipCardLogs.get(position).getSwipCount()+"");


        return convertView;
    }
    public final class ViewHolder{
        public TextView staffName;
        public TextView staffNumber;
        public TextView swipCount;
    }
}
