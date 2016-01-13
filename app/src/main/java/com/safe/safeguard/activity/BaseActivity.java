package com.safe.safeguard.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safe.safeguard.R;

/**
 *
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {

    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseActivity() {
        mContext = this;
    }

    private RelativeLayout rl_title;
    private RelativeLayout rl_contain;
    private TextView tv_title;
    private Button btn_back;
    private Button btn_confim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initView();
        initData();
    }

    private void initData() {
        mInflater = LayoutInflater.from(mContext);
    }

    private void initView() {
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_contain = (RelativeLayout) findViewById(R.id.rl_contain);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_confim = (Button) findViewById(R.id.btn_confim);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tv_title.setText(title);
    }

    protected void setView(int sourceId) {
        View inflate = mInflater.inflate(sourceId, null);
        rl_contain.addView(inflate);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
