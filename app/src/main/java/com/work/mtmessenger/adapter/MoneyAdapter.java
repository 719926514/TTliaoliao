package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.etil.Money;
import com.work.mtmessenger.util.TimeUtil;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class MoneyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Money.DataBean.ArrayBean> list;


    public MoneyAdapter(Activity context, int resource, List<Money.DataBean.ArrayBean> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<Money.DataBean.ArrayBean> getList() {
        return list;
    }

    public void setList(List<Money.DataBean.ArrayBean> list) {
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

        if (list.size() == 0) {
            return null;
        }
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_money, null);
            holder = new ViewHolder();

            holder.myiv_phot = (ImageView) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_ok = (TextView) convertView.findViewById(R.id.tv_ok);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String status = "";
        if (list.get(position).getStatus() == 1) {
            //支出
            status = "送给";
            holder.tv_ok.setTextColor(context.getResources().getColor(R.color.green));
            holder.tv_ok.setText("-" + list.get(position).getMoney());
        } else {
            status = "收到";
            holder.tv_ok.setTextColor(context.getResources().getColor(R.color.red));
            holder.tv_ok.setText("+" + list.get(position).getMoney());
        }


        if (list.get(position).getType() == 1) {

            holder.myiv_phot.setImageResource(R.drawable.hong);

        } else {
            holder.myiv_phot.setImageResource(R.drawable.log);
        }
        holder.tv_name.setText(status + list.get(position).getTarget() + "的红包");
        holder.tv_time.setText(TimeUtil.getStrTime(list.get(position).getCreate_time() + ""));

        return convertView;
    }


    static class ViewHolder {
        TextView tv_ok, tv_name, tv_time;
        ImageView myiv_phot;
    }


}

