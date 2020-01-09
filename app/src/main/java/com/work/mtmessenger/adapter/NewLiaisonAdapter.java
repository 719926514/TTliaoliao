package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class NewLiaisonAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Lgin.DataBean.FriendRequestArrayBean> list;


    public NewLiaisonAdapter(Activity context, int resource, List<Lgin.DataBean.FriendRequestArrayBean> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<Lgin.DataBean.FriendRequestArrayBean> getList() {
        return list;
    }

    public void setList(List<Lgin.DataBean.FriendRequestArrayBean> list) {
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
            convertView = mInflater.inflate(R.layout.item_newliaison, null);
            holder = new ViewHolder();

            holder.myiv_phot = (MyImage) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_ok = (TextView) convertView.findViewById(R.id.tv_ok);
            holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(list.get(position).getNick_name());
        holder.myiv_phot.setImageURL(list.get(position).getHead_url());
        holder.tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAddListener.onAddListener(list.get(position).getApplicant_id(),position);
            }
        });

        holder.tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnnotListener.onnotListener(list.get(position).getApplicant_id(),position);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView tv_ok, tv_name, tv_no;
        MyImage myiv_phot;
    }



    /**
     * 监听接受
     */

    public interface onAddListener {
        void onAddListener( int applicant_id,int position);
    }

    public void setOnAddListener(NewLiaisonAdapter.onAddListener onAddListener) {
        this.mOnAddListener = onAddListener;
    }


    private NewLiaisonAdapter.onAddListener mOnAddListener;



    /**
     * 监听拒绝
     */
    public interface onnotListener {
        void onnotListener( int applicant_id,int position);
    }

    public void setOnnotListener(NewLiaisonAdapter.onnotListener onnotListener) {
        this.mOnnotListener = onnotListener;
    }


    private NewLiaisonAdapter.onnotListener mOnnotListener;


}

