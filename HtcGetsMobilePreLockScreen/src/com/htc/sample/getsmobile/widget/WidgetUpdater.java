package com.htc.sample.getsmobile.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.RemoteViews;

import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.data.MonthExerciseData;
import com.htc.sample.getsmobile.data.Total;

public class WidgetUpdater {

	public static void update(Context context) {

		ComponentName thisWidget = new ComponentName(context.getApplicationContext(), ContestWidgetProvider.class);
		MonthExerciseData data = new MonthExerciseData(context, Calendar.getInstance());
		Total total = data.getContestTotal();
		String title = "Contest Exercise:";
		
		updateWidgets(context, thisWidget, total, title);

		thisWidget = new ComponentName(context.getApplicationContext(), MonthWidgetProvider.class);
		total = data.getMonthTotal();
		title = "Month Exercise:";
		
		updateWidgets(context, thisWidget, total, title);
	}

	public static void updateWidgets(Context context, ComponentName thisWidget,
			Total contestTotal, String title) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds2) {

			RemoteViews remoteViews = new RemoteViews(context
					.getApplicationContext().getPackageName(),
					R.layout.widget_layout);

			remoteViews.setTextViewText(R.id.date, title);
						
			setDoodleTextImage(remoteViews, context, contestTotal.amount, R.id.date_minutes);

			PackageManager pm = context.getPackageManager();
			Intent clickIntent = pm.getLaunchIntentForPackage(context.getPackageName());
		      
		      clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		      PendingIntent clickPI = PendingIntent.getActivity(context, 0,
		                                            clickIntent,
		                                            PendingIntent.FLAG_UPDATE_CURRENT);
		      
		      remoteViews.setOnClickPendingIntent (R.id.widget_content, clickPI);
		      
		      
			
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
	
    public static RemoteViews setDoodleTextImage(RemoteViews views, Context context, int minutes, int id)
{
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(),"HTCscript.otf");
        paint.setAntiAlias(true);
        //paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        if ( minutes > 0 ) {
        	paint.setColor(Color.parseColor("#73b20e"));
        } else {
            paint.setColor(Color.RED);        	
        }
        paint.setTextSize(72);

        String text = "" + minutes;
        Rect textSize = new Rect();
        paint.getTextBounds(text, 0, text.length(), textSize);
        
        Bitmap myBitmap = Bitmap.createBitmap(textSize.width() + 20, textSize.height() + 10, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        myCanvas.drawText(text, 10, textSize.height(), paint);

        views.setImageViewBitmap(id, myBitmap);
        
        return views;
}
	
}
