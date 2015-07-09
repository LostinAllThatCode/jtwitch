package org.gdesign.twitch.api.resource.follows;

import java.util.List;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Links;

public class FollowList extends TwitchAPIResource {
	public Links _links;
	public int _total;
	public List<Follow> follows;

}
