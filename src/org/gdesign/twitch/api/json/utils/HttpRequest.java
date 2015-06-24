package org.gdesign.twitch.api.json.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.json.type.TChannel;
import org.gdesign.twitch.api.json.type.TFollows;
import org.gdesign.twitch.api.json.type.TStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpRequest {
	
	public static enum JSONRequest { CHANNEL, FOLLOWS, STREAM }	
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
	
	public TFollows getFollows(String user) throws ParseException{
		return new TFollows((JSONObject) get(user,JSONRequest.FOLLOWS));
	}
	
	public TChannel getChannel(String channel) throws ParseException{
		return new TChannel((JSONObject) get(channel,JSONRequest.CHANNEL));
	}
	
	public TStream getStream(String stream) throws ParseException{
		return new TStream((JSONObject) get(stream,JSONRequest.STREAM));
	}
	
	private Object get(String keyword, JSONRequest request) throws ParseException {
		String req = null;
		switch (request) {
			case CHANNEL:
				req = request("https://api.twitch.tv/kraken/channels/"+keyword.toLowerCase());
				break;
			case FOLLOWS:
				req = request("https://api.twitch.tv/kraken/users/"+keyword.toLowerCase()+"/follows/channels");
				break;
			case STREAM:
				req = request("https://api.twitch.tv/kraken/streams/"+keyword.toLowerCase());
				break;
			default:
				return null;
		}
		LogManager.getLogger().debug(keyword);
		if (req != null) return jsonParserInstance.parse(req,containerFactory); else return null;
	}
	
	public String request(String request_url) throws ParseException{
		try {
			URL url = new URL(request_url.replaceAll(" ", ""));
			URLConnection con = url.openConnection();
			con.connect();
			InputStreamReader in = new InputStreamReader(con.getInputStream());
			if (in != null)	return jsonParserInstance.parse(new BufferedReader(in)).toString();
		} catch (IOException e) {
			LogManager.getLogger().error(e);
		}
		return null;
	}
}
