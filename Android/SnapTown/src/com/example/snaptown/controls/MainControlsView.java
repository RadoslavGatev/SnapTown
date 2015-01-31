package com.example.snaptown.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.snaptown.R;

public class MainControlsView extends RelativeLayout {

	public MainControlsView(Context context) {
        super(context);
        init(null, 0);
    }

    public MainControlsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MainControlsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_main_controls, this);
    }
	
}
