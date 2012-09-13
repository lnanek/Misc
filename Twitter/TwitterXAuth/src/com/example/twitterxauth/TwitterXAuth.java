package com.example.twitterxauth;

import java.sql.Date;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Using Twitter4J Java library and Signpost OAuth library to access Twitter via xAuth.
 */
public class TwitterXAuth extends Activity {
	
	private static final String LOG_TAG = "TwitterXAuth";
	private static final int UPDATED_STATUS_MESSAGE_ID = 0;
	private static final int ON_EXCEPTION_MESSAGE_ID = 1;

	//The consumer key and secret from your Twitter application's setup web page on Twitter.
	private static final String CONSUMER_KEY = 		"RF4G8FucWm5QtC4aoSKVOw";
	private static final String CONSUMER_SECRET = 	"K46z9T5IKCGfeVWPplUS5MjbbdP81iKKO9Fr9iM";
	
	private TextView tweetResultTextView;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button authenticateAndTweetButton;

	private String tweet;
	
	// Runs on the main thread that can access the Android UI. 
	private Handler mainThreadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case UPDATED_STATUS_MESSAGE_ID:

				// Show message
				tweetResultTextView.setText(tweet);
				Toast.makeText(TwitterXAuth.this, tweet, Toast.LENGTH_LONG).show();
				authenticateAndTweetButton.setVisibility(Button.GONE);
				
				break;
			case ON_EXCEPTION_MESSAGE_ID:
				
				Exception e = (Exception) msg.obj;
				Log.e(LOG_TAG, "Exception authorizing and posting to Twitter: ", e);
				Toast.makeText(TwitterXAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
				
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the UI and get references to the components.
		setContentView(R.layout.main);
		tweetResultTextView = (TextView)findViewById(R.id.tweetResult);
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		authenticateAndTweetButton = (Button)findViewById(R.id.authenticateAndTweetButton);
		authenticateAndTweetButton.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				askXAuth();
			}
		});
	}

	/**
	 * Exchange username and password for an access token via xAuth,
	 * then post a test message to Twitter.
	 */
	private void askXAuth() {
		
		// Create a tweet
		Date date = new Date(System.currentTimeMillis());
		tweet = "TwitterXAuth works" + date.toLocaleString();
		
		// Read username and password
		final String username = usernameEditText.getText().toString();
		Log.e(LOG_TAG, "Using username: " + username);
		final String password = passwordEditText.getText().toString();
		Log.e(LOG_TAG, "Using password: " + password);
		
		// Authorize and send. 
		// This performs network access, so should be done on a different thread than the UI.
		new Thread() {
			@Override
			public void run() {
				try {							
										
					// Initialize Twitter4J
					ConfigurationBuilder confbuilder  = new ConfigurationBuilder();
					confbuilder.setOAuthConsumerKey(CONSUMER_KEY);
					confbuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
					Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();
					
					// Get the access token.
					// TODO: you might want to store token and token_secret in you app settings!
					AccessToken a = twitter.getOAuthAccessToken(username, password);
					
					// Post the tweet
					twitter.updateStatus(tweet);
					
					// Let the main/UI thread know.
					mainThreadHandler.sendEmptyMessage(UPDATED_STATUS_MESSAGE_ID);
					
				} catch (Exception e) {
					Message message = mainThreadHandler.obtainMessage(ON_EXCEPTION_MESSAGE_ID, e);
					mainThreadHandler.sendMessage(message);
				}
			}
		}.start();
	}
}

