package org.gdesign.twitch.api.resource.streams;

import java.util.Date;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.channels.Channel;

public class Stream extends TwitchAPIResource {
	public String game;
	public int viewers;
	public float average_fps;
	public int video_height;
	public Date created_at;
	public int id;
	public Channel channel;
}
