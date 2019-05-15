package com.android.toolkit;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTabAdapter tabAdapter;
    private LinearLayout rgs;
    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        rgs = (LinearLayout) findViewById(R.id.tabs_rg);
    }

    @Override
    public void initView() {
        setBackButtonVisible(false);
        fragments.add(new TextFragment());
        fragments.add(new TextFragment());
        fragments.add(new TextFragment());
        fragments.add(new TextFragment());
        fragments.add(new TextFragment());
    }

    @Override
    public void setListener() {
        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,
                rgs);
        tabAdapter
                .setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                    @Override
                    public void OnRgsExtraCheckedChanged(LinearLayout radioGroup,
                                                         int checkedId, int index) {
                    }
                });
    }

    @Override
    public void afterCreate() {

    }
}
