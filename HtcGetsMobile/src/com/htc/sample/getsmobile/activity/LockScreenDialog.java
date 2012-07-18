package com.htc.sample.getsmobile.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.htc.sample.getsmobile.R;

public class LockScreenDialog extends Dialog {
	
	public LockScreenDialog(Context context) {
		super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(null);
		setContentView(R.layout.lock_screen_dialog);

		final View openSettings = findViewById(R.id.lock_screen_dialog_open_settings_button);
		openSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				showSettings();
				dismiss();
			}
		});
		
		final View close = findViewById(R.id.lock_screen_dialog_close_button);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				dismiss();
			}
		});
				
		TextView instructions = (TextView) findViewById(R.id.lock_screen_dialog_instructions);		
		String instructionsHtml = "See your exercise on your phone\'s lock screen:<br />" + 
				"1) open <b><font color=\"#73b20e\">Settings</font></b> app<br />" + 
				"2) choose <b><font color=\"#73b20e\">Personalize</font></b><br />" + 
				"3) choose <b><font color=\"#73b20e\">Lock screen style</font></b><br />"; 
		instructions.setText(Html.fromHtml(instructionsHtml));
	}
	
	private void showSettings() {
		Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );
		getContext().startActivity(intent);		
	}

}
