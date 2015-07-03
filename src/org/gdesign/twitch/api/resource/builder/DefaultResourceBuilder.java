package org.gdesign.twitch.api.resource.builder;

import java.io.IOException;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.users.UserFollowedChannels;
import org.gdesign.twitch.api.resource.users.UserFollowedStreams;

import com.google.gson.Gson;

public class DefaultResourceBuilder {
	
	public static <T extends TwitchAPIResource> T buildResource(String url, String token, Class<T> clazz) throws IOException, TwitchAPIUnauthorizedAccessException{		
		if (clazz.equals(UserFollowedChannels.class)) {
			UserFollowedChannels temp = new Gson().fromJson(TwitchAPI.request(url+"?limit=100", token), UserFollowedChannels.class);
			for (int i=100; i<=temp._total; i+=100){
				UserFollowedChannels toAdd = new Gson().fromJson(TwitchAPI.request(url+"?limit=100&offset="+i, token), UserFollowedChannels.class);
				temp.follows.addAll(toAdd.follows);
			}
			return clazz.cast(temp);
		} else if (clazz.equals(UserFollowedStreams.class)) {
			UserFollowedStreams temp = new Gson().fromJson(TwitchAPI.request(url+"?limit=100", token), UserFollowedStreams.class);
			for (int i=100; i<=temp._total; i+=100){
				UserFollowedStreams toAdd = new Gson().fromJson(TwitchAPI.request(url+"?limit=100&offset="+i, token), UserFollowedStreams.class);
				temp.streams.addAll(toAdd.streams);
			}
			return clazz.cast(temp);
		}
		return new Gson().fromJson(TwitchAPI.request(url+"?limit=100", token),clazz);	
	}
}
