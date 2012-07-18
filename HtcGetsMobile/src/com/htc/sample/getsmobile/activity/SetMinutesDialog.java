package com.htc.sample.getsmobile.activity;

import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.R.id;
import com.htc.sample.getsmobile.R.layout;
import com.htc.sample.getsmobile.data.MonthExerciseData;
import com.htc.sample.getsmobile.view.MonthViewUpdater;
import com.htc.sample.getsmobile.widget.WidgetUpdater;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SetMinutesDialog extends Dialog {
	
	private EditText minutes;
	
	private Button done;

	public SetMinutesDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public SetMinutesDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public SetMinutesDialog(Context context) {
		super(context);
		init();
	}
	
	private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(null);
		setContentView(R.layout.set_minutes_dialog);
		minutes = (EditText) findViewById(R.id.set_minutes_dialog_minutes);
		done = (Button) findViewById(R.id.set_minutes_dialog_done);
	}
	
	public void setDay(final MonthViewUpdater viewUpdater, final int tappedDay, final MonthExerciseData data) {
		
		int minutesValue = data.getMinutes(tappedDay);
		if ( minutesValue > 0 ) {
			minutes.setText("" + minutesValue);
		}

		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String minutesText = minutes.getText() + "";
				if ( minutesText.length() > 0 ) {				
					int minutesValue = Integer.parseInt("" + minutes.getText());
					setMinutes(viewUpdater, tappedDay, data, minutesValue);
				} else {

					setMinutes(viewUpdater, tappedDay, data, 0);
				}
				
				dismiss();
			}
		});
	}
	

	private void setMinutes(final MonthViewUpdater viewUpdater, final int tappedDay, MonthExerciseData data, int minutesValue) {		
		data.setMinutes(tappedDay, minutesValue);
		WidgetUpdater.update(getContext());
		viewUpdater.update(data);		
	}

}
