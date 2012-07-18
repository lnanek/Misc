package com.htc.sample.getsmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Displays text using a doodle font.
 *
 */
public class DoodleTextView extends TextView {

	private static final String ROUNDED_FONT = "HTCscript.otf";
	
	public DoodleTextView(Context context) {
		super(context);
		init();
	}

	public DoodleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DoodleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		FontUtil.setTypeFace(this, ROUNDED_FONT);
	}
	
}
