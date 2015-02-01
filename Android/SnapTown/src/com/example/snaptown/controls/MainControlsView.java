package com.example.snaptown.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.snaptown.R;

public class MainControlsView extends LinearLayout {

	private static final int ATTR_NEWS_FEED_SELECTED = 1;
	private static final int ATTR_TOWNS_SELECTED = 2;

	private Button newsFeedButton;
	private Button townsButton;
	private View newsFeedBorder;
	private View townsBorder;

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

		newsFeedButton = (Button) findViewById(R.id.news_feed_button);
		townsButton = (Button) findViewById(R.id.towns_button);
		newsFeedBorder = findViewById(R.id.main_controls_border_news_feed);
		townsBorder = findViewById(R.id.main_controls_border_towns);

		if (attrs != null) {
			TypedArray a = getContext().getTheme().obtainStyledAttributes(
					attrs, R.styleable.MainControlsView, 0, 0);
			try {
				int selectedButton = a.getInteger(
						R.styleable.MainControlsView_selectedButton, 0);
				if (selectedButton == ATTR_NEWS_FEED_SELECTED) {
					newsFeedBorder.setVisibility(View.INVISIBLE);
					townsButton.setBackgroundColor(getContext().getResources()
							.getColor(R.color.light_gray));
				} else if (selectedButton == ATTR_TOWNS_SELECTED) {
					townsBorder.setVisibility(View.INVISIBLE);
					newsFeedButton.setBackgroundColor(getContext()
							.getResources().getColor(R.color.light_gray));
				}
			} finally {
				a.recycle();
			}
		}
	}

}
