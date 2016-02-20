package com.safe.safeguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safe.safeguard.R;

/**
 * Created by Ansel on 16/2/20.
 */
public class SetUp1Activity extends SetBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);


    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void pre_activity() {

    }
}
