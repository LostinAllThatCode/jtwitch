package org.gdesign.twitch.player.gui.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.streams.StreamInfo;

public class ChannelListModel {
	
	private Map<String, ChannelModel> channels;
	private List<ChannelModel> channelsCopy;

	public ChannelListModel() {
		this.channels = Collections.synchronizedMap(new HashMap<String, ChannelModel>());
	}

	public ChannelModel createChannel(String channelName) {
		ChannelModel m = new ChannelModel(channelName);
		channels.put(channelName,m);
		return m;	
	}
	
	public synchronized boolean updateChannelModel(final ChannelModel m) {
		if (m != null) {
			try {
				//synchronized (channels) {							
					boolean changed = false;
					StreamInfo streamInfo = TwitchAPI.getResource("/streams/"+m.getName(), StreamInfo.class);
					if (streamInfo.stream != null) {
						if (m.getGame().compareTo(streamInfo.stream.game) != 0) {
							m.setGame(streamInfo.stream.game);
							changed=true;
						}
						if (m.getViewers() != streamInfo.stream.viewers) {
							m.setViewers(streamInfo.stream.viewers);
							changed=true;
						}
						if (!m.isOnline()) {
							m.setOnline(true);
							changed=true;
						}
						if (!m.getIconUrl().equals(streamInfo.stream.channel.logo)) {
							m.setIconUrl(streamInfo.stream.channel.logo);
							changed=true;
						}
					} else { 
						if (m.getGame().compareTo("") != 0) {
							m.setGame("");
							changed=true;
						}
						if (m.getViewers() != 0) {
							m.setViewers(0);
							changed=true;
						}
						if (m.isOnline()) {
							m.setOnline(false);
							changed=true;
						}
					}
					return changed;
				//}
			} catch (IOException e) {
				LogManager.getLogger().error(e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*
	private void cleanChannelList(){
		for (ChannelModel m : channels) {
			try {
				new TwitchAPI().getResource("https://api.twitch.tv/kraken/users/"+username+"/follows/channels/"+m.getName(), Follows.class);
			} catch (IOException | TwitchAPIUnauthorizedAccessException e) {
				LogManager.getLogger().debug("Your no longer following :" + m.getName());
				delete.add(m);
			}
		}
		for (ChannelModel m : delete) channels.remove(m);
	}
	*/
	
	public Collection<ChannelModel> sortChannels() {
		channelsCopy = new ArrayList<ChannelModel>(channels.values());
		Collections.sort(channelsCopy);
		return channelsCopy;	
	}
	
	public Collection<ChannelModel> getChannels() {
		return channelsCopy;
	}

	public void removeChannel(String channelName) {
		synchronized (channels) {
			channels.remove(channelName);
		}
	}

	public ChannelModel getChannel(String channelName) {
		return channels.get(channelName);
	}
	
	public void removeAll(){
		synchronized (channels) {
			channels.clear();
			channelsCopy.clear();
			channelsCopy = null;
		}
	}

}
