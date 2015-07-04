package org.gdesign.twitch.api.resource.builder;

import java.io.IOException;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.users.UserFollowedChannels;
import org.gdesign.twitch.api.resource.users.UserFollowedStreams;

import com.google.gson.Gson;

public class DefaultResourceBuilder {

	
	public static <T extends TwitchAPIResource> T buildResource(String url, Class<T> clazz) throws IOException {		
		if (clazz.equals(UserFollowedChannels.class)) {
			String jsonData = TwitchAPI.request(url+"?limit=100");
			UserFollowedChannels temp = new Gson().fromJson(jsonData, UserFollowedChannels.class);
			for (int i=100; i<=temp._total; i+=100){
				UserFollowedChannels toAdd = new Gson().fromJson(TwitchAPI.request(url+"?limit=100&offset="+i), UserFollowedChannels.class);
				temp.follows.addAll(toAdd.follows);
			}
			return clazz.cast(temp.setJsonData(jsonData,clazz));
		} else if (clazz.equals(UserFollowedStreams.class)) {
			String jsonData = TwitchAPI.request(url+"?limit=100");
			UserFollowedStreams temp = new Gson().fromJson(jsonData, UserFollowedStreams.class);
			for (int i=100; i<=temp._total; i+=100){
				UserFollowedStreams toAdd = new Gson().fromJson(TwitchAPI.request(url+"?limit=100&offset="+i), UserFollowedStreams.class);
				temp.streams.addAll(toAdd.streams);
			}
			return clazz.cast(temp.setJsonData(jsonData,clazz));
		} else {
			String jsonData = TwitchAPI.request(url);
			return new Gson().fromJson(TwitchAPI.request(url),clazz).setJsonData(jsonData,clazz);	
		}
	}
}
