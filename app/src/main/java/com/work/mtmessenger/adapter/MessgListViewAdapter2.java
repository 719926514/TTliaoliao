package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.GroupUnreadArray;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.util.TimeUtil;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class MessgListViewAdapter2 extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Lgin.DataBean.GroupUnreadArray> list;


    public MessgListViewAdapter2(Activity context, int resource, List<Lgin.DataBean.GroupUnreadArray> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<Lgin.DataBean.GroupUnreadArray> getList() {
        return list;
    }

    public void setList(List<Lgin.DataBean.GroupUnreadArray> list) {
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
            convertView = mInflater.inflate(R.layout.item_messglist, null);
            holder = new ViewHolder();

            holder.myiv_phot = (MyImage) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_context);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_unRead_message = (TextView) convertView.findViewById(R.id.tv_unRead_message);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (list != null) {
            holder.tv_name.setText(list.get(position).getSend_nick_name());
            if (list.get(position).getNews_messages() != null) {
                holder.tv_content.setText(list.get(position).getNews_messages());
            }
            if (list.get(position).getNews_create_time() != 0) {
                holder.tv_time.setText(TimeUtil.getStrTime(list.get(position).getNews_create_time() + ""));
            }
            if (list.get(position).getUnread_array() == 0) {
                holder.tv_unRead_message.setVisibility(View.GONE);
            } else if (list.get(position).getUnread_array() > 99) {
                holder.tv_unRead_message.setVisibility(View.VISIBLE);
                holder.tv_unRead_message.setText(99 + "+");
            } else {
                holder.tv_unRead_message.setVisibility(View.VISIBLE);
                holder.tv_unRead_message.setText(list.get(position).getUnread_array() + "");
            }
            holder.myiv_phot.setImageURL(list.get(position).getSend_head_url());

        }

        return convertView;
    }


    static class ViewHolder {
        TextView tv_content, tv_name, tv_time, tv_unRead_message;
        MyImage myiv_phot;
    }


}

