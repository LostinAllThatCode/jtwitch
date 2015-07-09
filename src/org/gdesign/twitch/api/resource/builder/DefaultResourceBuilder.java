package org.gdesign.twitch.api.resource.builder;

import java.io.IOException;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.TwitchAPIResource;

import com.google.gson.Gson;

public class DefaultResourceBuilder {
	
	public static <T extends TwitchAPIResource> T buildResource(String url, Class<T> clazz) throws IOException {
		return new Gson().fromJson(TwitchAPI.request(url),clazz);	
	}
}
