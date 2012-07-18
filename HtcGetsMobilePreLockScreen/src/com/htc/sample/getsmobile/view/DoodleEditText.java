package com.htc.sample.getsmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Displays text using a doodle font.
 *
 */
public class DoodleEditText extends EditText {

	private static final String ROUNDED_FONT = "HTCscript.otf";
	
	public DoodleEditText(Context context) {
		super(context);
		init();
	}

	public DoodleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DoodleEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		FontUtil.setTypeFace(this, ROUNDED_FONT);
	}
	
}
