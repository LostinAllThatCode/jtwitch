package org.gdesign.twitch.api.resource.follows;

import java.util.Date;

import org.gdesign.twitch.api.resource.Links;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.channels.Channel;

public class Follows extends TwitchAPIResource{
	public Date created_at;
	public Links _links;
	public boolean notifications;
	public Channel channel;
}
