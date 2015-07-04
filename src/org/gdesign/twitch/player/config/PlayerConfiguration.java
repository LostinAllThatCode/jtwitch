package org.gdesign.twitch.player.config;

import org.gdesign.utils.GsonDataSet;

public class PlayerConfiguration extends GsonDataSet {
	public String token;
	public PlayerSettings player;

	public class PlayerSettings {
		public int volume;
	}
}
