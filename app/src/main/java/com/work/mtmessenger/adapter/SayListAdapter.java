package com.work.mtmessenger.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Call;
import com.work.mtmessenger.ui.SayListActivity;
import com.work.mtmessenger.util.MediaMetadataRetriever;
import com.work.mtmessenger.util.TimeUtil;
import com.work.mtmessenger.util.image.MyImage;

import java.util.List;


public class SayListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<Call.DataBean.ArrayBean> list;
    private final String TAG = "disorderlist";


    public SayListAdapter(Activity context, int resource, List<Call.DataBean.ArrayBean> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

    }

    public List<Call.DataBean.ArrayBean> getList() {
        return list;
    }

    public void setList(List<Call.DataBean.ArrayBean> list) {
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
            convertView = mInflater.inflate(R.layout.item_saylist, parent, false);
            holder = new ViewHolder();
            holder.myiv_phot1 = (MyImage) convertView.findViewById(R.id.myiv_phot1);
            holder.myiv_phot2 = (MyImage) convertView.findViewById(R.id.myiv_phot2);
            holder.iv_icn1 = (MyImage) convertView.findViewById(R.id.iv_icn1);
            holder.iv_icn2 = (MyImage) convertView.findViewById(R.id.iv_icn2);
            holder.ll_bieren = (LinearLayout) convertView.findViewById(R.id.ll_bieren);
            holder.ll_ziji = (LinearLayout) convertView.findViewById(R.id.ll_ziji);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content1 = (TextView) convertView.findViewById(R.id.tv_content1);
            holder.tv_content2 = (TextView) convertView.findViewById(R.id.tv_content2);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_timen);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.id_recorder_anim = (View) convertView.findViewById(R.id.id_recorder_anim);
            holder.id_recorder_anim1 = (View) convertView.findViewById(R.id.id_recorder_anim1);
            holder.id_recorder_time1 = (TextView) convertView.findViewById(R.id.id_recorder_time1);
            holder.id_recorder_time = (TextView) convertView.findViewById(R.id.id_recorder_time);
            holder.tv_sym = (TextView) convertView.findViewById(R.id.tv_sym);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {


            if (list.get(position).isIs_me()) {
                //自己的
                holder.ll_ziji.setVisibility(View.VISIBLE);
                holder.ll_bieren.setVisibility(View.GONE);
                //1文本  2图片 3语音  4红包 5系统
                if (list.get(position).getValue_type() == 1) {
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.tv_content2.setVisibility(View.VISIBLE);
                    holder.tv_content2.setText(list.get(position).getValue());
                    holder.tv_content2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_btn_gren));
                    holder.id_recorder_anim1.setVisibility(View.GONE);
                    holder.id_recorder_time1.setVisibility(View.GONE);
                    holder.iv_icn2.setVisibility(View.GONE);
                } else if (list.get(position).getValue_type() == 2) {//只有ListView处于空闲状态才能加载数据
                    holder.tv_sym.setVisibility(View.GONE);
                    if (SayListActivity.mIsListViewIdle) {
                        holder.iv_icn2.setVisibility(View.VISIBLE);
                        holder.iv_icn2.setImageURL(list.get(position).getValue());
                    }
                    holder.id_recorder_anim1.setVisibility(View.GONE);
                    holder.tv_content2.setVisibility(View.GONE);
                    holder.id_recorder_time1.setVisibility(View.GONE);
                } else if (list.get(position).getValue_type() == 3) {
                    String s = MediaMetadataRetriever.getRingDuring(list.get(position).getValue());
                    int i = Integer.valueOf(s);
                    i = i / 1000;
                    holder.id_recorder_time1.setText(i + "");
                    holder.id_recorder_time1.setVisibility(View.VISIBLE);
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.id_recorder_anim1.setVisibility(View.VISIBLE);
                    holder.tv_content2.setVisibility(View.GONE);
                    holder.iv_icn2.setVisibility(View.GONE);
                } else if (list.get(position).getValue_type() == 4) {
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.id_recorder_time1.setVisibility(View.GONE);
                    holder.iv_icn2.setVisibility(View.GONE);
                    holder.tv_content2.setVisibility(View.VISIBLE);
                    holder.tv_content2.setText("");
                    holder.id_recorder_anim1.setVisibility(View.GONE);
                    if (SayListActivity.mIsListViewIdle) {//只有ListView处于空闲状态才能加载数据
                        if (list.get(position).getValue().contains("true")) {
                            holder.tv_content2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hong2));
                        } else {
                            holder.tv_content2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hong));
                        }
                    }
                } else if (list.get(position).getValue_type() == 5) {
                    holder.id_recorder_time1.setVisibility(View.GONE);
                    holder.tv_sym.setVisibility(View.VISIBLE);
                    holder.tv_sym.setText(list.get(position).getValue());
                    holder.tv_content2.setVisibility(View.GONE);
                    holder.id_recorder_anim1.setVisibility(View.GONE);
                    holder.iv_icn2.setVisibility(View.GONE);
                }
            } else {
                //别人的
                holder.ll_ziji.setVisibility(View.GONE);
                holder.ll_bieren.setVisibility(View.VISIBLE);
                //1文本  2图片 3语音 4红包 5系统
                if (list.get(position).getValue_type() == 1) {
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.tv_content1.setText(list.get(position).getValue());
                    holder.id_recorder_anim.setVisibility(View.GONE);
                    holder.tv_content1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_btn_whit));
                    holder.id_recorder_time.setVisibility(View.GONE);
                    holder.iv_icn1.setVisibility(View.GONE);
                    holder.tv_content1.setVisibility(View.VISIBLE);

                } else if (list.get(position).getValue_type() == 2) {
                    holder.tv_sym.setVisibility(View.GONE);
                    if (SayListActivity.mIsListViewIdle) {//只有ListView处于空闲状态才能加载数据
                        holder.iv_icn1.setVisibility(View.VISIBLE);
                        holder.iv_icn1.setImageURL(list.get(position).getValue());
                    }
                    holder.id_recorder_anim.setVisibility(View.GONE);
                    holder.tv_content1.setVisibility(View.GONE);
                    holder.id_recorder_time.setVisibility(View.GONE);


                } else if (list.get(position).getValue_type() == 3) {
                    String s = MediaMetadataRetriever.getRingDuring(list.get(position).getValue());
                    int i = Integer.valueOf(s);
                    i = i / 1000;
                    holder.id_recorder_time.setText(i + "");
                    holder.id_recorder_time.setVisibility(View.VISIBLE);
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.id_recorder_anim.setVisibility(View.VISIBLE);
                    holder.tv_content1.setVisibility(View.GONE);
                    holder.iv_icn1.setVisibility(View.GONE);
                } else if (list.get(position).getValue_type() == 4) {
                    holder.tv_sym.setVisibility(View.GONE);
                    holder.id_recorder_time.setVisibility(View.GONE);
                    holder.iv_icn1.setVisibility(View.GONE);
                    holder.tv_content1.setVisibility(View.VISIBLE);
                    holder.id_recorder_anim.setVisibility(View.GONE);
                    if (SayListActivity.mIsListViewIdle) {//只有ListView处于空闲状态才能加载数据
                        if (list.get(position).getValue().contains("true")) {
                            holder.tv_content1.setText("");
                            holder.tv_content1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hong2));
                        } else {
                            holder.tv_content1.setText("");
                            holder.tv_content1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hong));
                        }
                    }
                } else if (list.get(position).getValue_type() == 5) {
                    holder.id_recorder_time.setVisibility(View.GONE);
                    holder.tv_sym.setVisibility(View.VISIBLE);
                    holder.tv_sym.setText(list.get(position).getValue());
                    holder.tv_content1.setVisibility(View.GONE);
                    holder.id_recorder_anim.setVisibility(View.GONE);
                }
            }

            holder.tv_name.setText(list.get(position).getSend_nick_name());
            if (SayListActivity.mIsListViewIdle) {//只有ListView处于空闲状态才能加载数据
                holder.myiv_phot1.setImageURL(list.get(position).getSend_head_url());
                holder.myiv_phot2.setImageURL(list.get(position).getSend_head_url());
            }
//        holder.tv_time.setText(position+"");
            if (list.get(position).getCreate_time() != 0) {
                holder.tv_time.setText(TimeUtil.getStrTime(list.get(position).getCreate_time() + ""));
            }


            holder.id_recorder_anim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAddListener.onAddListener(list.get(position).getValue(), view, list.get(position).isIs_me());
                }
            });

            holder.id_recorder_anim1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAddListener.onAddListener(list.get(position).getValue(), view, list.get(position).isIs_me());
                }
            });
            holder.iv_icn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getValue_type() == 2) {
                        mOnbigListener.onbigListener(list.get(position).getValue());
                    }
                }
            });
            holder.iv_icn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getValue_type() == 2) {
                        mOnbigListener.onbigListener(list.get(position).getValue());
                    }
                }
            });


            holder.tv_content1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getValue_type() == 4) {
                        mOnopenListener.onopenListener(list.get(position).getValue_type(), list.get(position).getMessages_id(), list.get(position).isIs_me());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    static class ViewHolder {
        TextView tv_content1, tv_content2, tv_name, tv_time, id_recorder_time1, id_recorder_time, tv_sym;
        MyImage myiv_phot1, myiv_phot2, iv_icn1, iv_icn2;
        LinearLayout ll_ziji, ll_bieren;
        View id_recorder_anim, id_recorder_anim1;
    }

    /**
     * 播放音频
     */

    public interface onAddListener {
        void onAddListener(String value, View view, boolean isme);
    }

    public void setOnAddListener(SayListAdapter.onAddListener onAddListener) {
        this.mOnAddListener = onAddListener;
    }


    private SayListAdapter.onAddListener mOnAddListener;


    /**
     * 开红包
     */

    public interface onopenListener {
        void onopenListener(int Value_type, int id, boolean isme);
    }

    public void setOnopenListener(SayListAdapter.onopenListener onopenListener) {
        this.mOnopenListener = onopenListener;
    }

    private SayListAdapter.onopenListener mOnopenListener;


    /**
     * 大图
     */


    public interface onbigListener {
        void onbigListener(String Value);
    }

    public void setOnbigListener(SayListAdapter.onbigListener onbigListener) {
        this.mOnbigListener = onbigListener;
    }


    private SayListAdapter.onbigListener mOnbigListener;
}

