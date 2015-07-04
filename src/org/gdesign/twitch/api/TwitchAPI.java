package org.gdesign.twitch.api;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.builder.DefaultResourceBuilder;
import org.gdesign.twitch.api.resource.root.Root;

import com.google.gson.JsonSyntaxException;

public class TwitchAPI {
	private static final String API_ROOT_URL 	= "https://api.twitch.tv/kraken";
	private static final String CLIENT_ID		= "1l1xxqg16ehfn68qim2lpw88qqg072d";
	private static final String REDIRECT_URI	= "http://jtwitchplayer.g-design.org";

	private static String oAuthToken = "";
	
	/**
	 * Permissions from justin.tv/twitch.tv api. 
	 * user_read, // Read access to non-public user information, such as email address.
	 * user_read, // Read access to non-public user information, such as email address.
	 * user_blocks_edit, // Ability to ignore or unignore on behalf of a user.
	 * user_blocks_read, // Read access to a user's list of ignored users.
	 * user_follows_edit, // Access to manage a user's followed channels.
	 * channel_read, // Read access to non-public channel information, including
	 * email address and stream key
	 * channel_editor, // Write access to channel metadata (game, status, etc).
	 * channel_commercial, // Access to trigger commercials on channel.
	 * channel_stream, // Ability to reset a channel's stream key.
	 * channel_subscriptions, // Read access to all subscribers to your channel.
	 * user_subscriptions, // Read access to subscriptions of a user.
	 * channel_check_subscription, // Read access to check if a user is subscribed to your channel.
	 * chat_login // Ability to log into chat and send messages.
	 * 
	 * @author agaida
	 */
	public static enum Permission {
		user_read, user_blocks_edit, user_blocks_read, user_follows_edit, channel_read, channel_editor, channel_commercial, channel_stream, channel_subscriptions, user_subscriptions, channel_check_subscription, chat_login
	}
	
	public static enum HttpType {
		GET, PUT, DELETE
	}

	/**
	 * Sets oAuth2 token for this instance
	 * 
	 * @param token
	 *            oAuth2 token which includes permission for the useraccount.
	 * @return TwitchAPI instance
	 */	
	public static void setAuthToken(String token){
		oAuthToken = token.trim();
	}

	/**
	 * Returns a TwitchAPIResource object which includes its defined datasets.
	 * 
	 * @param url
	 *            URL to twitch api resource. Example:
	 *            https://api.twitch.tv/kraken/users/its1z0 for User.class
	 * @param clazz
	 *            Class which specifies how the request json object from @param
	 *            url should be wrapped
	 * @return Returns TwitchAPIResource as specified @param clazz
	 * @throws IOException
	 * @throws TwitchAPIUnauthorizedAccessException
	 * @throws JsonSyntaxException
	 */
	public static <T extends TwitchAPIResource> T getResource(String url, Class<T> clazz) throws IOException {
		return DefaultResourceBuilder.buildResource(API_ROOT_URL+url, clazz);
	}
	
	/**
	 * Sends a raw api request to given URL.
	 * 
	 * @param urlString
	 *            URL to twitch api resource.
	 * @param token
	 *            oAuth2 token which includes permission for the useraccount.
	 * @return Returns string in json format.
	 * @throws TwitchAPIUnauthorizedAccessException
	 * @throws IOException
	 */
	public static String request(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		
		httpCon.setRequestMethod(HttpType.GET.toString());	
		httpCon.setRequestProperty("Accept","application/vnd.twitchtv.v3+json");
		httpCon.setRequestProperty("Authorization", "OAuth " + oAuthToken);
		
		if (httpCon.getDoInput()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			String input = reader.readLine();
			return input;
		}
		
		LogManager.getLogger().debug(httpCon.getResponseCode());
		return "";
	}
	
	public static String send(String urlString, HttpType type) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		
		if (type.equals(HttpType.PUT) || type.equals(HttpType.DELETE)) httpCon.setDoOutput(true);
		httpCon.setRequestMethod(type.toString());	
		httpCon.setRequestProperty("Accept","application/vnd.twitchtv.v3+json");
		httpCon.setRequestProperty("Authorization", "OAuth " + oAuthToken);
		
		if (httpCon.getDoOutput()) {
			OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			out.write("Resource content");
			out.close();
		}
		
		if (httpCon.getDoOutput()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			String input = reader.readLine();
			return input;
		}
		return "";
	}

	/**
	 * Checks if specified oAuth2 token is valid.
	 * 
	 * @return Validity of token
	 * @throws IOException
	 * @throws TwitchAPIUnauthorizedAccessException
	 * @throws TwitchAPINoTokenSpecifiedException 
	 * @throws TwitchAPIAuthTokenInvalidException 
	 * @throws JsonSyntaxException
	 */
	public static boolean authorized() throws IOException, TwitchAPINoTokenSpecifiedException, TwitchAPIAuthTokenInvalidException {
		if (oAuthToken == "" || oAuthToken == null) throw new TwitchAPINoTokenSpecifiedException();
		Root root = getResource("", Root.class);
		if (root != null) {
			if (!root.token.valid) throw new TwitchAPIAuthTokenInvalidException();
		}
		return true;
	}

	/**
	 * Returns a list of permissions for valid oAuth2 token.
	 * 
	 * @return null if no permissions exist
	 * @throws IOException
	 * @throws TwitchAPIAuthTokenInvalidException 
	 * @throws TwitchAPINoTokenSpecifiedException 
	 * @throws TwitchAPIUnauthorizedAccessException
	 * @throws JsonSyntaxException
	 */
	public static List<String> getPermissions() throws IOException, TwitchAPINoTokenSpecifiedException, TwitchAPIAuthTokenInvalidException {
		if (authorized()) return getResource("", Root.class).token.authorization.scopes;
		else return null;
	}
	
	/**
	 * Checks if specified oAuth2 token has given permissions.
	 * 
	 * @param p
	 *            Array of permissions
	 * @return self-explanatory
	 * @throws IOException
	 * @throws TwitchAPIUnauthorizedAccessException
	 * @throws TwitchAPINoPermissionException 
	 * @throws TwitchAPIAuthTokenInvalidException 
	 * @throws TwitchAPINoTokenSpecifiedException 
	 * @throws JsonSyntaxException
	 */
	public static boolean hasPermisson(Permission... p) throws IOException, TwitchAPINoPermissionException, TwitchAPINoTokenSpecifiedException, TwitchAPIAuthTokenInvalidException {
		List<String> permissionList = getPermissions();		
		for (Permission pe : p) {
			boolean has = false;
			for (String s : permissionList) {
				if (Permission.valueOf(s).equals(pe)) {
					has = true;
					break;
				}
			}
			if (!has) throw new TwitchAPINoPermissionException("Missing permission: " + pe);
		}
		return true;
	}
	
	private static String parsePermissonToString(Permission... p){
		String pString = "";
		for (Permission permission : p){
			pString += permission.toString()+"%20";
		}
		if (pString == "") return "";
		else return pString.substring(0,pString.length()-3);
	}
	
	public static void authorizeApp(Permission... permissons) throws IOException, URISyntaxException{
		String request = "https://api.twitch.tv/kraken/oauth2/authorize"+
		    "?response_type=token"+
		    "&client_id=%clientid%"+
		    "&redirect_uri=%redirecturi%"+
		    "&scope=%scope%";
		
		request = request.replace("%clientid%", CLIENT_ID);
		request = request.replace("%redirecturi%", REDIRECT_URI);
		request = request.replace("%scope%", parsePermissonToString(permissons));
		
		Desktop desktop = Desktop.getDesktop();
		if (desktop != null) desktop.browse(new URI(request));
	}
}
