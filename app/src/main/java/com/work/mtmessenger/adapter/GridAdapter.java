package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Qunliao;
import com.work.mtmessenger.etil.Seelistall;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Seelistall.DataBean.ArrayBean> list;


    public GridAdapter(Activity context, int resource, List<Seelistall.DataBean.ArrayBean> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<Seelistall.DataBean.ArrayBean> getList() {
        return list;
    }

    public void setList(List<Seelistall.DataBean.ArrayBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list == null ? null : list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return list == null ? null : arg0;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        String huifu1 = null, huifu2 = null, content = null;
        if (list.size() == 0) {
            return null;
        }
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grid, null);
            holder = new ViewHolder();


            holder.myiv_phot = (MyImage) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getNick_name());
        if (list.get(position).isAdmin()){
            holder.tv_name.setTextColor(Color.parseColor("#ff2f8eff"));
        }
        holder.myiv_phot.setImageURL(list.get(position).getHead_url());
        return convertView;
    }


    static class ViewHolder {
        TextView tv_name;
        MyImage myiv_phot;
    }


}

