/*
 * Copyright (C) 2012 HTC Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.htc.sample.getsmobile.lockscreen;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htc.lockscreen.fusion.idlescreen.SimpleIdleScreenEngine;
import com.htc.lockscreen.fusion.open.SimpleEngine;
import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.activity.SetMinutesDialog;
import com.htc.sample.getsmobile.data.MonthExerciseData;
import com.htc.sample.getsmobile.view.MonthViewUpdater;
import com.htc.sample.getsmobile.view.MonthViewUpdater.OnDayClickedListener;

/**
 * The lock screen wrapper implementation class. <br>
 * Use this class to target legacy API and emulator support in addition to the current API. <br>
 * See AndroidManifest to toggle support for each of the three cases there.
 * 
 */
public class LockScreenImpl extends LockScreenImplBase implements LockScreenListener {

    private Calendar mMonth;
	
    private MonthExerciseData mData;
	
    private ViewGroup mCalendarView;
	
    private TextView mTitle;
    
	public LockScreenImpl(Context service, SimpleEngine engine) {
		super(service, engine);
	}

	// support for older deprecated API
	public LockScreenImpl(Context service, SimpleIdleScreenEngine engine) {
		super(service, engine);
	}

	// support for running in an activity (emulator)
	public LockScreenImpl(Activity context) {
		super(context);
	}

	@Override
	public void onCreate(SurfaceHolder holder) {
		
		setContent(R.layout.lock_screen);
		setShowLiveWallpaper(true);

	    mCalendarView = (ViewGroup) findViewById(R.id.gridview);
	    mTitle  = (TextView) findViewById(R.id.title);

	    mMonth = Calendar.getInstance();
	    refreshCalendar();
	    
	    final View previous = findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(final View v) {
				mMonth.add(Calendar.MONTH, -1);
				refreshCalendar();
			}
		});
	    
	    final View next = findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(final View v) {
				mMonth.add(Calendar.MONTH, 1);
				refreshCalendar();
				
			}
		});
	}
	
	public void refreshCalendar() {
		mTitle.setText(android.text.format.DateFormat.format("MMMM yyyy", mMonth) + " Exercise");
		
        mData = new MonthExerciseData(getContext(), mMonth);

        final MonthViewUpdater viewUpdater = new MonthViewUpdater(mCalendarView, mMonth);
	    viewUpdater.setOnDayClickedListener(new OnDayClickedListener() {
	    	public void onDayClicked(int year, int month, int tappedDay) {	        	
	        	SetMinutesDialog dialog = new SetMinutesDialog(getContext());
	        	dialog.setDay(viewUpdater, tappedDay, mData);
	        	dialog.show();		        
		    }
		});
		viewUpdater.update(mData);
	}
	
	@Override
	public void onStart() {
		refreshCalendar();
	}

	@Override
	public void onResume() {
	}
	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onStop() {
	}
	
	@Override
	public void onDestroy() {
	}
	
}
