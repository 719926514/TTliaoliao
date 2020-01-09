package com.work.mtmessenger.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.work.mtmessenger.R;


/**
 * @author ailibin
 * @date 2017/11/28
 */

public class CommonSelectorDialog extends Dialog {

    private TextView title, tvContent, tvSure, tvCancel;
    private Context context;

    public CommonSelectorDialog(Context context) {
        super(context, R.style.circleDialog);
        this.context = context;
        //通过LayoutInflater获取布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_cehui, null);
        title = view.findViewById(R.id.tv_title);
        //标题进行加粗
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);

        tvContent = view.findViewById(R.id.tv_content);
        tvSure = view.findViewById(R.id.tv_sure);
        tvCancel = view.findViewById(R.id.tv_cancel);
        setContentView(view);
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
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        //一定加上这句才起效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //false 对话框外部失去焦点
        setCanceledOnTouchOutside(false);
    }

    //解决pt适配在dialog中适配失效的问题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RudenessScreenHelper.resetDensity(getContext(), Constant.INSTANCE.getDESIGN_WIDTH());
    }

    /**
     * 设置标题
     *
     * @param titleStr
     */
    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    /**
     * 设置标题栏是否可见,默认可见
     *
     * @param visible
     */
    public void setTitleVisible(boolean visible) {
        if (visible) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
    }


    /**
     * 设置内容
     *
     * @param contentStr
     */
    public void setContent(String contentStr) {
        tvContent.setText(contentStr);
    }


    public void setOnPositiveListener(View.OnClickListener listener) {
        tvSure.setOnClickListener(listener);
    }

    public void setOnNegativeListener(View.OnClickListener listener) {
        tvCancel.setOnClickListener(listener);
    }

    /**
     * 设置底部左边的text
     */
    public void setBottomLeftText(String text) {
        tvCancel.setText(text);
    }

    /**
     * 设置底部右边的text
     */
    public void setBottomRightText(String text) {
        tvSure.setText(text);
    }
}
