package org.gdesign.twitch.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gdesign.twitch.api.prototypes.TChannel;
import org.gdesign.twitch.api.prototypes.TFollows;
import org.gdesign.twitch.api.prototypes.TStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TwitchAPI {
	
	private static ContainerFactory containerFactory;
	private static JSONParser jsonParserInstance;
	
	static { 
		jsonParserInstance = new JSONParser(); 
		new ContainerFactory(){
		    @Override
			@SuppressWarnings("rawtypes")
			public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    @Override
			@SuppressWarnings("rawtypes")
			public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };		
	}
	
	public static enum JSONRequest {
		CHANNEL, FOLLOWS, STREAM
	}	
	
	public static TFollows getFollows(String user) throws ParseException{
		return new TFollows((JSONObject) get(user,JSONRequest.FOLLOWS));
	}
	
	public static TChannel getChannel(String channel) throws ParseException{
		return new TChannel((JSONObject) get(channel,JSONRequest.CHANNEL));
	}
	
	public static TStream getStream(String stream) throws ParseException{
		return new TStream((JSONObject) get(stream,JSONRequest.STREAM));
	}
	
	private static Object get(String keyword, JSONRequest request) throws ParseException {
		switch (request) {
			case CHANNEL:
				return jsonParserInstance.parse(request("https://api.twitch.tv/kraken/channels/"+keyword.toLowerCase()),containerFactory);
			case FOLLOWS:
				return jsonParserInstance.parse(request("https://api.twitch.tv/kraken/users/"+keyword.toLowerCase()+"/follows/channels"),containerFactory);
			case STREAM:
				return jsonParserInstance.parse(request("https://api.twitch.tv/kraken/streams/"+keyword.toLowerCase()),containerFactory);
			default:
				break;
			}
		return null;
	}
	
	public static String request(String request_url) throws ParseException{
		try {
			URL url = new URL( request_url );
			
			URLConnection con = url.openConnection();
			con.connect();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			return jsonParserInstance.parse(br).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
