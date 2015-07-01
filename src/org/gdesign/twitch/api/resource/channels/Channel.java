package org.gdesign.twitch.api.resource.channels;

import java.net.URL;
import java.util.Date;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Links;

public class Channel extends TwitchAPIResource {
	
	public boolean mature;
	public String status;
	public String broadcaster_language;
	public String display_name;
	public String game;
	public int delay;
	public String language;
	public int id;
	public String name;
	public Date created_at;
	public Date updated_at;
	public URL logo;
	public URL banner;
	public URL video_banner;
	public URL background;
	public URL profile_banner;
	public String profile_banner_background_color;
	public boolean partner;
	public URL url;
	public int views;
	public int followers;
	public Links _links;
}
