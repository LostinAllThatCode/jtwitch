package org.gdesign.twitch.api.resource.follows;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.channels.Channel;
import org.gdesign.twitch.api.resource.root.Links;

public class Follow extends TwitchAPIResource {
	public String created_at;
	public Links _links;
	public boolean notifications;
	public Channel channel;
}
