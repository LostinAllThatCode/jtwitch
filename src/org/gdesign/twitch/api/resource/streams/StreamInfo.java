package org.gdesign.twitch.api.resource.streams;

import org.gdesign.twitch.api.resource.Links;
import org.gdesign.twitch.api.resource.TwitchAPIResource;

public class StreamInfo extends TwitchAPIResource {
	public Links _links;
	public Stream stream;
	public Preview preview;
}
