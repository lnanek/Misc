package com.htc.sample.mediaaudio;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class TestMediaRecorderAudioRecordActivity extends Activity {

	private MediaRecorder recorder;
	
	private TextView status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		status = (TextView) findViewById(R.id.status);

		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/test.3gpp");
		try {
			recorder.prepare();
			recorder.start();
			status.setText("Recording.");
		} catch (IllegalStateException e) {
			recorder = null;
			status.setText("IllegalStateException.");
		} catch (IOException e) {
			recorder = null;
			status.setText("IOException.");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if ( null != recorder ) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}
		status.setText("Finished.");
	}

}