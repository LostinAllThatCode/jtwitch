package org.gdesign.twitch.api.resource.root;
import java.util.List;

import org.gdesign.twitch.api.resource.TwitchAPIResource;

public class Authorization extends TwitchAPIResource {	
	public List<String> scopes;
	public String created_at;
	public String updated_at;
}
