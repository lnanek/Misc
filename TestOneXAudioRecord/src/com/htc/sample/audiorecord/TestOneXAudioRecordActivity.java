package com.htc.sample.audiorecord;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestOneXAudioRecordActivity extends Activity {

	private static final int[] AUDIO_FORMATS = new int[] { AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT };

	private static int[] SAMPLE_RATES = new int[] { 44100, 22050, 16000, 11025, 8000 };
    
    private static final int[] CHANNELS = new int[] { 
    	
    	AudioFormat.CHANNEL_IN_MONO, 
    	AudioFormat.CHANNEL_IN_STEREO,
/*
    	AudioFormat.CHANNEL_CONFIGURATION_DEFAULT,
    	AudioFormat.CHANNEL_CONFIGURATION_MONO,
    	AudioFormat.CHANNEL_CONFIGURATION_STEREO,

    	AudioFormat.CHANNEL_IN_BACK	,
    	AudioFormat.CHANNEL_IN_BACK_PROCESSED	,
    	AudioFormat.CHANNEL_IN_DEFAULT	,
    	AudioFormat.CHANNEL_IN_FRONT	,
    	AudioFormat.CHANNEL_IN_FRONT_PROCESSED	,
    	AudioFormat.CHANNEL_IN_LEFT	,
    	AudioFormat.CHANNEL_IN_LEFT_PROCESSED	,
    	AudioFormat.CHANNEL_IN_MONO	,
    	AudioFormat.CHANNEL_IN_PRESSURE	,
    	AudioFormat.CHANNEL_IN_RIGHT	,
    	AudioFormat.CHANNEL_IN_RIGHT_PROCESSED	,
    	AudioFormat.CHANNEL_IN_STEREO	,
    	AudioFormat.CHANNEL_IN_VOICE_DNLINK	,
    	AudioFormat.CHANNEL_IN_VOICE_UPLINK	,
    	AudioFormat.CHANNEL_IN_X_AXIS	,
    	AudioFormat.CHANNEL_IN_Y_AXIS	,
    	AudioFormat.CHANNEL_IN_Z_AXIS,
*/    	
    };
	
	private static final String LOG_TAG = "***TestOneXAudioRecordActivity***";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView text = (TextView) findViewById(R.id.text_view); 
        text.setText("Trying to find valid AudioRecord...");
        
        AudioRecord recorder = null;
        try {
        	recorder = findAudioRecord();

        	if ( null != recorder ) {
        		Log.i("***TestOneXAudioRecordActivity***", "Initialized an AudioRecord = " + recorder);
                text.setText("Initialized an AudioRecord.");
        	} else {
                Log.i("***TestOneXAudioRecordActivity***", "Failed to initilize any AudioRecord");        		
                text.setText("Failed to initilize any AudioRecord.");
        	}

        } finally {
        	if ( null != recorder ) {
        		recorder.release();
        	}
        }
    }
    
    public AudioRecord findAudioRecord() {
        for (final int rate : SAMPLE_RATES) {
            for (final int audioFormat : AUDIO_FORMATS) {
                for (final int channelConfig : CHANNELS) {
                    try {
                    	
                        Log.d(LOG_TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat 
                        		+ ", channel: " + channelConfig);
                        
                        final int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR &&
                        		bufferSize != AudioRecord.ERROR_BAD_VALUE &&
                        			bufferSize != AudioRecord.ERROR_INVALID_OPERATION) {

                        	final AudioRecord recorder = new AudioRecord(AudioSource.MIC, rate, channelConfig, audioFormat, bufferSize);

                        	final int state = recorder.getState();
                            Log.d(LOG_TAG, "AudioRecord.getState = " + getAudioRecordState(state));

                            if (state == AudioRecord.STATE_INITIALIZED) {
                                return recorder;
                            } else {
                            	recorder.release();
                            }
                        } else {
                            Log.e(LOG_TAG, "AudioRecord.getMinBufferSize returned error, keep trying.");
                        }
                    } catch (Exception e) {
                        Log.e(LOG_TAG, rate + "Exception, keep trying: ", e);
                    }
                }
            }
        }
        return null;
    }

    public String getAudioRecordState(final int state) {
    	if ( state == AudioRecord.STATE_INITIALIZED ) {
    		return "AudioRecord.STATE_INITIALIZED";
    	}
    	
    	if ( state == AudioRecord.STATE_UNINITIALIZED ) {
    		return "AudioRecord.STATE_UNINITIALIZED";
    	}
    	
    	return "UNKNOWN (" + state + ")";
    }
}