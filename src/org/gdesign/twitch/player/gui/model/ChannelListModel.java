package org.gdesign.twitch.player.gui.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.streams.StreamInfo;

public class ChannelListModel {

	private List<ChannelModel> channels;
	private HashMap<ChannelModel,Thread> threads;

	public ChannelListModel() {
		this.channels = Collections.synchronizedList(new ArrayList<ChannelModel>());
		this.threads = new HashMap<ChannelModel,Thread>();
	}

	public ChannelModel createChannel(String channelName, String displayName) {
		ChannelModel m = new ChannelModel(channelName);
		m.setDisplayname(displayName);
		channels.add(m);
		return m;
	}
	
	public synchronized void updateChannelModel(final ChannelModel m) {
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (m != null) {
					try {
						StreamInfo streamInfo = new TwitchAPI().getResource("https://api.twitch.tv/kraken/streams/"+m.getName(), StreamInfo.class);
						if (streamInfo.stream != null) {
							m.setGame(streamInfo.stream.game);
							m.setViewers(streamInfo.stream.viewers);
							m.setOnline(true);
							m.setIconUrl(streamInfo.stream.channel.logo);
						} else { 
							m.setGame("").setViewers(0).setOnline(false);
						}
					} catch (TwitchAPIUnauthorizedAccessException | IOException e) {
						LogManager.getLogger().error(e);
						e.printStackTrace();
					}
				}
				threads.remove(m);
			}
		});
		threads.put(m,t);
		t.start();
	}

	public void waitForUpdate() throws InterruptedException{
		while (threads.size() != 0) {
			Thread.sleep(250);
		}
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
	
	public void removeAll(){
		channels.clear();
	}

}
