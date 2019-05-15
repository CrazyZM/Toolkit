package com.android.toolkit;

import com.android.toolkitlibrary.network.http.FFNetWorkCallBack;

public class TextFragment extends BaseFragment {
    @Override
    public int getContentViewId() {
        return R.layout.fragment_text;
    }

    @Override
    public void findView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {
    }

    @Override
    public void afterCreate() {
        post("http://59.110.213.176/zmw/public/admin/Bannermanager/getAllOnlineBanner", "", String.class, new FFNetWorkCallBack<String>() {
            @Override
            public boolean onSuccess(String response) {
                showToast("好的");
                e("0000000000000000000");
                return false;
            }

            @Override
            public boolean onFail(String response) {
                return false;
            }
        });
    }
}
