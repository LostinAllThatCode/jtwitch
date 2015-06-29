package org.gdesign.twitch.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.resource.Root;

import com.google.gson.Gson;


public class TwitchAPI {
	
	private static final String TwitchAPIRoot 	= "https://api.twitch.tv/kraken";	
	
	public static enum Permission {
		user_read,						// Read access to non-public user information, such as email address.
		user_blocks_edit,				// Ability to ignore or unignore on behalf of a user.
		user_blocks_read,				// Read access to a user's list of ignored users.
		user_follows_edit,				// Access to manage a user's followed channels.
		channel_read,					// Read access to non-public channel information, including email address and stream key
		channel_editor,					// Write access to channel metadata (game, status, etc).
		channel_commercial,				// Access to trigger commercials on channel.
		channel_stream,					// Ability to reset a channel's stream key.
		channel_subscriptions,			// Read access to all subscribers to your channel.
		user_subscriptions,				// Read access to subscriptions of a user. 
		channel_check_subscription,		// Read access to check if a user is subscribed to your channel.
		chat_login						// Ability to log into chat and send messages.
	}
	
	private String oAuthToken 			= System.getenv("OAUTH");
	
	public TwitchAPI() {
		setAuthToken(oAuthToken);
	}
	
	public TwitchAPI(String token) {
		setAuthToken(token);
	}
	
	public TwitchAPI setAuthToken(String token){
		this.oAuthToken = token;
		return this;
	}
	
	public String request(String urlString,String token){
		try {
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			con.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
			con.setRequestProperty("Authorization", "OAuth "+token);
			con.connect();
			return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		} catch (IOException e) {
			LogManager.getLogger().error(e);
			return null;
		}
	}
	
	public boolean hasPermisson(Permission... p){
		List<String> permissionList = getPermissions();
		for (String s : permissionList){
			for (Permission pe : p){
				if (!Permission.valueOf(s).equals(pe)) return false;
			}
		}
		return true;
	}
	
	public boolean authorized(){
		return new Gson().fromJson(request(TwitchAPIRoot, this.oAuthToken), Root.class).token.valid;
	}
	
	private List<String> getPermissions(){
		return new Gson().fromJson(request(TwitchAPIRoot, this.oAuthToken), Root.class).token.authorization.scopes;
	}

}
