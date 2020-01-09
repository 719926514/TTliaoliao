package com.work.mtmessenger.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.work.mtmessenger.R;


/**
 * @desc:
 * @projectName:Future
 * @author:xuwh
 * @date:2019/3/26 17:29
 * @UpdateUser： 更新者
 * @UpdateDate: 2019/3/26 17:29
 * @UpdateRemark: 更新说明
 * @version:
 */
public class Shensu2Dialog extends Dialog {

    protected Context context;
    private View view;
    private TextView tv_xiangce, tv_paizao,tv_hongbao;


    public Shensu2Dialog(Context context) {
        super(context, R.style.photo_dialog_style);
        this.context = context;
        init(R.layout.activity_dialog);
    }

    private void init(int layoutId) {
        view = LayoutInflater.from(context).inflate(layoutId, null);
        findView(view);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.windowAnimations = R.style.animation_for_translation_pw;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    /**
     * 初始化布局，可重写
     *
     * @param view
     */
    protected void findView(View view) {
        tv_xiangce = (TextView) view.findViewById(R.id.tv_xiangce);
        tv_paizao = (TextView) view.findViewById(R.id.tv_paizao);
        tv_hongbao= (TextView) view.findViewById(R.id.tv_hongbao);
        tv_xiangce.setOnClickListener(onClickListener);
        tv_paizao.setOnClickListener(onClickListener);
        tv_hongbao.setOnClickListener(onClickListener);

    }

    /**
     * show之后设置属性
     */
    public void setAttributes() {
        //对话框属性
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //所有弹窗的背景透明度都为0.6
        lp.dimAmount = 0.6f;
        WindowManager m = ((Activity) context).getWindowManager();
        // 为获取屏幕宽、高
        Display d = m.getDefaultDisplay();
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = ScreenUtils.dip2px(context, 275.0f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        //一定加上这句才起效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //false 对话框外部失去焦点
        setCanceledOnTouchOutside(false);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onButtonClickListener != null) {
                int i = v.getId();
                if (i == R.id.tv_xiangce) {
                    onButtonClickListener.onbt_xiangceClick(v);

                } else if (i == R.id.tv_paizao) {
                    onButtonClickListener.ontv_paizaoClick(v);

                } else if (i == R.id.tv_hongbao) {
                    onButtonClickListener.bt_hongbaoClick(v);
                }else {
                    return;
                }
            }
        }
    };

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onbt_xiangceClick(View view);

        void ontv_paizaoClick(View view);

        void bt_hongbaoClick(View view);
    }


}
