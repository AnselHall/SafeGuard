package com.safe.safeguard.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.safe.safeguard.R;
import com.safe.safeguard.widget.SettingItem;

/**
 * Created by user on 2016/1/14.
 */
public class SettingActivity extends BaseActivity{

    private SettingItem si_settingitem_update;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        setView(R.layout.activity_setting);
        setTitle("设置中心");

        initView();
        initDate();
        
        registerListener();
        
    }

    private void initDate() {
        sp = getSharedPreferences("update", MODE_PRIVATE);
        boolean isChecked = sp.getBoolean("isChecked", false);
        si_settingitem_update.setChecked(isChecked);
    }

    private void initView() {

        si_settingitem_update = (SettingItem) findViewById(R.id.si_settingitem_update);
    }


    private void registerListener() {
        si_settingitem_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.si_settingitem_update:
                boolean isChecked = si_settingitem_update.isChecked();
                si_settingitem_update.setChecked(!isChecked);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isChecked", !isChecked);
                edit.commit();

                break;
        }
    }
}
