package org.gdesign.jtwitch.player.gui.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.SwingPropertyChangeSupport;

public class ChannelListModel {
	
	private SwingPropertyChangeSupport propertyChange;
	private List<ChannelModel> channels;
	
	public ChannelListModel(final String username) {
		channels = Collections.synchronizedList(new ArrayList<ChannelModel>());
		propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
    }
	
	public Collection<ChannelModel> getSortedChannels(){
		Collections.sort(channels);
		return channels;
	}
	
	public ChannelModel addChannel(ChannelModel m){
		channels.add(m);
		propertyChange.firePropertyChange("addChannel", null, m);
		return m;
	}
	
	public void removeChannel(ChannelModel m){
		channels.remove(m);
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
	
	public Collection<ChannelModel> getChannels(){
		return channels;
	}
	
}
