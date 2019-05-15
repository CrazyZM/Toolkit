package com.android.toolkitlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.toolkitlibrary.R;
import com.android.toolkitlibrary.network.utils.StringUtils;


// 类描述： 自定义对话框 类名称：MyDialog

public class Loading extends Dialog {
    private TextView tv_message;

    public Loading(Context context) {
        super(context, R.style.my_dialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_progress);
        tv_message = (TextView) findViewById(R.id.tv_message);
    }

    public void setMessage(String message) {
        if(tv_message!=null&&!StringUtils.isEmpty(message)){
            tv_message.setText(message);
        }
    }
}
