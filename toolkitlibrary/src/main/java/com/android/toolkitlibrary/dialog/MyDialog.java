package com.android.toolkitlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.toolkitlibrary.R;
import com.android.toolkitlibrary.network.utils.FFUtils;


/**
 * 自定义dialog
 *
 * @author our
 */
public class MyDialog extends Dialog implements
        View.OnClickListener {
    private DialogClickListener listener;
    private Context context;
    private TextView tv_restinfo_pop_tel_content;
    private TextView dialog_no;
    private TextView dialog_ok;
    private String text_ok;
    private String text_no;
    private String content;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义dialog
     *
     * @param content  主体文字
     * @param ok       左按钮文字，若为 "" 则隐藏
     * @param no       右按钮文字，若为 "" 则隐藏
     * @param listener 回调接口
     */
    public MyDialog(Context context, String content, String ok,
                    String no, DialogClickListener listener) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.content = content;
        this.text_ok = ok;
        this.text_no = no;
        this.listener = listener;
    }

    public void setTextSize(int size) {
        dialog_no.setTextSize(size);
        dialog_ok.setTextSize(size);
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
        tv_restinfo_pop_tel_content = (TextView) findViewById(R.id.tv_restinfo_pop_tel_content);
        dialog_ok = (TextView) findViewById(R.id.dialog_ok);
        dialog_no = (TextView) findViewById(R.id.dialog_no);
        dialog_ok.setOnClickListener(this);
        dialog_no.setOnClickListener(this);
        initView();
        initDialog(context);
    }

    /**
     * 设置dialog的宽为屏幕的3分之1
     *
     * @param context
     */
    private void initDialog(Context context) {
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (FFUtils.getDisWidth() / 3 * 2); // 设置宽度
        this.getWindow().setAttributes(lp);
    }

    private void initView() {
        tv_restinfo_pop_tel_content.setText(content);
        if (text_ok.equals("")) {
            dialog_ok.setVisibility(View.GONE);
            findViewById(R.id.dialog_line).setVisibility(View.GONE);
        } else {
            dialog_no.setText(text_no);
        }
        if (text_no.equals("")) {
            dialog_no.setVisibility(View.GONE);
            findViewById(R.id.dialog_line).setVisibility(View.GONE);
        } else {
            dialog_ok.setText(text_ok);
        }
    }

    public interface DialogClickListener {
        void onOkClick(Dialog dialog);

        void onNoClick(Dialog dialog);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (listener == null) {
            return;
        }
        int i = v.getId();
        if (i == R.id.dialog_ok) {
            listener.onOkClick(this);

        } else if (i == R.id.dialog_no) {
            listener.onNoClick(this);

        }
    }
}