package org.gdesign.twitch.api.resource.users;

import java.util.List;

import org.gdesign.twitch.api.resource.Links;
import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.follows.Follows;

public class UserFollowedChannels extends TwitchAPIResource {
	public Links _links;
	public int _total;
	public List<Follows> follows;

}
