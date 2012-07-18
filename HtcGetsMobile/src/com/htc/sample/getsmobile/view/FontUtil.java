package com.htc.sample.getsmobile.view;

import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontUtil {

	public static void setTypeface(final TextView view, final AttributeSet attrs) {
		final String typefaceFileName = attrs.getAttributeValue(null, "typeface");
		setTypeFace(view, typefaceFileName);
	}

	public static void setTypeFace(final TextView view, final String typefaceFileName) {
		if ( Integer.parseInt(Build.VERSION.SDK) >= 4 ) {
			if (typefaceFileName != null) {
				Typeface typeface = Typeface.createFromAsset(
						view.getContext().getAssets(), typefaceFileName);
				view.setTypeface(typeface);
			}
		}
	}
	
}
