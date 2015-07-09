package org.gdesign.twitch.api.resource.streams;

import java.net.URL;

import org.gdesign.twitch.api.resource.TwitchAPIResource;

public class Featured extends TwitchAPIResource {
	public URL image;
	public String text;
	public String title;
	public int priority;
	public boolean sponsored;
	public boolean scheduled;
	public Stream stream;
}
