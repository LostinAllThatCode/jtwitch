package org.gdesign.twitch.player.gui.model;

public class MainModel {
	
	private EmbeddedPlayerModel embeddedPlayerModel;

	public MainModel() {
		embeddedPlayerModel = new EmbeddedPlayerModel();
	}

	public EmbeddedPlayerModel getEmbeddedPlayerModel() {
		return embeddedPlayerModel;
	}
}
