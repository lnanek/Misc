package com.htc.sample.getsmobile.view;

import java.util.Calendar;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.data.MonthExerciseData;

public class MonthViewUpdater {
    
    public static interface OnDayClickedListener {
    	void onDayClicked(int year, int month, int day);
    }
    
    private static final int[] DAYS_OF_WEEK = new int[] {
    	Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, 
    	Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
    };
    
    private static final int LIGHT_BLUE = Color.parseColor("#dce6f1");

    private final ViewGroup mView;
    
    private final int mYear;
    
    private final int mMonth;
    
    private final int lastDayOfMonth;
    
    private final int dayOfWeekOfFirstDay;
    
    private final int dayOfWeekOfLasttDay;
    
    private final Integer mCurrentDay;
    
    private OnDayClickedListener mClickListener;
    
    public MonthViewUpdater(final ViewGroup view, final Calendar month) {

    	mView = view;

    	// Highlight today.
    	final Calendar now = Calendar.getInstance();
		if (month.get(Calendar.YEAR) == now.get(Calendar.YEAR)
				&& month.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
			mCurrentDay = now.get(Calendar.DAY_OF_MONTH);
		} else {
			mCurrentDay = null;
		}
    	
		final Calendar calendar = (Calendar) month;
		calendar.set(Calendar.DAY_OF_MONTH, 1);		
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);        
    	lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    	dayOfWeekOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);		
		dayOfWeekOfLasttDay = calendar.get(Calendar.DAY_OF_WEEK);    	
    }  
    
    public void setOnDayClickedListener(OnDayClickedListener clickListener) {
    	mClickListener = clickListener;
    }
	
    public void update(final MonthExerciseData data) {

    	// For each week of the calendar view
    	int setupDayOfMonth = 1;
    	boolean closedLastDay = false;
    	for ( int rowIndex = 0; rowIndex < mView.getChildCount() ; rowIndex++ ) {
    		final boolean moreDaysToSetupThisRow = setupDayOfMonth <= lastDayOfMonth;
    		final boolean lastVisibleRow = moreDaysToSetupThisRow && !(setupDayOfMonth+7 <= lastDayOfMonth);
    		final ViewGroup row = (ViewGroup) mView.getChildAt(rowIndex);    
    		row.setVisibility(moreDaysToSetupThisRow ? View.VISIBLE : View.GONE);

    		boolean dayOfWeekBeforeFirstDayOfWeek = true;
    		boolean dayOfWeekAfterLastDayOfWeek = true;

    		for ( int childIndex = 0; childIndex < DAYS_OF_WEEK.length ; childIndex++ ) {

        		final ViewGroup border = (ViewGroup) row.getChildAt(childIndex);			

    			final int dayOfWeekConstant = DAYS_OF_WEEK[childIndex];
        		
        		if ( dayOfWeekConstant == dayOfWeekOfFirstDay ) {
        			dayOfWeekBeforeFirstDayOfWeek = false;
        		}
        		final boolean beforeFirstDay = 1 == setupDayOfMonth && dayOfWeekBeforeFirstDayOfWeek;
        		final boolean afterLastDay = setupDayOfMonth > lastDayOfMonth;
        		final boolean empty = beforeFirstDay || afterLastDay;

           		int rightBorder = childIndex == DAYS_OF_WEEK.length - 1 ? 1 : 0;
           		final int topBorder = (0 == rowIndex && !dayOfWeekBeforeFirstDayOfWeek) 
           				|| (1 == rowIndex && dayOfWeekBeforeFirstDayOfWeek) ? 1 : 0;
           		int leftBorder = (0 == rowIndex && dayOfWeekBeforeFirstDayOfWeek) ? 0 : 1;
           		int bottomBorder = (0 == rowIndex && dayOfWeekBeforeFirstDayOfWeek) ? 0 : 1;
           		if ( lastVisibleRow && !dayOfWeekAfterLastDayOfWeek ) {
           			rightBorder = 0;
           			bottomBorder = 0;
           			leftBorder = closedLastDay ? 0 : 1;
           			closedLastDay = true;
           		}

           		border.setPadding(leftBorder, topBorder, rightBorder, bottomBorder);           			

           		border.getChildAt(0).setPadding(leftBorder == 1 ? 0 : 1, 
           				topBorder == 1 ? 0 : 1,
           						rightBorder == 1 ? 0 : 1,
           								bottomBorder == 1 ? 0 : 1);

    			final ViewGroup col = (ViewGroup) border.getChildAt(0);
        		setupDay(data, col, setupDayOfMonth, empty);

        		if ( dayOfWeekConstant == dayOfWeekOfLasttDay ) {
        			dayOfWeekAfterLastDayOfWeek = false;
        		}
        		if (!empty) {
        			setupDayOfMonth++;        				
        		}
        	}
    	}
    }

	private void setupDay(final MonthExerciseData data, final View dayLayout, final int setupDayOfMonth, final boolean empty) {

		final TextView dayTextView = (TextView) dayLayout.findViewById(R.id.date);
		final TextView minutesExercisedTextView = (TextView) dayLayout.findViewById(R.id.date_minutes);
		
		if ( empty ) {
			dayTextView.setText(" ");
			minutesExercisedTextView.setText(" ");
			dayLayout.setOnClickListener(null);
			dayLayout.setBackgroundColor(Color.WHITE);
			return;
		}

		// mark current day
		if ( null != mCurrentDay && mCurrentDay.equals(setupDayOfMonth) ) {
			dayLayout.setBackgroundColor(LIGHT_BLUE);
		} else {
			dayLayout.setBackgroundColor(Color.WHITE);
		}

		dayTextView.setText("" + setupDayOfMonth);

		int minutesData = data.getMinutes(setupDayOfMonth);
		if (minutesData > 0) {
			minutesExercisedTextView.setText("" + data.getMinutes(setupDayOfMonth));
		} else {
			minutesExercisedTextView.setText(" ");
		}
		
		dayLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( null != mClickListener ) {
					mClickListener.onDayClicked(mYear, mMonth, setupDayOfMonth);
				}
			}
		});
	}

}