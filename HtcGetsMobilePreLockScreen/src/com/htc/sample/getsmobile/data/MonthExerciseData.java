package com.htc.sample.getsmobile.data;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.htc.sample.getsmobile.Flags;

public class MonthExerciseData {
    
    private static final String LOG_TAG = MonthExerciseData.class.getSimpleName();
	
	private static final String MINUTES_KEY_FORMAT = MonthExerciseData.class.getName() + ".MINUTES_KEY %d %d %d";
	
	private final SharedPreferences mPrefs;

	private final Calendar mMonth;
	
	public MonthExerciseData(final Context context, final Calendar month) {
		if (Flags.LOG) Log.d(LOG_TAG, "context.getPackage() = " + context.getPackageName());

		mPrefs = context.getSharedPreferences(context.getPackageName() + "_preferences", 
				Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
		
		//mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		mMonth = (Calendar) month.clone();				
	}
	
	private String getMinutesKey(int year, int month, int day) {
		if (Flags.LOG) Log.d(LOG_TAG, "getMinutesKey year = " + year + ", month = " + month+ ", day = " + day);
		
		String value = String.format(MINUTES_KEY_FORMAT, year, month, day);
		if (Flags.LOG) Log.d(LOG_TAG, "getMinutesKey returning = " + value);
		
		return value;
	}

	public int getMinutes(int year, int month, int day) {
		String key = getMinutesKey(year, month, day);
		return mPrefs.getInt(key, 0);
	}

	public int getMinutes(int day) {
		return getMinutes(mMonth.get(Calendar.YEAR), mMonth.get(Calendar.MONTH), day);
	}

	public void setMinutes(int day, int value) {
		if (Flags.LOG) Log.d(LOG_TAG, "setMinutes day = " + day + ", value = " + value);
		
		String key = getMinutesKey(mMonth.get(Calendar.YEAR), mMonth.get(Calendar.MONTH), day);
		Editor edit = mPrefs.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public Total getContestTotal() {
		Calendar start = Calendar.getInstance();
		start.set(2012, Calendar.JUNE, 11);
		
		Calendar end = Calendar.getInstance();
		end.set(2012, Calendar.JULY, 7);
		
		return getTotal(start, end);
	}

	public Total getMonthTotal() {
		Calendar start = Calendar.getInstance();
		start.set(Calendar.DAY_OF_MONTH, 0);
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return getTotal(start, end);
	}
	
	public Total getTotal(Calendar start, Calendar end) {
		
		if (Flags.LOG) Log.d(LOG_TAG, "getTotal start = " + getYearMonthDay(start) + ", end = " + getYearMonthDay(end));

		int totalMinutes = 0;
		String description = "";
		
		do {
			final int minutes = getMinutes(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
			totalMinutes += minutes;
			if ( minutes > 0 ) {
				description += "" + minutes + " ";
			}
			
			start.add(Calendar.DAY_OF_MONTH, 1);
		
		} while ( start.before(end) );
		
		return new Total(description, totalMinutes);
	}
	
	private static String getYearMonthDay(Calendar calendar) {
		return calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH);
	}
		
}
