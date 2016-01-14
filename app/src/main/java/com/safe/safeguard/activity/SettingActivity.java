package com.safe.safeguard.activity;

import android.os.Bundle;

import com.safe.safeguard.R;

/**
 * Created by user on 2016/1/14.
 */
public class SettingActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        setView(R.layout.activity_setting);
        setTitle("设置中心");

        initView();
    }

    private void initView() {

    }
}
