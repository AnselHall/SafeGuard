package com.safe.safeguard.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.safe.safeguard.R;

/**
 * Created by Ansel on 16/2/20.
 */
public class LostFindActivity extends BaseActivity{

    private SharedPreferences configSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手机防盗");

        initView();
        initData();

        if (configSp.getBoolean("first", true)) {
            //true : 表示是第一次进入，还没有进行任何设置，需要跳转设置向导界面进行功能设置
            Intent intent = new Intent(this,SetUp1Activity.class);
            startActivity(intent);
            finish();
        }else{
            //false  已经进行了设置，会进入手机防盗界面，显示设置功能
            setView(R.layout.activity_lostfind);
        }
    }

    private void initData() {
        configSp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void initView() {

    }
}
