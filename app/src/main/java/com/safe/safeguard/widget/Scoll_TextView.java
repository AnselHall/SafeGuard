package com.safe.safeguard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Ansel on 16/2/20.
 */
public class Scoll_TextView extends TextView{

    public Scoll_TextView(Context context) {
        super(context);
    }

    public Scoll_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Scoll_TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public void setMarqueeRepeatLimit(int marqueeLimit) {
        super.setMarqueeRepeatLimit(marqueeLimit);
    }
}
