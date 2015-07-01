package org.gdesign.twitch.player.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChannelListModel {

	private List<ChannelModel> channels;

	public ChannelListModel() {
		this.channels = Collections
				.synchronizedList(new ArrayList<ChannelModel>());
	}

	public ChannelModel createChannel(String channelName, String displayName) {
		ChannelModel m = new ChannelModel(channelName);
		m.setDisplayname(displayName);
		channels.add(m);
		return m;
	}

	public Collection<ChannelModel> getChannels() {
		Collections.sort(channels);
		return channels;
	}

	public void removeChannel(ChannelModel m) {
		channels.remove(m);
	}

	public ChannelModel getChannel(String name) {
		for (ChannelModel m : channels) {
			if (m.getName().compareTo(name) == 0)
				return m;
		}
		return null;
	}

}
