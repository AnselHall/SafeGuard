package com.safe.safeguard.activity;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

public abstract class SetBaseActivity extends Activity {

    public void next(View view) {
        next_activity();
    }

    public void pre(View view) {
        pre_activity();
    }

    abstract public void next_activity();

    abstract public void pre_activity();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pre_activity();
        }

        return super.onKeyDown(keyCode, event);
    }
}
