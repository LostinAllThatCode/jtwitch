package org.gdesign.jtwitch.player.gui.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.SwingPropertyChangeSupport;

import org.gdesign.twitch.api.prototypes.TChannel;
import org.gdesign.twitch.api.util.TwitchAPI;
import org.json.simple.parser.ParseException;

public class ChannelListModel {
	
	private SwingPropertyChangeSupport propertyChange;
	private PropertyChangeListener listener;
	private ArrayList<ChannelModel> channels;
	
	public ChannelListModel(final String username) {
		channels = new ArrayList<>();
		propertyChange = new SwingPropertyChangeSupport(this);
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							for (TChannel channel : new TwitchAPI().getFollows(username).getChannels()){
								if (getChannel(channel.get("display_name")) == null) {
									ChannelModel m = new ChannelModel(channel.get("display_name"));
									m.addModelListener(listener);
									addChannel(m);
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}, 250, 30000);
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
		listener = prop;
    }
	
	public ArrayList<ChannelModel> getSortedChannels(){
		Collections.sort(channels);
		return channels;
	}
	
	public synchronized ChannelModel addChannel(ChannelModel m){
		channels.add(m);
		propertyChange.firePropertyChange("addChannel", null, m);
		return m;
	}
	
	public synchronized void removeChannel(ChannelModel m){
		channels.remove(m);
	}
	
	public synchronized ChannelModel getChannel(String name){
		for (ChannelModel m : channels){
			if (m.getName().compareTo(name)==0) return m;
		}
		return null;
	}
	
}
