package org.gdesign.twitch.player.gui.model;

public class MainModel {

	private ChannelListModel channelListModel;
	private EmbeddedPlayerModel embeddedPlayerModel;

	public MainModel() {
		channelListModel = new ChannelListModel();
		embeddedPlayerModel = new EmbeddedPlayerModel();
	}

	public EmbeddedPlayerModel getEmbeddedPlayerModel() {
		return embeddedPlayerModel;
	}

	public ChannelListModel getChannelListModel() {
		return channelListModel;
	}
}
