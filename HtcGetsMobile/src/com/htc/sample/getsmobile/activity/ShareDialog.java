package com.htc.sample.getsmobile.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.htc.sample.getsmobile.R;
import com.htc.sample.getsmobile.data.MonthExerciseData;
import com.htc.sample.getsmobile.data.Total;

public class ShareDialog extends Dialog {
    		
    private static final String SHARE_TYPE = "image/jpeg";

	private static final String FILE_URI_PREFIX = "file://";
	
	private static final String NAME_KEY = ShareDialog.class.getName() + ".NAME_KEY";
	
	private static final String TEAM_KEY = ShareDialog.class.getName() + ".TEAM_KEY";
	
	private EditText name;
	
	private EditText team;
	
	private TextView contestTotal;
	
	private TextView monthTotal;
	
	private Button done;	
	
	private SharedPreferences mPrefs;

	public ShareDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public ShareDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public ShareDialog(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(null);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		setContentView(R.layout.share_dialog);
		
		name = (EditText) findViewById(R.id.share_dialog_name);
		name.setText(mPrefs.getString(NAME_KEY, ""));
		
		team = (EditText) findViewById(R.id.share_dialog_team);
		team.setText(mPrefs.getString(TEAM_KEY, ""));
		
		contestTotal = (TextView) findViewById(R.id.share_dialog_contest_total);
		monthTotal = (TextView) findViewById(R.id.share_dialog_month_total);
		done = (Button) findViewById(R.id.share_dialog_share);
		
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Editor edit = mPrefs.edit();
				edit.putString(NAME_KEY, name.getText().toString());
				edit.putString(TEAM_KEY, team.getText().toString());
				edit.commit();
			}
		});
	
	}

	public void setData(final MonthExerciseData data, final String mSavedImagePath) {
		
		final Total contestTotalValue = data.getContestTotal();
		final Total monthTotalValue = data.getMonthTotal();

		contestTotal.setText("" + contestTotalValue.amount);
		monthTotal.setText("" + monthTotalValue.amount);
		
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final Intent share = new Intent(Intent.ACTION_SEND);
				share.setType(SHARE_TYPE);
				share.putExtra(Intent.EXTRA_STREAM,Uri.parse(FILE_URI_PREFIX + mSavedImagePath));
				share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
				
				String story = "Name: " + name.getText() + "\n";
				story += "Team: " + team.getText() + "\n\n";
				
				story += "Individual Total (current month): " + monthTotal.getText() + "\n";
				story += "Minutes Each Day of Month (current month): " + monthTotalValue.description + "\n\n";
				
				story += "Individual Total (6/11/12 - 7/6): " + contestTotal.getText() + "\n";
				story += "Minutes Each Day of Contest (6/11/12 - 7/6): " + contestTotalValue.description + "\n\n";

				//share.putExtra(Intent.EXTRA_EMAIL, new String[] {"Amber_West@htc.com"});
				share.putExtra(Intent.EXTRA_TEXT, story); 
				share.putExtra("sms_body", story);   
				share.putExtra(Intent.EXTRA_TITLE, "My minutes for HTC Gets Mobile");
				share.putExtra(Intent.EXTRA_SUBJECT, "My minutes for HTC Gets Mobile");
				getContext().startActivity(Intent.createChooser(share, "Choose a way to share:"));
				
				dismiss();
			}
		});
		
	}
	
}
