/*
* Copyright 2011 Lauri Nevala.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.htc.sample.getsmobile.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.htc.sample.getsmobile.Prefs;
import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.data.MonthExerciseData;
import com.htc.sample.getsmobile.lockscreen.LockScreenHelper;
import com.htc.sample.getsmobile.view.MonthViewUpdater;
import com.htc.sample.getsmobile.view.MonthViewUpdater.OnDayClickedListener;


public class CalendarViewActivity extends Activity {
    
    private static final String LOG_TAG = CalendarViewActivity.class.getSimpleName();
	
	private static final boolean PRETEND_NO_SD = false;
	
	private static final String IMAGE_OUTPUT_FILE_NAME = "HTC_Gets_Mobile.jpg";
	
	private String mSavedImagePath;
	
    private Calendar mMonth;
		
    private View mContent;

    private MonthExerciseData mData;
	
    private ViewGroup mCalendarView;
	
    private TextView mTitle;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

        final boolean enabledLockScreen = LockScreenHelper.enable(this);
        if ( enabledLockScreen ) {
        	final Prefs prefs = new Prefs(this);
        	if ( !prefs.getShowedLockScreenDialog() ) {
        		new LockScreenDialog(this).show();
        		prefs.setShowedLockScreenDialog(true);
        	}
        }
        
        /*
	    if ( 1 == 1) {
	    throw new RuntimeException("Test exception reporting");
	    }
	    */

        requestWindowFeature(Window.FEATURE_NO_TITLE);        
	    setContentView(R.layout.calendar);
	    mContent = findViewById(R.id.content);
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
		
	    final View share = findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final ShareDialog dialog = new ShareDialog(CalendarViewActivity.this);
								
				saveImage();
				
				dialog.setData(mData, mSavedImagePath);				
				dialog.show();
			}
		});
		
	}
	
	public void refreshCalendar()
	{
		mTitle.setText(android.text.format.DateFormat.format("MMMM yyyy", mMonth));
		
        mData = new MonthExerciseData(this, mMonth);

        final MonthViewUpdater viewUpdater = new MonthViewUpdater(mCalendarView, mMonth);
	    viewUpdater.setOnDayClickedListener(new OnDayClickedListener() {
	    	public void onDayClicked(int year, int month, int tappedDay) {	        	
	        	SetMinutesDialog dialog = new SetMinutesDialog(CalendarViewActivity.this);
	        	dialog.setDay(viewUpdater, tappedDay, mData);
	        	dialog.show();		        
		    }
		});
		viewUpdater.update(mData);
	}

    public void saveImage() {
    	OutputStream outputStream = null;
    	Bitmap bitmap = null;
    	
    	try {
	        final File targetFile;
	        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
	        boolean useSdCard = !PRETEND_NO_SD && externalStorageDirectory.exists();
	        if ( useSdCard ) {
		        targetFile = new File(externalStorageDirectory, IMAGE_OUTPUT_FILE_NAME);
		        if ( targetFile.exists() ) {
		        	targetFile.delete();
		        }	        
		        outputStream = new FileOutputStream(targetFile);
		       
	        } else {
	        	targetFile = getFileStreamPath(IMAGE_OUTPUT_FILE_NAME);
		        outputStream = openFileOutput(IMAGE_OUTPUT_FILE_NAME, MODE_WORLD_READABLE);
	        }

	        bitmap = drawToBitmap();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
	        bitmap.recycle();
	        bitmap = null;
	        
	        outputStream.flush();
	        outputStream.close();
		        
        	//MediaStore.Images.Media.insertImage(getContentResolver(),
        	//		targetFile.getAbsolutePath(), targetFile.getName() ,targetFile.getName());
	        
	        mSavedImagePath = targetFile.getAbsolutePath();
	        
    	} catch (final Exception e) {
        	Log.e(LOG_TAG, "Error saving image.", e);
        } finally {
            if ( null != bitmap ) {
               	bitmap.recycle();
            }
        	if ( null != outputStream ) {
                try {
                	outputStream.close();
                } catch (IOException e) {
                	// Do nothing.
                }
            }
        }
    }

    /**
     * Draws view to a bitmap image.
     */
    public Bitmap drawToBitmap() {
    	final Bitmap bitmap = Bitmap.createBitmap(
    			mContent.getWidth(), 
    			mContent.getHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        mContent.draw(canvas);
        return bitmap;
    }
    
}
