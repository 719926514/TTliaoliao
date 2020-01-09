package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.asd;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;

public class SearchListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<asd> list;


    public SearchListViewAdapter(Activity context, int resource, List<asd> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<asd> getList() {
        return list;
    }

    public void setList(List<asd> list) {
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
            convertView = mInflater.inflate(R.layout.item_searchlist, null);
            holder = new ViewHolder();

            holder.myiv_phot = (MyImage) convertView.findViewById(R.id.myiv_phot);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.add = (TextView) convertView.findViewById(R.id.add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(list.get(position).getData().getNick_name());
        holder.myiv_phot.setImageURL(list.get(position).getData().getHead_url());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnAddListener.onAddListener(list.get(position).getData().getNick_name(),list.get(position).getData().getUser_name());
            }
        });


        return convertView;
    }


    static class ViewHolder {
        TextView   tv_name, add;
        MyImage myiv_phot;
    }



    /**
     * 监听添加好友
     */

    public interface onAddListener {
        void onAddListener( String CommentId,String user_name );
    }

    public void setOnAddListener(SearchListViewAdapter.onAddListener onAddListener) {
        this.mOnAddListener = onAddListener;
    }


    private SearchListViewAdapter.onAddListener mOnAddListener;
}

