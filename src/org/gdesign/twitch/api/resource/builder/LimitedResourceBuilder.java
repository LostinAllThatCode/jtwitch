package org.gdesign.twitch.api.resource.builder;

import java.io.IOException;
import java.util.ArrayList;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.helper.HelperGetTotal;


import org.gdesign.twitch.api.resource.streams.StreamList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LimitedResourceBuilder {
	
	private static int LIMIT_API_MAX = 100;
	private static int LIMIT_BUILDER_MAX = 1000;
	
	public static <T extends TwitchAPIResource> T buildResource(String url, Class<T> clazz, int count) throws IOException, JsonSyntaxException, InterruptedException {		
		HelperGetTotal dataSize =  new Gson().fromJson(TwitchAPI.request(url+"?limit=1"),HelperGetTotal.class);
		if (dataSize._total > 0) {
			if (count <= LIMIT_API_MAX) {
				return new Gson().fromJson(TwitchAPI.request(url+"?limit="+count),clazz);
			} else {
				if (count > dataSize._total) count = dataSize._total;
				if (count > LIMIT_BUILDER_MAX) count = LIMIT_BUILDER_MAX;
				if (clazz.equals(StreamList.class)) {
					StreamList resource = new Gson().fromJson(TwitchAPI.request(url+"?limit="+LIMIT_API_MAX),StreamList.class);
					count-=100;
					final ArrayList<T> requests = new ArrayList<T>();
					int countThreads = ((int) count / LIMIT_API_MAX) + (count % LIMIT_API_MAX > 0 ? 1 : 0);  
					
					int limit = 0;
					int offset=100; 
					while (requests.size() != countThreads) {
						if (((int) count / LIMIT_API_MAX) > 0) limit = 100; else limit = count % LIMIT_API_MAX;
						if (count != 0) startThread(url+"?limit="+limit+"&offset="+offset,clazz,requests);
						count-=limit;
						offset+=limit;
						Thread.sleep(250);
					}

					for (T t : requests) {
						StreamList streamlist = (StreamList) t;
						resource.streams.addAll(streamlist.streams);
					}
					
					return clazz.cast(resource);
				} else throw new IOException("Class is not supported yet. Please use buildResource(url,clazz).");
			}
		}
		return null;
	}
	
	private static <T extends TwitchAPIResource> void startThread(final String url, final Class<T> clazz, final ArrayList<T> arraylist) throws JsonSyntaxException, IOException, InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					arraylist.add(new Gson().fromJson(TwitchAPI.request(url),clazz));
				} catch (JsonSyntaxException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
