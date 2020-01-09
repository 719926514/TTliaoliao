package com.work.mtmessenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.SortModel;
import com.work.mtmessenger.ui.SayListActivity;
import com.work.mtmessenger.util.image.MyImage;
import com.work.mtmessenger.util.image.MyImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xp
 * @date: 2017/7/19
 */
public class SortAdapter extends SlideRecyclerViewBaseAdapter {
    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;
    private String name;
    private int id;
    private CallBack mCallBack;
    private SparseBooleanArray cb_result;

    public SortAdapter(Context context, List<SortModel> data, CallBack callback) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
        this.mCallBack = callback;

        cb_result = new SparseBooleanArray();
        for (int i = 0; i < data.size(); i++) {
            cb_result.put(i, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvTag.setVisibility(View.VISIBLE);
            viewHolder.tvTag.setText(mData.get(position).getLetters());
        } else {
            viewHolder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        cb_result = new SparseBooleanArray();
        for (int i = 0; i < mData.size(); i++) {
            cb_result.put(i, false);
        }

        if (this.mData.get(position).getHead_url() != null) {
            viewHolder.myImageView.setImageURL(this.mData.get(position).getHead_url());
        }
        viewHolder.tvName.setText(this.mData.get(position).getNick_name());

        viewHolder.cb.setTag(position);
        viewHolder.cb.setChecked(cb_result.get(position));
        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                cb_result.put(pos, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[0];
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName, tvDelete, tvTop, tvDowm;
        MyImage myImageView;
        SlideTouchView mSlideTouchView;
        CheckBox cb;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tag);
            tvName = (TextView) itemView.findViewById(R.id.name);
                cb = (CheckBox) itemView.findViewById(R.id.cb);
//            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
//            tvTop = (TextView) itemView.findViewById(R.id.tv_top);
//            tvDowm = (TextView) itemView.findViewById(R.id.tv_dowm);
            myImageView = (MyImage) itemView.findViewById(R.id.myimage);
//            mSlideTouchView = (SlideTouchView) itemView.findViewById(R.id.mSlideTouchView);
            /**
             * must call
             */
            bindSlideState(mSlideTouchView);
        }
    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<SortModel> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public interface CallBack {
        void deleteItem(int position);

//        void dowmItem(int position);
//
//        void topItem(int position);
    }



    /**
     * 1---全选，2---全不选3---反选
     *
     * @param state
     */
    public void setCheckBoxState(int state) {
        if (cb_result != null && cb_result.size() != 0) {
            int size = cb_result.size();
            switch (state) {
                case 1:
                    for (int i = 0; i < size; i++) {
                        cb_result.put(i, true);
                    }
                    break;
                case 2:
                    for (int i = 0; i < size; i++) {
                        cb_result.put(i, false);
                    }
                    break;
                case 3:
                    for (int i = 0; i < size; i++) {
                        Boolean flag = cb_result.valueAt(i);
                        if (flag != null) {
                            cb_result.put(i, !flag);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 获取选中结果
     *
     */
    public List<SortModel> getCbsResult() {
        List<SortModel> users = new ArrayList<SortModel>();
        int size = cb_result.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                if (cb_result.valueAt(i)) {
                    users.add(mData.get(cb_result.keyAt(i)));
                }
            }
        }
        return users;
    }
}
