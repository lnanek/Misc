/**
 * 
 */
package com.htc.sample.getsmobile.widget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * @author Lance_Nanek
 *
 */
public class MonthWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		WidgetUpdater.update(context);
	}

}
