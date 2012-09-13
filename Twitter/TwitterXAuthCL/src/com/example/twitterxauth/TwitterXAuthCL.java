package com.example.twitterxauth;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Posts to Twitter from the command line using xAuth.
 */
public class TwitterXAuthCL {
	
	public static void main(String[] args) {
		
		if ( args.length != 5 ) {
			System.out.println(
					"Usage: java -jar TwitterXAuthCL.jar <consumer key> <consumer secret> <username> <password> <tweet text> ");
			System.exit(1);
		}
		
		String consumerKey = args[0];
		String consumerSecret = args[1];
		String username = args[2];
		String password = args[3];
		String tweetText = args[4];
		
		try {									
			// Initialize Twitter4J
			System.setProperty("twitter4j.debug", "true");
			ConfigurationBuilder confbuilder  = new ConfigurationBuilder();
			confbuilder.setOAuthConsumerKey(consumerKey);
			confbuilder.setOAuthConsumerSecret(consumerSecret);
			Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance(
					new BasicAuthorization(username, password));
			
			// Get the access token.
			AccessToken a = twitter.getOAuthAccessToken();
			twitter.setOAuthAccessToken(a);
			
			// Post the tweet
			twitter.updateStatus(tweetText);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}
