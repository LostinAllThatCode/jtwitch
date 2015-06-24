package org.gdesign.twitch.player.gui.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.SwingPropertyChangeSupport;

public class ChannelListModel {
	
	private PropertyChangeListener listener;
	private SwingPropertyChangeSupport propertyChange;
	private List<ChannelModel> channels;
	private String username;
	
	public ChannelListModel(final String username) {
		this.channels 		= Collections.synchronizedList(new ArrayList<ChannelModel>());
		this.propertyChange = new SwingPropertyChangeSupport(this);
		this.setUsername(username);
	}
	
	public ChannelModel createChannel(String channelName, String displayName){
		ChannelModel m = new ChannelModel(channelName);
		m.setDisplayname(displayName);
		m.addModelListener(listener);
		addChannel(m);
		return m;
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(listener = prop);
    }
	
	public Collection<ChannelModel> getSortedChannels(){
		Collections.sort(channels);
		return channels;
	}
	
	private void addChannel(ChannelModel m){
		channels.add(m);
		propertyChange.firePropertyChange("addChannel", null, m);
	}
	
	public void removeChannel(ChannelModel m){
		channels.remove(m);
		propertyChange.firePropertyChange("removeChannel", null, m);
	}
	
	public ChannelModel getChannel(String name){
		for (ChannelModel m : channels){
			if (m.getName().compareTo(name)==0) return m;
		}
		return null;
	}
	
	public int getChannelCount(){
		return channels.size();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
