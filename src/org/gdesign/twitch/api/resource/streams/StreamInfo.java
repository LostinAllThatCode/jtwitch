package org.gdesign.twitch.api.resource.streams;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Links;

public class StreamInfo extends TwitchAPIResource {
	public Links _links;
	public Stream stream;
	public Preview preview;
}
