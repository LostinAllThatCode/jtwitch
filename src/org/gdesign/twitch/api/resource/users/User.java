package org.gdesign.twitch.api.resource.users;

import java.net.URL;
import java.util.Date;

import org.gdesign.twitch.api.resource.TwitchAPIResource;
import org.gdesign.twitch.api.resource.root.Links;

public class User extends TwitchAPIResource {
	public String type;
	public String name;
	public Date created_at;
	public Date updated_at;
	public Links _links;
	public URL logo;
	public int _id;
	public String display_name;
	public String bio;
}
