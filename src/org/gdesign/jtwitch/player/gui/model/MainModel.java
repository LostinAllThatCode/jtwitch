package org.gdesign.jtwitch.player.gui.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.event.SwingPropertyChangeSupport;

public class MainModel {
	
	private SwingPropertyChangeSupport propertyChange;
	private ChannelListModel channelListModel;
	private EmbeddedPlayerModel embeddedPlayerModel;
	
	public MainModel(String username) {
		channelListModel = new ChannelListModel(username);
		embeddedPlayerModel = new EmbeddedPlayerModel();
		propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public synchronized ChannelModel addChannel(ChannelModel c){
		return channelListModel.addChannel(c);
	}
	
	public synchronized void removeChannel(String name){
		removeChannel(getChannel(name));	
	}
	
	public synchronized void removeChannel(ChannelModel m){
		channelListModel.removeChannel(m);
	}
	
	public synchronized ChannelModel getChannel(String name){
		return channelListModel.getChannel(name);
	}
	
	public ArrayList<ChannelModel> getSortedChannels(){
		return channelListModel.getSortedChannels();
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
		channelListModel.addModelListener(prop);
    }
	
	public EmbeddedPlayerModel getPlayerModel(){
		return embeddedPlayerModel;
	}
}
