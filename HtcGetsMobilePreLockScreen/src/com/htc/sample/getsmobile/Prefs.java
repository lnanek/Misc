package com.htc.sample.getsmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefs {
    
    //private static final String LOG_TAG = Prefs.class.getSimpleName();
	
	private static final String SHOWED_LOCKSCREEN_DIALOG_KEY = Prefs.class.getName() + ".SHOWED_LOCKSCREEN_DIALOG_KEY";
	
	private final SharedPreferences mPrefs;

	public Prefs(final Context context) {
		mPrefs = context.getSharedPreferences(context.getPackageName() + "_preferences", 
				Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
	}
	
	public boolean getShowedLockScreenDialog() {
		return mPrefs.getBoolean(SHOWED_LOCKSCREEN_DIALOG_KEY, false);
	}

	public void setShowedLockScreenDialog(final boolean newValue) {
		final Editor edit = mPrefs.edit();
		edit.putBoolean(SHOWED_LOCKSCREEN_DIALOG_KEY, newValue);
		edit.commit();
	}

}
