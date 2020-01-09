package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Seelistall;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class SettingClassAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Seelistall.DataBean.ArrayBean> list;
    private int title;

    public SettingClassAdapter(Activity context, int resource, List<Seelistall.DataBean.ArrayBean> list, int title) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.title = title;
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
            convertView = mInflater.inflate(R.layout.item_qunliao, null);
            holder = new ViewHolder();


            holder.myiv_phot = (MyImage) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ll_zhifubao = (LinearLayout) convertView.findViewById(R.id.ll_zhifubao);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            holder.tv_name.setText(list.get(position).getNick_name());
            if (list.get(position).isAdmin()) {
                holder.tv_name.setTextColor(Color.parseColor("#ff2f8eff"));
            }
            holder.myiv_phot.setImageURL(list.get(position).getHead_url());

            if (title == 4) {
                if (list.get(position).getRole().contains("1")) {
                    holder.iv.setImageResource(R.drawable.shuohua);
                } else {
                    holder.iv.setImageResource(R.drawable.bushuohua);
                }
            }

            if (title == 5) {
                if (list.get(position).isAdmin()) {
                    holder.ll_zhifubao.setVisibility(View.GONE);
                } else {
                    holder.ll_zhifubao.setVisibility(View.VISIBLE);
                }
            }


            if (title == 6) {
                if (list.get(position).isAdmin()) {
                    holder.ll_zhifubao.setVisibility(View.VISIBLE);
                } else {
                    holder.ll_zhifubao.setVisibility(View.GONE);
                }
            }
        }
        return convertView;

    }


    static class ViewHolder {
        TextView tv_name;
        MyImage myiv_phot;
        ImageView iv;
        LinearLayout ll_zhifubao;
    }


}

