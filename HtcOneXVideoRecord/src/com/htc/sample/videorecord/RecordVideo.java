package com.htc.sample.videorecord;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class RecordVideo extends Activity implements SurfaceHolder.Callback {

	private SurfaceHolder mSurfaceHolder;

	private SurfaceView mSurfaceView;

	public MediaRecorder mRecorder = new MediaRecorder();

	File mSavedVideoFile;

	private Camera mCamera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("RecordVideo", "Preview starting");

		mCamera = Camera.open();
/*
		Parameters params = mCamera.getParameters();

		List<Camera.Size> supportedSizes = params.getSupportedPreviewSizes();
		for ( Camera.Size size : supportedSizes ) {
			Log.i("Record Video", "Supported size: " + size.width + " x " + size.height);				
		}
*/
		mSurfaceView = new SurfaceView(this);
        setContentView(mSurfaceView);
		
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "StartRecording");
		menu.add(0, 1, 0, "StopRecording");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			try {
				startRecording();
			} catch (Exception e) {
				String message = e.getMessage();
				Log.i(null, "Problem Start" + message);
				mRecorder.release();
			}
			break;

		case 1: // GoToAllNotes
			stopRecording();
			releaseMediaRecorder();
			releaseCamera();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	protected void startRecording() throws IOException {
		mRecorder = new MediaRecorder(); // Works well
		mCamera.unlock();

		mRecorder.setCamera(mCamera);

		mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

		//mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		
		//mrec.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		//mrec.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setVideoSize(640,480);
		mRecorder.setVideoFrameRate(30);
		//mrec.setVideoEncodingBitRate(MAXIMAL_PERMITTED_VIDEO_ENCODING_BITRATE);
		mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
		
		mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mRecorder.setOutputFile("/sdcard/zzzz.mp4");

		mRecorder.prepare();
		mRecorder.start();
	}

	protected void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mCamera.release();
	}

	private void releaseMediaRecorder() {
		if (mRecorder != null) {
			mRecorder.reset(); // clear recorder configuration
			mRecorder.release(); // release the recorder object
			mRecorder = null;
			//mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera != null) {
			Parameters params = mCamera.getParameters();
			
			
			mCamera.setParameters(params);
		} else {
			Toast.makeText(getApplicationContext(), "Camera not available!",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
	}
}