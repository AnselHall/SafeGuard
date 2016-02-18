package com.safe.safeguard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safe.safeguard.R;

/**
 * Created by user on 2016/1/14.
 */
public class SettingItem extends RelativeLayout {

    private CheckBox cb_homesetting_checkbox;
    private TextView tv_homesetting_des;
    private TextView tv_homesetting_title;
    private String des_on;
    private String des_off;

    public SettingItem(Context context) {
        super(context);
        init();
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        /*TypedArray ta = context.obtainStyledAttributes(R.styleable.SettingItem);

        for (int i = 0; i < ta.length(); i++) {
            String string = ta.getString(i);
        }

        ta.recycle();*/
        des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_off");
        des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_on");
        String setting_title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_title");

        tv_homesetting_title.setText(setting_title);
        if (isChecked()) {
            tv_homesetting_des.setText(des_on);
        } else {
            tv_homesetting_des.setText(des_off);
        }

    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View inflate = View.inflate(getContext(), R.layout.setting_item, this);
        tv_homesetting_title = (TextView) inflate.findViewById(R.id.tv_homesetting_title);
        tv_homesetting_des = (TextView) inflate.findViewById(R.id.tv_homesetting_des);
        cb_homesetting_checkbox = (CheckBox) inflate.findViewById(R.id.cb_homesetting_checkbox);

    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_homesetting_title.setText(title);
    }

    /**
     * 设置描述
     *
     * @param des
     */
    public void setDes(String des) {
        tv_homesetting_des.setText(des);
    }

    /**
     * 获取CheckBox的状态
     *
     * @return
     */
    public boolean isChecked() {
        return cb_homesetting_checkbox.isChecked();
    }

    /**
     * 设置CheckBox的状态
     *
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        cb_homesetting_checkbox.setChecked(isChecked);
        if (isChecked()) {
            tv_homesetting_des.setText(des_on);
        } else {
            tv_homesetting_des.setText(des_off);
        }
    }
}
