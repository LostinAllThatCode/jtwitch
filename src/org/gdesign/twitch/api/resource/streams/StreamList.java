package org.gdesign.twitch.api.resource.streams;

import java.util.List;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Links;

public class StreamList extends TwitchAPIResource {
	public Links _links;
	public int _total;
	public List<Stream> streams;
}
