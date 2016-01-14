package com.safe.safeguard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.safe.safeguard.R;

/**
 * Created by user on 2016/1/14.
 */
public class SettingItem extends RelativeLayout {

    public SettingItem(Context context) {
        super(context);
        init();
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray ta = context.obtainStyledAttributes(R.styleable.AA);


        ta.recycle();
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View inflate = View.inflate(getContext(), R.layout.setting_item, this);
    }
}
