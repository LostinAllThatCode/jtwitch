package org.gdesign.twitch.player.gui.model;

import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;

public class EmbeddedPlayerModel {

	private LivestreamerInstance instance;

	public void setInstance(LivestreamerInstance live) {
		this.instance = live;
	}

	public LivestreamerInstance getInstance() {
		return instance;
	}

}
