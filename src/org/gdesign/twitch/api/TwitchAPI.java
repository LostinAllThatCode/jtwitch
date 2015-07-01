package org.gdesign.twitch.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Root;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TwitchAPI {
	private static final String TwitchAPIRoot 	= "https://api.twitch.tv/kraken";
	
	/**
	 * Permissions defined in justin.tv/twitch.tv api.
	 * 	user_read,						// Read access to non-public user information, such as email address.
	 *	user_read,						// Read access to non-public user information, such as email address.
	 *	user_blocks_edit,				// Ability to ignore or unignore on behalf of a user.
	 *	user_blocks_read,				// Read access to a user's list of ignored users.
	 *	user_follows_edit,				// Access to manage a user's followed channels.
	 *	channel_read,					// Read access to non-public channel information, including email address and stream key
	 *	channel_editor,					// Write access to channel metadata (game, status, etc).
	 *	channel_commercial,				// Access to trigger commercials on channel.
	 *	channel_stream,					// Ability to reset a channel's stream key.
	 *	channel_subscriptions,			// Read access to all subscribers to your channel.
	 *	user_subscriptions,				// Read access to subscriptions of a user. 
	 *	channel_check_subscription,		// Read access to check if a user is subscribed to your channel.
	 *	chat_login						// Ability to log into chat and send messages.
	 * 
	 * @author agaida
	 */
	public static enum Permission {
		user_read,
		user_blocks_edit,
		user_blocks_read,
		user_follows_edit,
		channel_read,
		channel_editor,
		channel_commercial,
		channel_stream,
		channel_subscriptions,
		user_subscriptions,
		channel_check_subscription,
		chat_login
	}

	private String oAuthToken 			= System.getenv("OAUTH");
	
	/**
	 * Creates a new TwitchAPI instance.
	 */
	public TwitchAPI() {
		setAuthToken(oAuthToken);
	}
	
	/**
	 * Creates a new TwitchAPI instance.
	 *
	 * @param token		oAuth2 token which includes permission for the useraccount.
	 */
	public TwitchAPI(String token) {
		setAuthToken(token);
	}
	
	/**
	 * Sets oAuth2 token for this instance
	 * @param token		oAuth2 token which includes permission for the useraccount.
	 * @return 			TwitchAPI instance
	 */
	public TwitchAPI setAuthToken(String token){
		this.oAuthToken = token;
		return this;
	}
	
	
	/**
	 * Returns a TwitchAPIResource object which includes its defined datasets.
	 *  
	 * @param url		URL to twitch api resource. Example: https://api.twitch.tv/kraken/users/its1z0 for User.class
	 * @param clazz		Class which specifies how the request json object from @param url should be wrapped
	 * @return 			Returns TwitchAPIResource as specified @param clazz
	 * @throws IOException 
	 * @throws TwitchAPIUnauthorizedAccessException 
	 * @throws JsonSyntaxException 
	 */
	public <T extends TwitchAPIResource> T getResource(String url, Class<T> clazz) throws JsonSyntaxException, TwitchAPIUnauthorizedAccessException, IOException{
		Gson gson = new Gson();
		return gson.fromJson(request(url, this.oAuthToken), clazz);
	}
	
	/**
	 * Sends an api request to given URL.
	 * @param urlString URL to twitch api resource.
	 * @param token		oAuth2 token which includes permission for the useraccount.
	 * @return			Returns string in json format.
	 * @throws TwitchAPIUnauthorizedAccessException 
	 * @throws IOException 
	 */
	private String request(String urlString,String token) throws TwitchAPIUnauthorizedAccessException, IOException{
		URLConnection connection = null;
		try {
			connection = new URL(urlString).openConnection();
			
			connection.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
			connection.setRequestProperty("Authorization", "OAuth "+token);
			connection.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			return reader.readLine();
		
		} catch (IOException e) {
			if (((HttpURLConnection)connection).getResponseCode() == 401)
				throw new TwitchAPIUnauthorizedAccessException("Unauthorized access to "+urlString+". Wrong token or no permission.\n"+e);
			else
				throw new IOException("Input/Output error for url: "+urlString+"\n"+e);
		}
	}
	
	/**
	 * Checks if specified oAuth2 token has given permissions.
	 * @param p 		Array of permissions
	 * @return			self-explanatory
	 * @throws IOException 
	 * @throws TwitchAPIUnauthorizedAccessException 
	 * @throws JsonSyntaxException 
	 */
	public boolean hasPermisson(Permission... p) throws JsonSyntaxException, TwitchAPIUnauthorizedAccessException, IOException{
		List<String> permissionList = getPermissions();
		for (String s : permissionList){
			for (Permission pe : p){
				if (!Permission.valueOf(s).equals(pe)) return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if specified oAuth2 token is valid.
	 * @return 			Validity of token
	 * @throws IOException 
	 * @throws TwitchAPIUnauthorizedAccessException 
	 * @throws JsonSyntaxException 
	 */
	public boolean authorized() throws JsonSyntaxException, TwitchAPIUnauthorizedAccessException, IOException{
		Root root = getResource(TwitchAPIRoot, Root.class);
		if (root != null) return root.token.valid; else return false;
	}
	
	/**
	 * Returns a list of permissions for valid oAuth2 token.
	 * @return null if no permissions exist
	 * @throws IOException 
	 * @throws TwitchAPIUnauthorizedAccessException 
	 * @throws JsonSyntaxException 
	 */
	private List<String> getPermissions() throws JsonSyntaxException, TwitchAPIUnauthorizedAccessException, IOException{
		return getResource(TwitchAPIRoot, Root.class).token.authorization.scopes;
	}

}
