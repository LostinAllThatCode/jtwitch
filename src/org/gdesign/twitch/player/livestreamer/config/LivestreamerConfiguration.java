package org.gdesign.twitch.player.livestreamer.config;

import org.gdesign.utils.GsonDataSet;

public class LivestreamerConfiguration extends GsonDataSet {
	public String path;
	public String args;
	public HttpService http;
	
	public class HttpService {
		public String ip;
		public int port;
		public int range;
		public String quality;
	}
}
