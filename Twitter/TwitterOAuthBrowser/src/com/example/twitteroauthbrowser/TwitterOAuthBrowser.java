package com.example.twitteroauthbrowser;

import java.sql.Date;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Using Twitter4J Java library and Signpost OAuth library to access Twitter 
 * via OAuth and the Android Browser app.
 */
public class TwitterOAuthBrowser extends Activity {
	
	private static final String LOG_TAG = "TwitterOAuthBrowser";
	private static final int UPDATED_STATUS_MESSAGE_ID = 0;
	private static final int ON_EXCEPTION_MESSAGE_ID = 1;
	private static final int RETRIEVED_REQUEST_TOKEN_MESSAGE_ID = 2;

	//The consumer key and secret from your Twitter application's setup web page on Twitter.
	private static final String CONSUMER_KEY = 		"v7xwr2xFzDEsgDvg962Paw";
	private static final String CONSUMER_SECRET = 	"YlbknWgtA9t5u4q6q18IwHTQ2vtOlHesVBO9u5EtEy4";
	private static final String CALLBACK_URL = 		"myapp://twitteroauth";
	
	private TextView tweetResultTextView;
	private Button authenticateAndTweetButton;

	private String tweet;
	private OAuthProvider provider;
	private OAuthConsumer consumer;
	
	// Runs on the main thread that can access the Android UI. 
	private Handler mainThreadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case RETRIEVED_REQUEST_TOKEN_MESSAGE_ID:
				
				String authUrl = (String) msg.obj;
				Toast.makeText(TwitterOAuthBrowser.this, "Authorize this app!", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				
				break;
			case UPDATED_STATUS_MESSAGE_ID:

				// Show message
				tweetResultTextView.setText(tweet);
				Toast.makeText(TwitterOAuthBrowser.this, tweet, Toast.LENGTH_LONG).show();
				authenticateAndTweetButton.setVisibility(Button.GONE);
				
				break;
			case ON_EXCEPTION_MESSAGE_ID:
				
				Exception e = (Exception) msg.obj;
				Log.e(LOG_TAG, "Exception authorizing and posting to Twitter: ", e);
				Toast.makeText(TwitterOAuthBrowser.this, e.getMessage(), Toast.LENGTH_LONG).show();
				
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup the UI and get references to the components.
		setContentView(R.layout.main);
		tweetResultTextView = (TextView) findViewById(R.id.tweetResult);
		authenticateAndTweetButton = (Button) findViewById(R.id.authenticateAndTweetButton);
		authenticateAndTweetButton.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				askOAuth();
			}
		});
	}
	
	/**
	 * Get the Twitter URL to send the user to for authentication.
	 */
	private void askOAuth() {
		// Use Apache HttpClient for HTTP messaging
		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider("https://api.twitter.com/oauth/request_token",
												"https://api.twitter.com/oauth/access_token",
												"https://api.twitter.com/oauth/authorize");					
		new Thread() {
			@Override
			public void run() {
				try {
					String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
					Message message = mainThreadHandler.obtainMessage(RETRIEVED_REQUEST_TOKEN_MESSAGE_ID, authUrl);
					mainThreadHandler.sendMessage(message);
				} catch (Exception e) {
					Message message = mainThreadHandler.obtainMessage(ON_EXCEPTION_MESSAGE_ID, e);
					mainThreadHandler.sendMessage(message);
				}
			}
		}.start();
	}
			
	/**
	 * Get the verifier from the callback URL. 
	 * Retrieve token and token_secret. 
	 * Feed them to twitter4j along with consumer key and secret
	 */
	@Override
	protected void onNewIntent(final Intent intent) {

		super.onNewIntent(intent);
		

		Log.e(LOG_TAG, "onNewIntent");
		
		// Create a tweet
		Date date = new Date(System.currentTimeMillis());
		tweet = "TwitterOAuthBrowser works " + date.toLocaleString();

		// Authorize and send. 
		// This performs network access, so should be done on a different thread than the UI.
		new Thread() {
			@Override
			public void run() {

				Uri uri = intent.getData();
				if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
		
					String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
		
					try {
						// Populate token and token_secret in consumer
						provider.retrieveAccessToken(consumer, verifier);
		
						// Get the access token.
						// TODO: you might want to store token and token_secret in you app settings!
						AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
						
						// Initialize Twitter4J
						ConfigurationBuilder confbuilder  = new ConfigurationBuilder();
						confbuilder.setOAuthAccessToken(a.getToken());
						confbuilder.setOAuthAccessTokenSecret(a.getTokenSecret());				
						confbuilder.setOAuthConsumerKey(CONSUMER_KEY);
						confbuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
						Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();
		
						// Post the tweet
						twitter.updateStatus(tweet);
						
						// Let the main/UI thread know.
						mainThreadHandler.sendEmptyMessage(UPDATED_STATUS_MESSAGE_ID);
						
					} catch (Exception e) {
						Message message = mainThreadHandler.obtainMessage(ON_EXCEPTION_MESSAGE_ID, e);
						mainThreadHandler.sendMessage(message);
					}
				}
			}
		}.start();
	}
}

