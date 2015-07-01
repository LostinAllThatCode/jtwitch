package org.gdesign.twitch.api.resource.users;

import java.util.List;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.follows.Follows;
import org.gdesign.twitch.api.resource.root.Links;

public class UserFollowedChannels extends TwitchAPIResource {
	public Links _links;
	public int _total;
	public List<Follows> follows;

}
